package re.yuugu.hzx.domain.strategy.service.rule.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;

@Slf4j
@Component
@LogicStrategy(logicMode= DefaultLogicFactory.LogicModel.RULE_WIGHT)
public class RuleWeightLogicFilter implements ILogicFilter<RuleActionEntity.BeforeGachaEntity> {
    @Resource
    private IStrategyRepository repository;

    public Long userPoint = 4500L;

    @Override
    public RuleActionEntity<RuleActionEntity.BeforeGachaEntity> filter(RuleDetailEntity ruleDetailEntity) {
        log.info("开始权重范围规则过滤");
        Long strategyId = ruleDetailEntity.getStrategyId();
        String ruleModel = ruleDetailEntity.getRuleModel();
        Integer awardId = ruleDetailEntity.getAwardId();
        String ruleValue = repository.queryStrategyRuleValue(strategyId,ruleModel,awardId);
        TreeMap<Long,String> map = getRuleWeights(ruleValue);
        if (map == null || map.isEmpty()) {
            return new RuleActionEntity<>();
        }
        Long matched = map.floorKey(userPoint);
        if(matched == null) {
            log.info("没有可匹配的权重范围规则");
            return new RuleActionEntity<>();
        }
        log.info("权重范围规则匹配成功, key:{}",matched);
        return RuleActionEntity.<RuleActionEntity.BeforeGachaEntity>builder()
                .code(RuleActionVO.TAKE_OVER.getCode())
                .info(RuleActionVO.TAKE_OVER.getInfo())
                .ruleModel(DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode())
                .data(RuleActionEntity.BeforeGachaEntity.builder()
                        .strategyId(ruleDetailEntity.getStrategyId())
                        .ruleWeightValueKey(map.get(matched))
                        .build())
                .build();
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
