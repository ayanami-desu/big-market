package re.yuugu.hzx.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import re.yuugu.hzx.domain.rebate.event.SendRebateMessageEvent;
import re.yuugu.hzx.domain.rebate.model.aggregate.CreateRebateOrderAggregate;
import re.yuugu.hzx.domain.rebate.model.entity.RebateOrderEntity;
import re.yuugu.hzx.domain.rebate.model.vo.BehaviorRebateVO;
import re.yuugu.hzx.domain.rebate.model.vo.BehaviorTypeVO;
import re.yuugu.hzx.domain.rebate.model.vo.RebateTypeVO;
import re.yuugu.hzx.domain.rebate.repository.IBehaviorRebateRepository;
import re.yuugu.hzx.infrastructure.event.EventPublisher;
import re.yuugu.hzx.infrastructure.persistent.dao.ITaskDao;
import re.yuugu.hzx.infrastructure.persistent.dao.IUserBehaviorRebateDao;
import re.yuugu.hzx.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import re.yuugu.hzx.infrastructure.persistent.po.Task;
import re.yuugu.hzx.infrastructure.persistent.po.UserBehaviorRebate;
import re.yuugu.hzx.infrastructure.persistent.po.UserBehaviorRebateOrder;
import re.yuugu.hzx.infrastructure.persistent.redis.IRedisService;
import re.yuugu.hzx.types.common.Constants;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.event.EventTask;
import re.yuugu.hzx.types.exception.AppException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ author anon
 * @ description BehaviorRebateRepository
 * @ create 2026/1/25 09:40
 */
@Repository
@Slf4j
public class BehaviorRebateRepository implements IBehaviorRebateRepository {
    @Resource
    private IUserBehaviorRebateDao userBehaviorRebateDao;
    @Resource
    private IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IRedisService redisService;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private IDBRouterStrategy dbRouter;

    @Override
    public List<BehaviorRebateVO> queryBehaviorRebateConfig(String behaviorType) {
        String cacheKey = Constants.RedisKeys.REBATE_BEHAVIOR_CONFIG_KEY + "_" + behaviorType;
        List<BehaviorRebateVO> behaviorRebateVOList = redisService.getValue(cacheKey);
        if (behaviorRebateVOList != null) {
            return behaviorRebateVOList;
        }
        behaviorRebateVOList = new ArrayList<>();
        List<UserBehaviorRebate> userBehaviorRebateList = userBehaviorRebateDao.queryBehaviorRebateConfig(behaviorType);
        if (userBehaviorRebateList == null || userBehaviorRebateList.isEmpty()) {
            return Collections.emptyList();
        }
        for (UserBehaviorRebate userBehaviorRebate : userBehaviorRebateList) {
            BehaviorRebateVO behaviorRebateVO = BehaviorRebateVO.builder()
                    .behaviorType(BehaviorTypeVO.valueOf(userBehaviorRebate.getBehaviorType()))
                    .rebateDesc(userBehaviorRebate.getRebateDesc())
                    .rebateType(RebateTypeVO.valueOf(userBehaviorRebate.getRebateType()))
                    .rebateConfig(userBehaviorRebate.getRebateConfig())
                    .build();
            behaviorRebateVOList.add(behaviorRebateVO);
        }
        redisService.setValue(cacheKey, behaviorRebateVOList);
        return behaviorRebateVOList;
    }

    @Override
    public void doSaveRebateOrderAggregate(CreateRebateOrderAggregate createRebateOrderAggregate) {
        String userId = createRebateOrderAggregate.getUserId();
        RebateOrderEntity rebateOrderEntity = createRebateOrderAggregate.getRebateOrderEntity();
        EventTask<SendRebateMessageEvent.SendRebateMessage> eventTask = createRebateOrderAggregate.getEventTask();

        UserBehaviorRebateOrder userBehaviorRebateOrder = UserBehaviorRebateOrder.builder()
                .userId(rebateOrderEntity.getUserId())
                .orderId(rebateOrderEntity.getOrderId())
                .behaviorType(rebateOrderEntity.getBehaviorType().getCode())
                .rebateDesc(rebateOrderEntity.getRebateDesc())
                .rebateType(rebateOrderEntity.getRebateType().getCode())
                .rebateConfig(rebateOrderEntity.getRebateConfig())
                .bizId(rebateOrderEntity.getBizId())
                .outBusinessNo(rebateOrderEntity.getOutBusinessNo())
                .build();
        Task task = Task.builder()
                .userId(eventTask.getUserId())
                .topic(eventTask.getTopic())
                .message(JSON.toJSONString(eventTask.getMessage()))
                .messageId(eventTask.getMessageId())
                .state(eventTask.getState().getCode())
                .build();
        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    userBehaviorRebateOrderDao.insert(userBehaviorRebateOrder);
                    taskDao.insert(task);
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入行为返利订单，唯一索引冲突", e);
                    throw new AppException(ResponseCode.DUPLICATE_KEY_EXCEPTION.getCode(), ResponseCode.DUPLICATE_KEY_EXCEPTION.getInfo());
                }
            });
        } finally {
            dbRouter.clear();
        }
        //发送MQ消息
        try {
            eventPublisher.publish(eventTask.getTopic(), eventTask.getMessage());
            taskDao.updateStateToCompleted(task);
        } catch (Exception e) {
            log.error("写入行为返利订单，发送mq消息失败");
            taskDao.updateStateToFail(task);
        }
    }

    @Override
    public List<RebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo) {
        UserBehaviorRebateOrder userBehaviorRebateOrderReq = new UserBehaviorRebateOrder();
        userBehaviorRebateOrderReq.setUserId(userId);
        userBehaviorRebateOrderReq.setOutBusinessNo(outBusinessNo);
        List<UserBehaviorRebateOrder> userBehaviorRebateOrderList = userBehaviorRebateOrderDao.queryOrderByOutBusinessNo(userBehaviorRebateOrderReq);
        if (userBehaviorRebateOrderList == null || userBehaviorRebateOrderList.isEmpty()) {
            return Collections.emptyList();
        }
        List<RebateOrderEntity> result = new ArrayList<>();
        for (UserBehaviorRebateOrder userBehaviorRebateOrder : userBehaviorRebateOrderList) {
            RebateOrderEntity rebateOrderEntity = RebateOrderEntity.builder()
                    .userId(userBehaviorRebateOrder.getUserId())
                    .orderId(userBehaviorRebateOrder.getOrderId())
                    .behaviorType(BehaviorTypeVO.valueOf(userBehaviorRebateOrder.getBehaviorType()))
                    .rebateDesc(userBehaviorRebateOrder.getRebateDesc())
                    .rebateType(RebateTypeVO.valueOf(userBehaviorRebateOrder.getRebateType()))
                    .rebateConfig(userBehaviorRebateOrder.getRebateConfig())
                    .bizId(userBehaviorRebateOrder.getBizId())
                    .outBusinessNo(userBehaviorRebateOrder.getOutBusinessNo())
                    .build();
            result.add(rebateOrderEntity);
        }
        return result;
    }
}
