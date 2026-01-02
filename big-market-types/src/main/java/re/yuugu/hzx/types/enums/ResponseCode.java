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
    STRATEGY_RULE_WEIGHT_IS_NULL("ERR_BIZ_001","业务异常，策略中存在rule_weight规则，但无具体值"),
    STRATEGY_NOT_CONFIGURED("ERR_BIZ_002","业务异常，未装配抽奖策略即调用抽奖方法")
    ;

    private String code;
    private String info;

}
