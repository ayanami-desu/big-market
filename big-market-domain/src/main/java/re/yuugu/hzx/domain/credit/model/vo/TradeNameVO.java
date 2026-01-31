package re.yuugu.hzx.domain.credit.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ author anon
 * @ description TradeNameVO
 * @ create 2026/1/28 14:45
 */
@Getter
@AllArgsConstructor
public enum TradeNameVO {
    REBATE_CREDIT("rebate_credit","增加"),
    GACHA_AWARD_CREDIT("gacha_award_credit","增加"),
    CONVERT_TO_SKU("convert_to_sku","扣减"),
    ;
    private final String name;
    private final String info;
}
