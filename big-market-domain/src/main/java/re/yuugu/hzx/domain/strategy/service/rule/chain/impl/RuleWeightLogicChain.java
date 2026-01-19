package re.yuugu.hzx.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.domain.strategy.service.armory.IStrategyArmory;
import re.yuugu.hzx.domain.strategy.service.dispatch.IGachaStrategyAwardDispatch;
import re.yuugu.hzx.domain.strategy.service.rule.chain.AbstractLoginChain;
import re.yuugu.hzx.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import re.yuugu.hzx.types.common.Constants;

import javax.annotation.Resource;
import java.util.TreeMap;

@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLoginChain {
    @Resource
    private IStrategyRepository strategyRepository;

    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IGachaStrategyAwardDispatch strategyAwardDispatch;

    public Long userPoint=0L;

    @Override
    public DefaultLogicChainFactory.ChainAwardVO logic(Long strategyId, String userId) {
        log.info("责任链-权重范围: strategyId:{},userId:{}",strategyId,userId);
        String ruleValue = strategyRepository.queryStrategyRuleValue(strategyId,ruleModel(),null);
        TreeMap<Long,String> map = getRuleWeights(ruleValue);
        if (map == null || map.isEmpty()) {
            return getNext().logic(strategyId,userId);
        }
        Long matched = map.floorKey(userPoint);
        if(matched == null) {
            log.info("没有可匹配的权重范围规则");
            return getNext().logic(strategyId,userId);
        }
        log.info("责任链-权重范围:接管, weight_key:{}",matched);
        return DefaultLogicChainFactory.ChainAwardVO.builder()
                .awardId(strategyAwardDispatch.getRandomAwardId(String.valueOf(strategyId),map.get(matched)))
                .logicChainType(DefaultLogicChainFactory.LogicChainType.RULE_WEIGHT)
                .build();
    }

    @Override
    protected String ruleModel() {
        return "rule_weight";
    }

    private TreeMap<Long,String> getRuleWeights(String ruleValue){
        String[] ruleWeights = ruleValue.split(Constants.SEMICOLON);
        TreeMap<Long,String> results = new TreeMap<>();
        for (String ruleWeight : ruleWeights) {
            if (StringUtils.isBlank(ruleWeight)) return null;
            //分割字符串，获得key 和 value
            String[] parts = ruleWeight.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("ruleValue wrong format" + ruleWeight);
            }
            log.info("{},{}",Long.parseLong(parts[0]), ruleWeight);
            results.put(Long.parseLong(parts[0]), ruleWeight);
        }
        return results;
    }
}
