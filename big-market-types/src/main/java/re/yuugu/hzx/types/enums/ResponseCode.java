package re.yuugu.hzx.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    DUPLICATE_KEY_EXCEPTION("0003","键值重复"),
    SWITCH_NO_MATCH_OPTION("0004","没有匹配的选项"),
    STRATEGY_RULE_WEIGHT_IS_NULL("ERR_BIZ_001","业务异常，策略中存在rule_weight规则，但无具体值"),
    STRATEGY_NOT_CONFIGURED("ERR_BIZ_002","业务异常，未装配抽奖策略即调用抽奖方法"),
    ACTIVITY_DATE_ERR("ERR_BIZ_003","未到活动时间或已超过活动时间"),
    ACTIVITY_STATE_ERR("ERR_BIZ_004","活动状态异常"),
    ACTIVITY_STOCK_ERR("ERR_BIZ_005","活动 sku 库存异常"),
    ACTIVITY_PARTAKE_STOCK_ERR("ERR_BIZ_005","抽奖账户总库存不足"),
    ACTIVITY_PARTAKE_STOCK_DAY_ERR("ERR_BIZ_006","抽奖账户日库存不足"),
    ACTIVITY_PARTAKE_STOCK_MONTH_ERR("ERR_BIZ_007","抽奖账户月总库存不足"),
    ACTIVITY_USED_GACHA_ORDER_ERR("ERR_BIZ_008","已经使用过的抽奖订单被重新使用"),
    ;
    private String code;
    private String info;

}
