package re.yuugu.hzx.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import re.yuugu.hzx.domain.rebate.model.aggregate.CreateRebateOrderAggregate;
import re.yuugu.hzx.domain.rebate.model.entity.RebateOrderEntity;
import re.yuugu.hzx.domain.rebate.model.entity.TaskEntity;
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
        TaskEntity taskEntity = createRebateOrderAggregate.getTaskEntity();

        UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
        userBehaviorRebateOrder.setUserId(rebateOrderEntity.getUserId());
        userBehaviorRebateOrder.setOrderId(rebateOrderEntity.getOrderId());
        userBehaviorRebateOrder.setBehaviorType(rebateOrderEntity.getBehaviorType().getCode());
        userBehaviorRebateOrder.setRebateDesc(rebateOrderEntity.getRebateDesc());
        userBehaviorRebateOrder.setRebateType(rebateOrderEntity.getRebateType().getCode());
        userBehaviorRebateOrder.setRebateConfig(rebateOrderEntity.getRebateConfig());
        userBehaviorRebateOrder.setBizId(rebateOrderEntity.getBizId());

        Task task = Task.builder()
                .userId(taskEntity.getUserId())
                .topic(taskEntity.getTopic())
                .message(JSON.toJSONString(taskEntity.getMessage()))
                .messageId(taskEntity.getMessageId())
                .state(taskEntity.getState().getCode())
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
            eventPublisher.publish(task.getTopic(), taskEntity.getMessage());
            taskDao.updateStateToCompleted(task);
        } catch (Exception e) {
            log.error("写入行为返利订单，发送mq消息失败");
            taskDao.updateStateToFail(task);
        }
    }
}
