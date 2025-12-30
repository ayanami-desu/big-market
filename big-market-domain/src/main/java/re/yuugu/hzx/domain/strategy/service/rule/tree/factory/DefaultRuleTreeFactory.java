package re.yuugu.hzx.domain.strategy.service.rule.tree.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionVO;
import re.yuugu.hzx.domain.strategy.model.vo.RuleTreeVO;
import re.yuugu.hzx.domain.strategy.service.rule.tree.ILogicTreeNode;
import re.yuugu.hzx.domain.strategy.service.rule.tree.engine.DecisionTreeEngine;

import java.util.Map;

/**
 * @ author anon
 * @ description DefaultRuleTreeFactory
 * @ create 2025/12/28 16:56
 */

@Service
public class DefaultRuleTreeFactory {
    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    public DefaultRuleTreeFactory(Map<String, ILogicTreeNode> logicTreeNodeGroup) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
    }

    public DecisionTreeEngine openRuleTreeEngine(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngine(logicTreeNodeGroup, ruleTreeVO);
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TreeAwardVO {
        private String awardConfig;
        private Integer awardId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RuleTreeAction{
        private RuleActionVO ruleActionVO=RuleActionVO.ALLOW;
        private TreeAwardVO treeAwardVO;
    }

}
