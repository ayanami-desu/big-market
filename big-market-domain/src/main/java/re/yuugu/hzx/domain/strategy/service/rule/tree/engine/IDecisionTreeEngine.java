package re.yuugu.hzx.domain.strategy.service.rule.tree.engine;

import re.yuugu.hzx.domain.strategy.service.rule.tree.factory.DefaultRuleTreeFactory;

/**
 * @ author anon
 * @ description IDecisionTreeEngine
 * @ create 2025/12/28 17:02
 */
public interface IDecisionTreeEngine {
    DefaultRuleTreeFactory.TreeAwardVO process(Long strategyId, String userId, Integer awardId);
}
