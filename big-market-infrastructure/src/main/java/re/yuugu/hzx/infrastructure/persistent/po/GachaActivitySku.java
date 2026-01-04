package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @ author anon
 * @ description GachaActivitySku Stock Keeping Unit（库存量单位）
 * 我们把抽奖次数看作一种商品，一个活动可获得抽奖次数的途径是多样的，因此sku也是多种的。
 * @ create 2026/1/4 17:30
 */
@Data
public class GachaActivitySku {
    private Long id;
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
     * 通过这个sku可以获得的抽奖次数
     */
    private Long activityCountId;
    /**
     * sku 库存总量
     */
    private Integer stockCount;
    /**
     * sku 剩余库存
     */
    private Integer stockCountSurplus;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
