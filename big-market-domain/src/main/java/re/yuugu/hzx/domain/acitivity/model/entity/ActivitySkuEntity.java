package re.yuugu.hzx.domain.acitivity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description ActivitySkuEntity
 * @ create 2026/1/4 19:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivitySkuEntity {
    /**
     * 商品 sku
     */
    private Long sku;
    /**
     * 活动 ID
     */
    private Long activityId;
    /**
     * 活动可参与次数 ID
     */
    private Long activityCountId;
    /**
     * 库存总量
     */
    private Integer stockCount;
    /**
     * 剩余库存
     */
    private Integer stockCountSurplus;
}
