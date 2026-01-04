package re.yuugu.hzx.domain.acitivity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ author anon
 * @ description GachaActivityOrderEntity
 * @ create 2026/1/4 18:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityOrderEntity {
    /**
     * 自增 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private String userId;

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
    private Integer mouthCount;

    /**
     * 日次数变化量
     */
    private Integer dayCount;

    /**
     * 订单状态
     */
    private String state;
}
