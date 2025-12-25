package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.StrategyRule;

@Mapper
public interface IStrategyRuleDao {

    StrategyRule queryStrategyRule(StrategyRule  strategyRuleReq);

    String queryStrategyRuleValue(StrategyRule  strategyRuleReq);
}
