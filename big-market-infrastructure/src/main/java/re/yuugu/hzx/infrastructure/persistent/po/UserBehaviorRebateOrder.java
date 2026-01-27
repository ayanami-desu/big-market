package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ author anon
 * @ description UserBehaviorRebateOrder
 * @ create 2026/1/24 15:43
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBehaviorRebateOrder{

    /** 自增 ID */
    private Integer id;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 订单 ID
     */
    private String orderId;

    /**
     * 行为类型（sign 签到、openai_pay 支付）
     */
    private String behaviorType;

    /**
     * 返利描述
     */
    private String rebateDesc;

    /**
     * 返利类型（sku 活动库存充值商品、point 用户活动积分）
     */
    private String rebateType;

    /**
     * 返利配置【sku值，积分值】
     */
    private String rebateConfig;

    /**
     * 业务ID - 拼接的唯一值
     */
    private String bizId;

    /**
     * 外部业务 id
     */
    private String outBusinessNo;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}

