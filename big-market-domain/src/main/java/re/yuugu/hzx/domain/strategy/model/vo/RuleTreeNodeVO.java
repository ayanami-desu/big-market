package re.yuugu.hzx.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeVO {

    /** 节点 ID */
    private Integer nodeId;
    /** 规则 Key */
    private String ruleKey;
    /** 规则描述 */
    private String ruleDesc;
    /** 规则值 */
    private String ruleValue;
    /** 边列表 */
    private List<RuleTreeEdgeVO> treeEdgeVOList;

}

