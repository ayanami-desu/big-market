package re.yuugu.hzx.domain.acitivity.repository;

import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateOrderAggregate;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityCountEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivitySkuEntity;
import re.yuugu.hzx.domain.acitivity.model.vo.SkuStockKeyVO;

import java.util.Date;

/**
 * @ author anon
 * @ description IActivityRepository
 * @ create 2026/1/4 17:38
 */

public interface IActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryActivityById(Long activityId);

    ActivityCountEntity queryActivityCountByActivityCountId(Long activityCountId);

    void saveOrder(CreateOrderAggregate createOrderAggregate);

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
}
