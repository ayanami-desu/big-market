package re.yuugu.hzx.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import re.yuugu.hzx.domain.acitivity.event.ActivitySkuStockZeroEvent;
import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateGachaOrderAggregate;
import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateSkuOrderAggregate;
import re.yuugu.hzx.domain.acitivity.model.entity.*;
import re.yuugu.hzx.domain.acitivity.model.vo.ActivityPartakeOrderState;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private IGachaActivityAccountDayDao activityAccountDayDao;
    @Resource
    private IGachaActivityAccountMonthDao activityAccountMonthDao;
    @Resource
    private IUserGachaOrderDao userGachaOrderDao;

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
    public void saveOrder(CreateSkuOrderAggregate createSkuOrderAggregate) {
        try {
            ActivityOrderEntity activityOrderEntity = createSkuOrderAggregate.getActivityOrderEntity();
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

            //只要这里设置了路由，后面的方法都会自动根据路由结果进行分库
            //至于分表则需要在 dao 中设置注解
            dbRouter.doRouter(activityOrderEntity.getUserId());
            transactionTemplate.execute(status -> {
                try {
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
        } else if (surplus < 0) {
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

    @Override
    public void doSaveCreateGachaOrderAggregate(CreateGachaOrderAggregate createGachaOrderAggregate) {
        //需要执行事务
        try {
            ActivityAccountDayEntity activityAccountDayEntity = createGachaOrderAggregate.getActivityAccountDayEntity();
            ActivityAccountMonthEntity activityAccountMonthEntity = createGachaOrderAggregate.getActivityAccountMonthEntity();
            UserGachaOrderEntity userGachaOrderEntity = createGachaOrderAggregate.getUserGachaOrderEntity();

            dbRouter.doRouter(createGachaOrderAggregate.getUserId());
            transactionTemplate.execute(status -> {
                try {
                    int totalCount = activityAccountDao.updateAccountQuotaSubtraction(GachaActivityAccount.builder().
                            userId(createGachaOrderAggregate.getUserId())
                            .activityId(createGachaOrderAggregate.getActivityId())
                            .build());
                    if (totalCount != 1) {
                        log.error("更新总账户出错：账户不存在");
                        status.setRollbackOnly();
                        throw new AppException(ResponseCode.ACTIVITY_PARTAKE_STOCK_ERR.getCode(), ResponseCode.ACTIVITY_PARTAKE_STOCK_ERR.getInfo());
                    }
                    //处理日账户
                    if (createGachaOrderAggregate.isDayAccountExist()) {
                        int count = activityAccountDayDao.updateAccountDayQuota(GachaActivityAccountDay.builder()
                                .userId(activityAccountDayEntity.getUserId())
                                .activityId(activityAccountDayEntity.getActivityId())
                                .day(activityAccountDayEntity.getDay())
                                .build());
                        if (count != 1) {
                            log.error("更新日账户库存出错");
                            status.setRollbackOnly();
                            throw new AppException(ResponseCode.ACTIVITY_PARTAKE_STOCK_DAY_ERR.getCode(), ResponseCode.ACTIVITY_PARTAKE_STOCK_DAY_ERR.getInfo());
                        }
                    } else {
                        activityAccountDayDao.insert(GachaActivityAccountDay.builder()
                                .userId(activityAccountDayEntity.getUserId())
                                .activityId(activityAccountDayEntity.getActivityId())
                                .day(activityAccountDayEntity.getDay())
                                .dayCount(activityAccountDayEntity.getDayCount())
                                // 新建日账户时，剩余量减一
                                .dayCountSurplus(activityAccountDayEntity.getDayCountSurplus() - 1)
                                .build());
                    }
                    //处理月账户
                    if (createGachaOrderAggregate.isMonthAccountExist()) {
                        int count = activityAccountMonthDao.updateAccountMonthQuota(GachaActivityAccountMonth.builder()
                                .userId(activityAccountMonthEntity.getUserId())
                                .activityId(activityAccountMonthEntity.getActivityId())
                                .month(activityAccountMonthEntity.getMonth())
                                .build());
                        if (count != 1) {
                            log.error("更新月账户库存出错");
                            status.setRollbackOnly();
                            throw new AppException(ResponseCode.ACTIVITY_PARTAKE_STOCK_MONTH_ERR.getCode(), ResponseCode.ACTIVITY_PARTAKE_STOCK_MONTH_ERR.getInfo());
                        }
                    } else {
                        activityAccountMonthDao.insert(GachaActivityAccountMonth.builder()
                                .userId(activityAccountMonthEntity.getUserId())
                                .activityId(activityAccountMonthEntity.getActivityId())
                                .month(activityAccountMonthEntity.getMonth())
                                .monthCount(activityAccountMonthEntity.getMonthCount())
                                // 新建月账户时，剩余量减一
                                .monthCountSurplus(activityAccountMonthEntity.getMonthCountSurplus() - 1)
                                .build());
                    }
                    userGachaOrderDao.insert(UserGachaOrder.builder()
                            .userId(userGachaOrderEntity.getUserId())
                            .activityId(userGachaOrderEntity.getActivityId())
                            .activityName(userGachaOrderEntity.getActivityName())
                            .strategyId(userGachaOrderEntity.getStrategyId())
                            .orderId(userGachaOrderEntity.getOrderId())
                            .orderTime(userGachaOrderEntity.getOrderTime())
                            .orderState(userGachaOrderEntity.getOrderState().getCode())
                            .build());
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入抽奖订单时出现唯一索引冲突");
                    throw new AppException(ResponseCode.DUPLICATE_KEY_EXCEPTION.getCode(), ResponseCode.DUPLICATE_KEY_EXCEPTION.getInfo());

                }
            });
        } finally {
            dbRouter.clear();
        }
    }

    @Override
    public ActivityAccountEntity queryActivityAccountById(String userId, Long activityId) {
        GachaActivityAccount req = new GachaActivityAccount();
        req.setUserId(userId);
        req.setActivityId(activityId);
        GachaActivityAccount gachaActivityAccount = activityAccountDao.queryActivityAccountById(req);
        if (gachaActivityAccount == null) {
            return null;
        }
        return ActivityAccountEntity.builder()
                .userId(gachaActivityAccount.getUserId())
                .activityId(gachaActivityAccount.getActivityId())
                .totalCount(gachaActivityAccount.getTotalCount())
                .totalCountSurplus(gachaActivityAccount.getTotalCountSurplus())
                .dayCount(gachaActivityAccount.getDayCount())
                .dayCountSurplus(gachaActivityAccount.getDayCountSurplus())
                .monthCount(gachaActivityAccount.getMonthCount())
                .monthCountSurplus(gachaActivityAccount.getMonthCountSurplus())
                .build();
    }

    @Override
    public ActivityAccountDayEntity queryActivityAccountDayById(String userId, Long activityId, String day) {
        GachaActivityAccountDay req = new GachaActivityAccountDay();
        req.setUserId(userId);
        req.setActivityId(activityId);
        req.setDay(day);
        GachaActivityAccountDay gachaActivityAccountDay = activityAccountDayDao.queryActivityAccountDayById(req);
        if (gachaActivityAccountDay == null) {
            return null;
        }
        return ActivityAccountDayEntity.builder()
                .userId(gachaActivityAccountDay.getUserId())
                .activityId(gachaActivityAccountDay.getActivityId())
                .day(gachaActivityAccountDay.getDay())
                .dayCount(gachaActivityAccountDay.getDayCount())
                .dayCountSurplus(gachaActivityAccountDay.getDayCountSurplus())
                .build();
    }

    @Override
    public ActivityAccountMonthEntity queryActivityAccountMonthById(String userId, Long activityId, String month) {
        GachaActivityAccountMonth req = new GachaActivityAccountMonth();
        req.setUserId(userId);
        req.setActivityId(activityId);
        req.setMonth(month);
        GachaActivityAccountMonth gachaActivityAccountMonth = activityAccountMonthDao.queryActivityAccountMonthById(req);
        if (gachaActivityAccountMonth == null) {
            return null;
        }
        return ActivityAccountMonthEntity.builder()
                .userId(gachaActivityAccountMonth.getUserId())
                .activityId(gachaActivityAccountMonth.getActivityId())
                .month(gachaActivityAccountMonth.getMonth())
                .monthCount(gachaActivityAccountMonth.getMonthCount())
                .monthCountSurplus(gachaActivityAccountMonth.getMonthCountSurplus())
                .build();
    }

    @Override
    public List<UserGachaOrderEntity> queryNoUsedGachaOrder(String userId, Long activityId) {
        UserGachaOrder req = new UserGachaOrder();
        req.setUserId(userId);
        req.setActivityId(activityId);
        List<UserGachaOrder> orders = userGachaOrderDao.queryNoUsedGachaOrder(req);
        if (orders == null || orders.isEmpty()) {
            return null;
        }
        List<UserGachaOrderEntity> userGachaOrderEntities = new ArrayList<>();
        for (UserGachaOrder order : orders) {
            userGachaOrderEntities.add(
                    UserGachaOrderEntity.builder()
                            .userId(order.getUserId())
                            .activityId(order.getActivityId())
                            .activityName(order.getActivityName())
                            .strategyId(order.getStrategyId())
                            .orderId(order.getOrderId())
                            .orderTime(order.getOrderTime())
                            .orderState(ActivityPartakeOrderState.valueOf(order.getOrderState()))
                            .build()
            );
        }
        return userGachaOrderEntities;
    }
}
