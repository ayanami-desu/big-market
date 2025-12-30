package re.yuugu.hzx.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleTreeEdgeVO {

    /** 规则树ID */
    private Integer treeId;
    /** 规则Key节点 From */
    private String ruleNodeFrom;
    /** 规则Key节点 To */
    private String ruleNodeTo;
    /** 限定类型;1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围] */
    private RuleTreeEdgeCompOpVO ruleTreeEdgeCompOp;
    /** 限定值; 标识这条边是第几条，对于二叉树即左边和右边*/
    private RuleActionVO ruleAction;
}
