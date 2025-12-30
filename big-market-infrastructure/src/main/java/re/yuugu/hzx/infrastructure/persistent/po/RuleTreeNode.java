package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.Data;

/**
 * @ author anon
 * @ description RuleTreeNode
 * @ create 2025/12/28 20:45
 */
@Data
public class RuleTreeNode {
    private Long id;
    private Integer treeId;
    /** 节点 ID */
    private Integer nodeId;
    /** 规则 Key */
    private String ruleKey;
    /** 规则描述 */
    private String ruleDesc;
    /** 规则值 */
    private String ruleValue;
}
