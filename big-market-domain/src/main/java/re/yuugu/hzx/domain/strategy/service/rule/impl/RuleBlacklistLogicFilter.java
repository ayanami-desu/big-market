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
import re.yuugu.hzx.types.common.Constants;

import javax.annotation.Resource;

@Slf4j
@Component
@LogicStrategy(logicMode=DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBlacklistLogicFilter implements ILogicFilter<RuleActionEntity.BeforeGachaEntity> {

    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public RuleActionEntity<RuleActionEntity.BeforeGachaEntity> filter(RuleDetailEntity ruleDetailEntity) {
        log.info("尝试匹配黑名单规则: userId:{},strategyId:{},ruleModel:{}",ruleDetailEntity.getUserId(),ruleDetailEntity.getStrategyId(),ruleDetailEntity.getRuleModel());
        String userId = ruleDetailEntity.getUserId();
        String ruleValue = strategyRepository.queryStrategyRuleValue(ruleDetailEntity.getStrategyId(),ruleDetailEntity.getRuleModel(),ruleDetailEntity.getAwardId());
        String[] splitValue = ruleValue.split(Constants.COLON);
        Integer blacklistAwardId = Integer.valueOf(splitValue[0]);
        String[] blacklist = splitValue[1].split(Constants.COMMA);
        for(String s: blacklist){
            if (userId.equals(s)){
                log.info("匹配到黑名单规则");
                return RuleActionEntity.<RuleActionEntity.BeforeGachaEntity>builder()
                        .code(RuleActionVO.TAKE_OVER.getCode())
                        .info(RuleActionVO.TAKE_OVER.getInfo())
                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                        .data(RuleActionEntity.BeforeGachaEntity.builder()
                                .strategyId(ruleDetailEntity.getStrategyId())
                                // 设置黑名单奖品
                                .awardId(blacklistAwardId)
                                .build())
                        .build();
            }
        }
        log.info("黑名单规则匹配失败");
        return new RuleActionEntity<>();
    }
}
