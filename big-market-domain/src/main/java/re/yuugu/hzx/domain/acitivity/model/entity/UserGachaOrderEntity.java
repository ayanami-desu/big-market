package re.yuugu.hzx.domain.acitivity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.acitivity.model.vo.ActivityPartakeOrderState;

import java.util.Date;

/**
 * @ author anon
 * @ description UserGachaOrderEntity
 * @ create 2026/1/11 17:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGachaOrderEntity {
    private String userId;

    private Long activityId;

    private String activityName;

    private Long strategyId;

    private String orderId;

    private Date orderTime;
    /**
     * 订单状态；create-创建、used-已使用、cancel-已作废
     */
    private ActivityPartakeOrderState orderState;
}
