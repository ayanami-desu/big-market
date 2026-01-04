package re.yuugu.hzx.domain.acitivity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ author anon
 * @ description ActivityStateVO
 * @ create 2026/1/4 18:28
 */
@Getter
@AllArgsConstructor
public enum ActivityStateVO {
    create("create","创建"),
    ;

    private final String code;
    private final String info;
}
