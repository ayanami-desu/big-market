package re.yuugu.hzx.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import re.yuugu.hzx.domain.acitivity.event.ActivitySkuStockZeroEvent;
import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateOrderAggregate;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityCountEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityOrderEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivitySkuEntity;
import re.yuugu.hzx.domain.acitivity.model.vo.SkuStockKeyVO;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;
import re.yuugu.hzx.infrastructure.event.EventPublisher;
import re.yuugu.hzx.infrastructure.persistent.dao.*;
import re.yuugu.hzx.infrastructure.persistent.po.*;
import re.yuugu.hzx.infrastructure.persistent.redis.IRedisService;
import re.yuugu.hzx.types.common.Constants;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @ author anon
 * @ description ActivityRepository
 * @ create 2026/1/4 17:38
 */
@Repository
@Slf4j
public class ActivityRepository implements IActivityRepository {
    @Resource
    private IGachaActivitySkuDao activitySkuDao;
    @Resource
    private IGachaActivityCountDao activityCountDao;
    @Resource
    private IGachaActivityDao activityDao;
    @Resource
    private IGachaActivityAccountDao activityAccountDao;
    @Resource
    private IGachaActivityOrderDao activityOrderDao;
    @Resource
    private IRedisService redisService;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private ActivitySkuStockZeroEvent activitySkuStockZeroEvent;

    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        String cacheKey = Constants.RedisKeys.ACTIVITY_SKU_KEY + sku;
        ActivitySkuEntity activitySkuEntity = redisService.getValue(cacheKey);
        if (activitySkuEntity != null) {
            return activitySkuEntity;
        }
        GachaActivitySku gachaActivitySku = activitySkuDao.queryGachaActivitySku(sku);
        activitySkuEntity = ActivitySkuEntity.builder()
                .sku(gachaActivitySku.getSku())
                .activityId(gachaActivitySku.getActivityId())
                .activityCountId(gachaActivitySku.getActivityCountId())
                .stockCount(gachaActivitySku.getStockCount())
                .stockCountSurplus(gachaActivitySku.getStockCountSurplus())
                .build();
        redisService.setValue(cacheKey, activitySkuEntity);
        return activitySkuEntity;
    }

    @Override
    public ActivityEntity queryActivityById(Long activityId) {
        String cacheKey = Constants.RedisKeys.ACTIVITY_ACTIVITY_KEY + activityId;
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (activityEntity != null) {
            return activityEntity;
        }
        GachaActivity activity = activityDao.queryActivityById(activityId);
        activityEntity = ActivityEntity.builder().activityName(activity.getActivityName())
                .activityDesc(activity.getActivityDesc())
                .beginDateTime(activity.getBeginDateTime())
                .endDateTime(activity.getEndDateTime())
                .strategyId(activity.getStrategyId())
                .state(activity.getState())
                .build();
        redisService.setValue(cacheKey, activityEntity);
        return activityEntity;
    }

    @Override
    public ActivityCountEntity queryActivityCountByActivityCountId(Long activityCountId) {
        String cacheKey = Constants.RedisKeys.ACTIVITY_ACTIVITY_COUNT_ID + activityCountId;
        ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
        if (activityCountEntity != null) {
            return activityCountEntity;
        }
        GachaActivityCount gachaActivityCount = activityCountDao.queryGachaActivityCountByActivityCountId(activityCountId);
        activityCountEntity = ActivityCountEntity.builder()
                .activityCountId(gachaActivityCount.getActivityCountId())
                .totalCount(gachaActivityCount.getTotalCount())
                .dayCount(gachaActivityCount.getDayCount())
                .monthCount(gachaActivityCount.getMonthCount())
                .build();
        redisService.setValue(cacheKey, activityCountEntity);
        return activityCountEntity;
    }

    @Override
    public void saveOrder(CreateOrderAggregate createOrderAggregate) {
        try {
            ActivityOrderEntity activityOrderEntity = createOrderAggregate.getActivityOrderEntity();
            GachaActivityOrder gachaActivityOrder = new GachaActivityOrder();

            gachaActivityOrder.setUserId(activityOrderEntity.getUserId());
            gachaActivityOrder.setSku(activityOrderEntity.getSku());
            gachaActivityOrder.setActivityId(activityOrderEntity.getActivityId());
            gachaActivityOrder.setActivityName(activityOrderEntity.getActivityName());
            gachaActivityOrder.setStrategyId(activityOrderEntity.getStrategyId());
            gachaActivityOrder.setOrderId(activityOrderEntity.getOrderId());
            gachaActivityOrder.setOrderTime(activityOrderEntity.getOrderTime());
            gachaActivityOrder.setTotalCount(activityOrderEntity.getTotalCount());
            gachaActivityOrder.setMonthCount(activityOrderEntity.getMonthCount());
            gachaActivityOrder.setDayCount(activityOrderEntity.getDayCount());
            gachaActivityOrder.setState(activityOrderEntity.getState());
            gachaActivityOrder.setBizId(activityOrderEntity.getBizId());

            GachaActivityAccount gachaActivityAccount = new GachaActivityAccount();
            gachaActivityAccount.setUserId(activityOrderEntity.getUserId());
            gachaActivityAccount.setActivityId(activityOrderEntity.getActivityId());
            gachaActivityAccount.setTotalCount(activityOrderEntity.getTotalCount());
            gachaActivityAccount.setTotalCountSurplus(activityOrderEntity.getTotalCount());
            gachaActivityAccount.setDayCount(activityOrderEntity.getDayCount());
            gachaActivityAccount.setDayCountSurplus(activityOrderEntity.getDayCount());
            gachaActivityAccount.setMonthCount(activityOrderEntity.getMonthCount());
            gachaActivityAccount.setMonthCountSurplus(activityOrderEntity.getMonthCount());

            dbRouter.doRouter(activityOrderEntity.getUserId());
            transactionTemplate.execute(status -> {
                try {
                    //TODO 只要这里设置了，后面的方法都会自动进行分库。或许分表不一定？
                    activityOrderDao.insert(gachaActivityOrder);
                    int count = activityAccountDao.updateAccountQuota(gachaActivityAccount);
                    if (count == 0) {
                        activityAccountDao.insert(gachaActivityAccount);
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入订单记录，唯一索引冲突");
                    throw new AppException(ResponseCode.DUPLICATE_KEY_EXCEPTION.getCode(), ResponseCode.DUPLICATE_KEY_EXCEPTION.getInfo());
                }
            });
        } finally {
            dbRouter.clear();
        }
    }

    @Override
    public boolean subtractActivitySkuStock(Long sku, Date endDateTime) {
        String cacheKey = Constants.RedisKeys.ACTIVITY_SKU_SURPLUS_COUNT + sku;
        //加锁；如上锁失败，代表这个库存数已经被扣减过
        long surplus = redisService.decr(cacheKey);
        String lockKey = cacheKey + "_" + surplus;
        // 过期时间，活动结束后一天
        long expireMillis = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        if (surplus == 0) {
            boolean isLocked = redisService.setNX(lockKey, expireMillis);
            if (isLocked) {
                log.info("sku 库存为0");
                // 为什么要发送mq消息，为了最终一致性
                // 直接清空队列，即无需趋势更新了。
                eventPublisher.publish(activitySkuStockZeroEvent.topic(), activitySkuStockZeroEvent.buildEventMessage(sku));
            }
            return isLocked;
        } else if (surplus<0) {
            log.info("sku 库存已为负数");
            redisService.setAtomicLong(cacheKey, 0);
            return false;
        }

        boolean lock = redisService.setNX(lockKey, expireMillis);
        if (!lock) {
            log.info("出现异常：sku 库存数不一致");
        }
        return lock;
    }

    @Override
    public void sendConsumeSkuStock(SkuStockKeyVO skuStockKeyVO) {
        String cacheKey = Constants.RedisKeys.ACTIVITY_SKU_STOCK_QUEUE;
        RBlockingQueue<SkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<SkuStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        // 3s 后再加入队列
        delayedQueue.offer(skuStockKeyVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public void cacheActivitySkuStockCount(Long sku, Integer stockCountSurplus) {
        String cacheKey = Constants.RedisKeys.ACTIVITY_SKU_SURPLUS_COUNT + sku;
        redisService.setValue(cacheKey, stockCountSurplus);
    }

    @Override
    public SkuStockKeyVO takeQueueValue() {
        String cacheKey = Constants.RedisKeys.ACTIVITY_SKU_STOCK_QUEUE;
        RBlockingQueue<SkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        return blockingQueue.poll();
    }

    @Override
    public void clearQueueValue() {
        // 两个队列都要清空
        String cacheKey = Constants.RedisKeys.ACTIVITY_SKU_STOCK_QUEUE;
        RBlockingQueue<SkuStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        blockingQueue.clear();
        RDelayedQueue<SkuStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.clear();
    }

    @Override
    public void updateSkuStock(Long sku) {
        activitySkuDao.updateSkuStock(sku);
    }

    @Override
    public void clearSkuStock(Long sku) {
        activitySkuDao.clearSkuStock(sku);
    }
}
