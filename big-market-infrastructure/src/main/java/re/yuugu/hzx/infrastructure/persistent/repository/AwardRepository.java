package re.yuugu.hzx.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import re.yuugu.hzx.domain.award.model.aggregate.CreateUserAwardRecordAggregate;
import re.yuugu.hzx.domain.award.model.entity.TaskEntity;
import re.yuugu.hzx.domain.award.model.entity.UserAwardRecordEntity;
import re.yuugu.hzx.domain.award.repository.IAwardRepository;
import re.yuugu.hzx.infrastructure.event.EventPublisher;
import re.yuugu.hzx.infrastructure.persistent.dao.ITaskDao;
import re.yuugu.hzx.infrastructure.persistent.dao.IUserAwardRecordDao;
import re.yuugu.hzx.infrastructure.persistent.dao.IUserGachaOrderDao;
import re.yuugu.hzx.infrastructure.persistent.po.Task;
import re.yuugu.hzx.infrastructure.persistent.po.UserAwardRecord;
import re.yuugu.hzx.infrastructure.persistent.po.UserGachaOrder;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description AwardRepository
 * @ create 2026/1/15 19:44
 */
@Repository
@Slf4j
public class AwardRepository implements IAwardRepository {
    @Resource
    private IUserAwardRecordDao userAwardRecordDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IUserGachaOrderDao userGachaOrderDao;

    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private IDBRouterStrategy dbRouter;

    @Override
    public void doSaveUserAwardRecordAggregate(CreateUserAwardRecordAggregate createUserAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = createUserAwardRecordAggregate.getUserAwardRecord();
        TaskEntity taskEntity = createUserAwardRecordAggregate.getTask();
        UserAwardRecord userAwardRecord = UserAwardRecord.builder()
                .userId(userAwardRecordEntity.getUserId())
                .activityId(userAwardRecordEntity.getActivityId())
                .strategyId(userAwardRecordEntity.getStrategyId())
                .awardId(userAwardRecordEntity.getAwardId())
                .orderId(userAwardRecordEntity.getOrderId())
                .awardTitle(userAwardRecordEntity.getAwardTitle())
                .awardTime(userAwardRecordEntity.getAwardTime())
                .awardState(userAwardRecordEntity.getAwardState().getCode())
                .build();
        Task task = Task.builder()
                .userId(taskEntity.getUserId())
                .topic(taskEntity.getTopic())
                .message(JSON.toJSONString(taskEntity.getMessage()))
                .messageId(taskEntity.getMessageId())
                .state(taskEntity.getState().getCode())
                .build();
        UserGachaOrder userGachaOrderReq = new UserGachaOrder();
        userGachaOrderReq.setUserId(userAwardRecordEntity.getUserId());
        userGachaOrderReq.setOrderId(userAwardRecordEntity.getOrderId());
        try {
            dbRouter.doRouter(userAwardRecordEntity.getUserId());
            transactionTemplate.execute(status -> {
                try {
                    userAwardRecordDao.insert(userAwardRecord);
                    taskDao.insert(task);
                    //更新订单状态
                    int count = userGachaOrderDao.updateOrderStateToUsed(userGachaOrderReq);
                    if (count != 1) {
                        status.setRollbackOnly();
                        throw new AppException(ResponseCode.ACTIVITY_USED_GACHA_ORDER_ERR.getCode(), ResponseCode.ACTIVITY_USED_GACHA_ORDER_ERR.getInfo());
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入中奖记录，唯一索引冲突", e);
                    throw new AppException(ResponseCode.DUPLICATE_KEY_EXCEPTION.getCode(), ResponseCode.DUPLICATE_KEY_EXCEPTION.getInfo());
                }
            });

        } finally {
            dbRouter.clear();
        }
        // 发送 mq 消息
        try {
            eventPublisher.publish(task.getTopic(), taskEntity.getMessage());
            taskDao.updateStateToCompleted(task);
        } catch (Exception e) {
            log.error("写入中奖记录，发送mq消息失败");
            taskDao.updateStateToFail(task);
        }
    }
}
