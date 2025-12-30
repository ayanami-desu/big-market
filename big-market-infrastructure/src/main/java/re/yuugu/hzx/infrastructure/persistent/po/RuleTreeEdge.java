package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.Data;

/**
 * @ author anon
 * @ description RuleTreeEdge
 * @ create 2025/12/28 20:45
 */
@Data
public class RuleTreeEdge {
    private Long id;
    private Integer edgeId;
    /** 规则树 ID */
    private Integer treeId;
    /** 规则Key节点 From */
    private String ruleNodeFrom;
    /** 规则Key节点 To */
    private String ruleNodeTo;
    /** 限定类型;1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围] */
    private String ruleTreeEdgeCompOp;
    /** 限定值; 标识这条边是第几条，对于二叉树即左边和右边*/
    private String ruleAction;
}
