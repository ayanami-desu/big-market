package re.yuugu.hzx.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RuleActionVO {
    ALLOW("0000","放行"),
    TAKE_OVER("0001","接管"),
    NULL("0002","指向空节点")
    ;
    private final String code;
    private final String info;

    public static RuleActionVO getByName(String name) {
        switch (name) {
            case "TAKE_OVER":
                return RuleActionVO.TAKE_OVER;
            case "ALLOW":
                return RuleActionVO.ALLOW;
            default:
                throw new RuntimeException("未知的规则类型");
        }
    }
}
