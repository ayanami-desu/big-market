package re.yuugu.hzx.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.rebate.model.vo.BehaviorTypeVO;
import re.yuugu.hzx.domain.rebate.model.vo.RebateTypeVO;

/**
 * @ author anon
 * @ description RebateBehaviorEntity
 * @ create 2026/1/24 15:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RebateBehaviorEntity {
    /**
     * 用户 id
     */
    private String userId;
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
     * 业务外部唯一 id
     */
    private String outBusinessNo;
}
