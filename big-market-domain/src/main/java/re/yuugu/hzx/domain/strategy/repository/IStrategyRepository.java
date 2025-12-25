package re.yuugu.hzx.domain.strategy.repository;

import re.yuugu.hzx.domain.strategy.model.entity.StrategyAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyEntity;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyRuleEntity;
import re.yuugu.hzx.domain.strategy.po.AliasTable;

import java.util.List;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);
    void storeStrategyAwardRateSearchTable(String key ,int[] awardIds,double[] probe,int[] alias);
    AliasTable getStrategyAwardRateSearchTable(String key);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRuleEntity(Long strategyId, String ruleWeight);

    /**
     * 查询strategy_rule表，获得rule_value的值
     */
    String queryStrategyRuleValue(Long strategyId, String ruleModel, Integer awardId);
}
