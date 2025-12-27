package re.yuugu.hzx.domain.strategy.service.Gacha;

import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.strategy.model.entity.GachaFactorEntity;
import re.yuugu.hzx.domain.strategy.model.entity.RuleActionEntity;
import re.yuugu.hzx.domain.strategy.model.entity.RuleDetailEntity;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionVO;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.domain.strategy.service.armory.IStrategyArmory;
import re.yuugu.hzx.domain.strategy.service.rule.ILogicFilter;
import re.yuugu.hzx.domain.strategy.service.rule.factory.DefaultLogicFactory;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultGachaStrategy extends AbstractGachaStrategy{

    @Resource
    private DefaultLogicFactory logicFactory;

    public DefaultGachaStrategy(IStrategyRepository strategyRepository, IStrategyArmory strategyArmory) {
        super(strategyRepository, strategyArmory);
    }

    @Override
    protected RuleActionEntity<RuleActionEntity.BeforeGachaEntity> doCheckBeforeGacha(GachaFactorEntity gachaFactor, String... logics) {
        Map<String, ILogicFilter<RuleActionEntity.BeforeGachaEntity>> logicFilterGroup = logicFactory.openLogicFilter();

        // 1. 先处理黑名单规则
        String ruleBlacklist = Arrays.stream(logics)
                .filter(l->DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(l))
                .findFirst()
                .orElse(null);
        if (ruleBlacklist!=null){
            ILogicFilter<RuleActionEntity.BeforeGachaEntity> logicFilter = logicFilterGroup.get(ruleBlacklist);
            RuleDetailEntity ruleDetailEntity = RuleDetailEntity.builder()
                    .userId(gachaFactor.getUserId())
                    .strategyId(gachaFactor.getStrategyId())
                    .ruleModel(ruleBlacklist)
                    .build();
            RuleActionEntity<RuleActionEntity.BeforeGachaEntity> ruleActionEntity = logicFilter.filter(ruleDetailEntity);
            if(!RuleActionVO.ALLOW.getCode().equals(ruleActionEntity.getCode())){
                return ruleActionEntity;
            }
        }
        // 2. 处理其他规则
        List<String> ruleList = Arrays.stream(logics)
                .filter(l->!DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(l))
                .collect(Collectors.toList());
        for(String rule:ruleList){
            ILogicFilter<RuleActionEntity.BeforeGachaEntity> logicFilter = logicFilterGroup.get(rule);
            RuleDetailEntity ruleDetailEntity = RuleDetailEntity.builder()
                    .userId(gachaFactor.getUserId())
                    .strategyId(gachaFactor.getStrategyId())
                    .ruleModel(rule)
                    .build();
            RuleActionEntity<RuleActionEntity.BeforeGachaEntity> ruleActionEntity = logicFilter.filter(ruleDetailEntity);
            if(!RuleActionVO.ALLOW.getCode().equals(ruleActionEntity.getCode())){
                return ruleActionEntity;
            }
        }
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
