package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.Data;

/**
 * @ author anon
 * @ description RuleTree
 * @ create 2025/12/28 20:45
 */
@Data
public class RuleTree {
    private Long id;
    /** 规则树 ID */
    private Integer treeId;
    /** 规则树名称 */
    private String treeName;
    /** 规则树描述 */
    private String treeDesc;
    /** 规则根节点 */
    private String rootNode;
}
