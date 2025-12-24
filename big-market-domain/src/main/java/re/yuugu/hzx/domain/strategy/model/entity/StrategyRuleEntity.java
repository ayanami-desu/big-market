package re.yuugu.hzx.domain.strategy.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import re.yuugu.hzx.types.common.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyRuleEntity {
    /**
     * 抽奖策略ID
     */
    private Long strategyId;
    /**
     * 抽奖奖品ID【规则类型为策略，则不需要奖品ID】
     */
    private Integer awardId;
    /**
     * 抽象规则类型；1-策略规则、2-奖品规则
     */
    private Integer ruleType;
    /**
     * 抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】
     */
    private String ruleModel;
    /**
     * 抽奖规则比值
     * e.g. 4000:102,103,104,105,106,107,108,109;6000:103,104,105,106,107,108,109
     */
    private String ruleValue;
    /**
     * 抽奖规则描述
     */
    private String ruleDesc;

    public Map<String, List<Integer>> getRuleWeights() {
        if (!"rule_weight".equals(ruleModel)) return null;
        if (StringUtils.isBlank(ruleValue)) return null;
        String[] ruleWeights = ruleValue.split(Constants.SEMICOLON);
        Map<String, List<Integer>> results = new HashMap<>();
        for (String ruleWeight : ruleWeights) {
            if (StringUtils.isBlank(ruleWeight)) return null;
            //分割字符串，获得key 和 value
            String[] parts = ruleWeight.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("ruleValue wrong format" + ruleWeight);
            }
            String[] valueStrings = parts[1].split(Constants.COMMA);
            List<Integer> valueList = new ArrayList<>();
            for (String value : valueStrings) {
                valueList.add(Integer.parseInt(value));
            }
            results.put(ruleWeight, valueList);
        }
        return results;
    }
}
