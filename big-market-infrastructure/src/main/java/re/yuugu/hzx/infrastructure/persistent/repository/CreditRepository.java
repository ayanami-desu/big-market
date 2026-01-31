package re.yuugu.hzx.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import re.yuugu.hzx.domain.credit.event.CreditPaySuccessEvent;
import re.yuugu.hzx.domain.credit.model.aggregate.CreateCreditAdjustAggregate;
import re.yuugu.hzx.domain.credit.model.entity.CreditAdjustEntity;
import re.yuugu.hzx.domain.credit.model.entity.UserCreditOrderEntity;
import re.yuugu.hzx.domain.credit.model.vo.TradeTypeVO;
import re.yuugu.hzx.domain.credit.repository.ICreditRepository;
import re.yuugu.hzx.infrastructure.event.EventPublisher;
import re.yuugu.hzx.infrastructure.persistent.dao.ITaskDao;
import re.yuugu.hzx.infrastructure.persistent.dao.IUserCreditAccountDao;
import re.yuugu.hzx.infrastructure.persistent.dao.IUserCreditOrderDao;
import re.yuugu.hzx.infrastructure.persistent.po.Task;
import re.yuugu.hzx.infrastructure.persistent.po.UserCreditAccount;
import re.yuugu.hzx.infrastructure.persistent.po.UserCreditOrder;
import re.yuugu.hzx.infrastructure.persistent.redis.IRedisService;
import re.yuugu.hzx.types.common.Constants;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.event.EventTask;
import re.yuugu.hzx.types.exception.AppException;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ author anon
 * @ description CreditRepository
 * @ create 2026/1/28 14:37
 */
