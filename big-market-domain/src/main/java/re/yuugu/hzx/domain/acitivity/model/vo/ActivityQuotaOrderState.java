package re.yuugu.hzx.domain.acitivity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ author anon
 * @ description OrderState
 * @ create 2026/1/4 18:30
 */
@Getter
@AllArgsConstructor
public enum ActivityQuotaOrderState {
    completed("completed","已完成"),
    ;
    private final String code;
    private final String info;
}
