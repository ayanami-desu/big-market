package re.yuugu.hzx.domain.strategy.service.rule.tree;

import re.yuugu.hzx.domain.strategy.service.rule.tree.factory.DefaultRuleTreeFactory;

public interface ILogicTreeNode {

    DefaultRuleTreeFactory.RuleTreeAction logic(String userId, Long strategyId, Integer awardId,String ruleValue);
}
