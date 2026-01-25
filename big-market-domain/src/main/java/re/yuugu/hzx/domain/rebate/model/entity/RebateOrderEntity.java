package re.yuugu.hzx.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.rebate.model.vo.BehaviorTypeVO;
import re.yuugu.hzx.domain.rebate.model.vo.RebateTypeVO;

/**
 * @ author anon
 * @ description RebateOrderEntity
 * @ create 2026/1/24 17:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RebateOrderEntity {
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
    private BehaviorTypeVO behaviorType;

    /**
     * 返利描述
     */
    private String rebateDesc;

    /**
     * 返利类型（sku 活动库存充值商品、point 用户活动积分）
     */
    private RebateTypeVO rebateType;

    /**
     * 返利配置【sku值，积分值】
     */
    private String rebateConfig;

    /**
     * 业务ID - 拼接的唯一值
     */
    private String bizId;
}
