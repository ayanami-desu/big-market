package re.yuugu.hzx.domain.rebate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ author anon
 * @ description AwardStateVO
 * @ create 2026/1/15 22:49
 */
@Getter
@AllArgsConstructor
public enum BehaviorTypeVO {
    OPENAI_PAY("OPENAI_PAY","openai 支付"),
    DAILY_SIGN("DAILY_SIGN","日常签到"),
    ;

    private final String code;
    private final String info;

}
