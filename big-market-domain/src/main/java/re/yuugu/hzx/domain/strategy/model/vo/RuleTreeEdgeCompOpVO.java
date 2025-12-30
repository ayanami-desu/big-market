package re.yuugu.hzx.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ author anon
 * @ description RuleTreeLimitTypeVO,不知道干嘛的类
 * @ create 2025/12/28 16:42
 */
@Getter
@AllArgsConstructor
public enum RuleTreeEdgeCompOpVO {
    EQUAL(1, "等于"),
    GT(2, "大于"),
    LT(3, "小于"),
    GE(4, "大于&等于"),
    LE(5, "小于&等于"),
    ENUM(6, "枚举"),
    ;

    private final Integer code;
    private final String info;

    public static RuleTreeEdgeCompOpVO getByName(String name) {
        switch (name) {
            case "eq":
                return RuleTreeEdgeCompOpVO.EQUAL;
            // 以下规则暂时不需要实现
            case "gt":
            case "lt":
            case "ge":
            case "le":
            default:
                throw new RuntimeException("未知的比较类型");
        }
    }

}