@Repository
@Slf4j
public class CreditRepository implements ICreditRepository {
    @Resource
    private IUserCreditAccountDao userCreditAccountDao;
    @Resource
    private IUserCreditOrderDao userCreditOrderDao;
    @Resource
    private ITaskDao taskDao;

    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private IRedisService redisService;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void doSaveCreditPayOrderAggregate(CreateCreditAdjustAggregate createCreditAdjustAggregate) {
        String userId = createCreditAdjustAggregate.getUserId();
        CreditAdjustEntity creditAdjustEntity = createCreditAdjustAggregate.getCreditAdjustEntity();
        UserCreditOrderEntity userCreditOrderEntity = createCreditAdjustAggregate.getUserCreditOrderEntity();
        EventTask<CreditPaySuccessEvent.ActivityOrderEvent> eventTask = createCreditAdjustAggregate.getEventTask();

        UserCreditOrder userCreditOrder = new UserCreditOrder();
        userCreditOrder.setUserId(userCreditOrderEntity.getUserId());
        userCreditOrder.setOrderId(userCreditOrderEntity.getOrderId());
        userCreditOrder.setTradeName(userCreditOrderEntity.getTradeName().getName());
        userCreditOrder.setTradeType(userCreditOrderEntity.getTradeType().getCode());
        userCreditOrder.setTradeAmount(userCreditOrderEntity.getTradeAmount());
        userCreditOrder.setOutBusinessNo(userCreditOrderEntity.getOutBusinessNo());

        Task task = Task.builder()
                .userId(eventTask.getUserId())
                .topic(eventTask.getTopic())
                .message(JSON.toJSONString(eventTask.getMessage()))
                .messageId(eventTask.getMessageId())
                .state(eventTask.getState().getCode())
                .build();

        RLock lock = redisService.getLock(Constants.RedisKeys.USER_CREDIT_ACCOUNT_LOCK + userId + Constants.UNDERLINE + userCreditOrderEntity.getOutBusinessNo());
        try {
            lock.lock(3, TimeUnit.SECONDS);
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    userCreditOrderDao.insert(userCreditOrder);
                    if (TradeTypeVO.FORWARD.getCode().equals(userCreditOrderEntity.getTradeType().getCode())) {
                        // 增加用户积分，如果不存在则创建
                        int cnt = userCreditAccountDao.updateUserCreditAccount(userId, creditAdjustEntity.getAdjustAmount());
                        if (cnt != 1) {
                            userCreditAccountDao.insert(userId, creditAdjustEntity.getAdjustAmount());
                        }
                    } else if (TradeTypeVO.REVERSE.getCode().equals(userCreditOrderEntity.getTradeType().getCode())) {
                        // 扣除用户积分，如果不存在则设置积分值为0
                        UserCreditAccount userCreditAccount = userCreditAccountDao.queryUserCreditAccountByUserId(userId);
                        if (userCreditAccount == null) {
                            // 扣减用户积分，但是积分账户不存在
                            status.setRollbackOnly();
                            log.error("扣减用户积分，但是积分账户不存在");
                        }
                        int cnt = userCreditAccountDao.minusUpdateUserCreditAccount(userId, creditAdjustEntity.getAdjustAmount());
                        if (cnt != 1) {
                            // 积分账户存在，但是积分不足
                            status.setRollbackOnly();
                            log.error("积分账户存在，但是积分不足");
                        }
                    }
                    taskDao.insert(task);
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入积分订单，唯一索引冲突", e);
                    throw new AppException(ResponseCode.DUPLICATE_KEY_EXCEPTION.getCode(), ResponseCode.DUPLICATE_KEY_EXCEPTION.getInfo());
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("写入积分订单，错误", e);
                    throw e;
                }
            });
        } finally {
            dbRouter.clear();
            lock.unlock();
        }
        //发送MQ消息
        try {
            log.info("发送积分支付成功消息");
            eventPublisher.publish(eventTask.getTopic(), eventTask.getMessage());
            taskDao.updateStateToCompleted(task);
        } catch (Exception e) {
            log.error("写入积分订单，发送mq消息失败");
            taskDao.updateStateToFail(task);
        }
    }

    @Override
    public void doSaveCreditAdjustAggregate(CreateCreditAdjustAggregate createCreditAdjustAggregate) {
        String userId = createCreditAdjustAggregate.getUserId();
        CreditAdjustEntity creditAdjustEntity = createCreditAdjustAggregate.getCreditAdjustEntity();
        UserCreditOrderEntity userCreditOrderEntity = createCreditAdjustAggregate.getUserCreditOrderEntity();


        UserCreditOrder userCreditOrder = new UserCreditOrder();
        userCreditOrder.setUserId(userCreditOrderEntity.getUserId());
        userCreditOrder.setOrderId(userCreditOrderEntity.getOrderId());
        userCreditOrder.setTradeName(userCreditOrderEntity.getTradeName().getName());
        userCreditOrder.setTradeType(userCreditOrderEntity.getTradeType().getCode());
        userCreditOrder.setTradeAmount(userCreditOrderEntity.getTradeAmount());
        userCreditOrder.setOutBusinessNo(userCreditOrderEntity.getOutBusinessNo());

        RLock lock = redisService.getLock(Constants.RedisKeys.USER_CREDIT_ACCOUNT_LOCK + userId + Constants.UNDERLINE + userCreditOrderEntity.getOutBusinessNo());
        try {
            lock.lock(3, TimeUnit.SECONDS);
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    userCreditOrderDao.insert(userCreditOrder);
                    if (TradeTypeVO.FORWARD.getCode().equals(userCreditOrderEntity.getTradeType().getCode())) {
                        // 增加用户积分，如果不存在则创建
                        int cnt = userCreditAccountDao.updateUserCreditAccount(userId, creditAdjustEntity.getAdjustAmount());
                        if (cnt != 1) {
                            userCreditAccountDao.insert(userId, creditAdjustEntity.getAdjustAmount());
                        }
                    } else if (TradeTypeVO.REVERSE.getCode().equals(userCreditOrderEntity.getTradeType().getCode())) {
                        // 扣除用户积分，如果不存在则设置积分值为0
                        UserCreditAccount userCreditAccount = userCreditAccountDao.queryUserCreditAccountByUserId(userId);
                        if (userCreditAccount == null) {
                            // 扣减用户积分，但是积分账户不存在
                            status.setRollbackOnly();
                            log.error("扣减用户积分，但是积分账户不存在");
                        }
                        int cnt = userCreditAccountDao.minusUpdateUserCreditAccount(userId, creditAdjustEntity.getAdjustAmount());
                        if (cnt != 1) {
                            // 积分账户存在，但是积分不足
                            status.setRollbackOnly();
                            log.error("积分账户存在，但是积分不足");
                        }
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入积分订单，唯一索引冲突", e);
                    throw new AppException(ResponseCode.DUPLICATE_KEY_EXCEPTION.getCode(), ResponseCode.DUPLICATE_KEY_EXCEPTION.getInfo());
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("写入积分订单，错误", e);
                    throw e;
                }
            });
        } finally {
            dbRouter.clear();
            lock.unlock();
        }
    }
}
