package re.yuugu.hzx.domain.acitivity.repository;

import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateGachaOrderAggregate;
import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateSkuOrderAggregate;
import re.yuugu.hzx.domain.acitivity.model.entity.*;
import re.yuugu.hzx.domain.acitivity.model.vo.SkuStockKeyVO;

import java.util.Date;
import java.util.List;

/**
 * @ author anon
 * @ description IActivityRepository
 * @ create 2026/1/4 17:38
 */

public interface IActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryActivityById(Long activityId);

    ActivityCountEntity queryActivityCountByActivityCountId(Long activityCountId);

    void saveOrder(CreateSkuOrderAggregate createSkuOrderAggregate);

    boolean subtractActivitySkuStock(Long sku, Date endDateTime);

    void sendConsumeSkuStock(SkuStockKeyVO skuStockKeyVO);

    void cacheActivitySkuStockCount(Long sku, Integer stockCountSurplus);
    SkuStockKeyVO takeQueueValue();

    void clearQueueValue();

    /**
     * 延迟队列+趋势更新
     */
    void updateSkuStock(Long sku);

    /**
     * redis中库存已经为0，清空数据库中库存
     */
    void clearSkuStock(Long sku);

    void doSaveCreateGachaOrderAggregate(CreateGachaOrderAggregate createGachaOrderAggregate);

    ActivityAccountEntity queryActivityAccountById(String userId, Long activityId);

    ActivityAccountDayEntity queryActivityAccountDayById(String userId, Long activityId, String day);

    ActivityAccountMonthEntity queryActivityAccountMonthById(String userId, Long activityId, String month);

    List<UserGachaOrderEntity> queryNoUsedGachaOrder(String userId, Long activityId);
}
