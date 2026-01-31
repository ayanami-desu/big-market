package re.yuugu.hzx.domain.credit.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ author anon
 * @ description TradeTypeVO
 * @ create 2026/1/28 14:45
 */
@Getter
@AllArgsConstructor
public enum TradeTypeVO {
    FORWARD("forward"),
    REVERSE("reverse"),
    ;
    private final String code;
}
