package re.yuugu.hzx.domain.acitivity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ author anon
 * @ description ActivityPartakeOrderState
 * @ create 2026/1/11 17:24
 */
@AllArgsConstructor
@Getter
public enum ActivityPartakeOrderState {
    create("create","创建"),
    used("used","已使用"),
    cancel("cancel","已作废")
    ;
    private final String code;
    private final String info;
}
