package re.yuugu.hzx.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import re.yuugu.hzx.types.common.Constants;

import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyEntity {
    /**
     * 抽奖策略 ID
     */
    private Long strategyId;

    /**
     * 抽奖策略规则模型
     * rule_weight,rule_blacklist
     */
    private String ruleModels;

    public String[] ruleModels(){
        if(StringUtils.isBlank(ruleModels)) return null;
        return ruleModels.split(Constants.COMMA);
    }

    public String getRuleWeight(){
        String[] ruleModels = this.ruleModels();
        for(String ruleModel : ruleModels){
            if("rule_weight".equals(ruleModel)){
                return ruleModel;
            }
        }
        return null;
    }
}
