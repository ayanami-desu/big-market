package re.yuugu.hzx.domain.strategy.service.Gacha;

import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.strategy.model.entity.GachaFactorEntity;
import re.yuugu.hzx.domain.strategy.model.entity.RuleActionEntity;
import re.yuugu.hzx.domain.strategy.model.entity.RuleDetailEntity;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionVO;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.domain.strategy.service.AbstractGachaStrategy;
import re.yuugu.hzx.domain.strategy.service.armory.IStrategyArmory;
import re.yuugu.hzx.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import re.yuugu.hzx.domain.strategy.service.rule.filter.ILogicFilter;
import re.yuugu.hzx.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultGachaStrategy extends AbstractGachaStrategy {

    @Resource
    private DefaultLogicFactory logicFactory;

    public DefaultGachaStrategy(IStrategyRepository strategyRepository, IStrategyArmory strategyArmory, DefaultLogicChainFactory defaultLogicChainFactory) {
        super(strategyRepository, strategyArmory,defaultLogicChainFactory);
    }

    @Override
    protected RuleActionEntity<RuleActionEntity.BeforeGachaEntity> doCheckBeforeGacha(GachaFactorEntity gachaFactor, String... logics) {
        return new RuleActionEntity<>();
    }

    @Override
    protected RuleActionEntity<RuleActionEntity.ProcessingGachaEntity> doCheckProcessingGacha(GachaFactorEntity gachaFactor, String... logics) {
        if (logics==null || logics.length==0){
            return new RuleActionEntity<>();
        }
        Map<String, ILogicFilter<RuleActionEntity.ProcessingGachaEntity>> logicFilterGroup = logicFactory.openLogicFilter();
        for(String rule:logics){
            ILogicFilter<RuleActionEntity.ProcessingGachaEntity> logicFilter = logicFilterGroup.get(rule);
            RuleDetailEntity ruleDetailEntity = RuleDetailEntity.builder()
                    .userId(gachaFactor.getUserId())
                    .strategyId(gachaFactor.getStrategyId())
                    .ruleModel(rule)
                    .awardId(gachaFactor.getAwardId())
                    .build();
            RuleActionEntity<RuleActionEntity.ProcessingGachaEntity> ruleActionEntity = logicFilter.filter(ruleDetailEntity);
            if(!RuleActionVO.ALLOW.getCode().equals(ruleActionEntity.getCode())){
                return ruleActionEntity;
            }
        }
        return new RuleActionEntity<>();
    }
}
