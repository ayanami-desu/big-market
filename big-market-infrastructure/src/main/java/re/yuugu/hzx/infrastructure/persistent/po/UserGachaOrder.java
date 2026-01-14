package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ author anon
 * @ description UserGachaOrder
 * @ create 2026/1/11 17:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGachaOrder {


    private String id;

    private String userId;

    private Long activityId;

    private String activityName;

    private Long strategyId;

    private String orderId;

    private Date orderTime;
    /**
     * 订单状态；create-创建、used-已使用、cancel-已作废
     */
    private String orderState;

    private Date createTime;

    private Date updateTime;

}
