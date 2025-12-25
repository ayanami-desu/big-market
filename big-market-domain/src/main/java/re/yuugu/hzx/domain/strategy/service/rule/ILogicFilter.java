package re.yuugu.hzx.domain.strategy.service.rule;

import re.yuugu.hzx.domain.strategy.model.entity.RuleActionEntity;
import re.yuugu.hzx.domain.strategy.model.entity.RuleDetailEntity;

public interface ILogicFilter<T extends RuleActionEntity.GachaEntity> {

    RuleActionEntity<T> filter(RuleDetailEntity ruleDetailEntity);
}
