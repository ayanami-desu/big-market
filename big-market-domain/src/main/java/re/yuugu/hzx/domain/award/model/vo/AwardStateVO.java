package re.yuugu.hzx.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ author anon
 * @ description AwardStateVO
 * @ create 2026/1/15 22:49
 */
@Getter
@AllArgsConstructor
public enum AwardStateVO {
    create("create","奖品待发放"),
    completed("completed","奖品已发放"),
    ;

    private final String code;
    private final String info;
}
