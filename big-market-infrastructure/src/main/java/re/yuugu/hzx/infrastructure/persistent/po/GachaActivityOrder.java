package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @ author anon
 * @ description GachaActivityOrder
 * @ create 2026/1/2 17:48
 */
@Data
public class GachaActivityOrder {
    /**
     * 自增 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 商品 sku
     */
    private Long sku;

    /**
     * 活动 ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 抽奖策略 ID
     */
    private Long strategyId;

    /**
     * 订单 ID
     */
    private String orderId;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 总次数变化量
     */
    private Integer totalCount;

    /**
     * 月次数变化量
     */
    private Integer monthCount;

    /**
     * 日次数变化量
     */
    private Integer dayCount;

    /**
     * 订单状态
     */
    private String state;

    /**
     * 外部透传字段，保证订单唯一性
     */
    private String bizId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
