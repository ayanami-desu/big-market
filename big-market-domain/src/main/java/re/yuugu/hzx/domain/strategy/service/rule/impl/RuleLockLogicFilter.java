package re.yuugu.hzx.domain.strategy.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.strategy.model.entity.RuleActionEntity;
import re.yuugu.hzx.domain.strategy.model.entity.RuleDetailEntity;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionVO;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.domain.strategy.service.annotation.LogicStrategy;
import re.yuugu.hzx.domain.strategy.service.rule.ILogicFilter;
import re.yuugu.hzx.domain.strategy.service.rule.factory.DefaultLogicFactory;

import javax.annotation.Resource;


@Slf4j
@Component
@LogicStrategy(logicMode= DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.ProcessingGachaEntity> {
    @Resource
    private IStrategyRepository strategyRepository;

    public Long userGachaCount = 0L;
    @Override
    public RuleActionEntity<RuleActionEntity.ProcessingGachaEntity> filter(RuleDetailEntity ruleDetailEntity) {
        log.info("规则过滤，次数锁");
        log.info("{},{},{}",ruleDetailEntity.getStrategyId(),ruleDetailEntity.getRuleModel(),ruleDetailEntity.getAwardId());
        String ruleLockValue = strategyRepository.queryStrategyRuleValue(ruleDetailEntity.getStrategyId(),ruleDetailEntity.getRuleModel(),ruleDetailEntity.getAwardId());

        long ruleLockCount = Long.parseLong(ruleLockValue);
        log.info("ruleLockCount:{}",ruleLockCount);
        if(ruleLockCount>userGachaCount){
            return RuleActionEntity.<RuleActionEntity.ProcessingGachaEntity>builder()
                    .code(RuleActionVO.TAKE_OVER.getCode())
                    .info(RuleActionVO.TAKE_OVER.getInfo())
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_LOCK.getCode())
                    .data(RuleActionEntity.ProcessingGachaEntity.builder()
                            .strategyId(ruleDetailEntity.getStrategyId())
                            .build())
                    .build();
        }
        return new RuleActionEntity<>();
    }
}
