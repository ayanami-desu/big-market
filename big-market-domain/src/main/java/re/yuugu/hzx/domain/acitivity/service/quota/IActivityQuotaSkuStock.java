package re.yuugu.hzx.domain.acitivity.service.quota;

import re.yuugu.hzx.domain.acitivity.model.vo.SkuStockKeyVO;

/**
 * @ author anon
 * @ description IGachaStock
 * @ create 2026/1/9 16:19
 */
public interface IActivityQuotaSkuStock {
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
