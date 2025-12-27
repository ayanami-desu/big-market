package re.yuugu.hzx.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RuleActionVO {
    ALLOW("0000","接管"),
    TAKE_OVER("0001","放行"),
    ;
    private final String code;
    private final String info;
}
