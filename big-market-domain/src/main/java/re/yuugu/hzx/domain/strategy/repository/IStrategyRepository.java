package re.yuugu.hzx.domain.strategy.repository;

import re.yuugu.hzx.domain.strategy.model.entity.StrategyAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyEntity;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyRuleEntity;
import re.yuugu.hzx.domain.strategy.model.vo.RuleTreeVO;
import re.yuugu.hzx.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import re.yuugu.hzx.domain.strategy.po.AliasTable;

import java.util.List;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);
    void storeStrategyAwardRateSearchTable(String key ,int[] awardIds,double[] probe,int[] alias);
    AliasTable getStrategyAwardRateSearchTable(String key);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);
    /**
     * 查询strategy_rule表，获得rule_model为rule_weight的，rule_value值
     */
    StrategyRuleEntity queryStrategyRuleEntity(Long strategyId, String ruleWeight);

    /**
     * 查询strategy_rule表，获得rule_value的值
     */
    String queryStrategyRuleValue(Long strategyId, String ruleModel, Integer awardId);
    /**
     * 查询strategy_award表，获得rule_models的值
     */
    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer gachaAwardId);

    RuleTreeVO queryRuleTreeByRootNode(String rootNode);
}
