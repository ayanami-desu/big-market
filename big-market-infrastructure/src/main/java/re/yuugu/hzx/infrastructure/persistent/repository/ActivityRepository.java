package re.yuugu.hzx.infrastructure.persistent.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityCountEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivitySkuEntity;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;
import re.yuugu.hzx.infrastructure.persistent.dao.IGachaActivityCountDao;
import re.yuugu.hzx.infrastructure.persistent.dao.IGachaActivityDao;
import re.yuugu.hzx.infrastructure.persistent.dao.IGachaActivitySkuDao;
import re.yuugu.hzx.infrastructure.persistent.po.GachaActivity;
import re.yuugu.hzx.infrastructure.persistent.po.GachaActivityCount;
import re.yuugu.hzx.infrastructure.persistent.po.GachaActivitySku;
import re.yuugu.hzx.infrastructure.persistent.redis.IRedisService;
import re.yuugu.hzx.types.common.Constants;

import javax.annotation.Resource;

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
    private IRedisService redisService;

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
}
