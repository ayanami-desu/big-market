package re.yuugu.hzx.domain.strategy.model.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import re.yuugu.hzx.types.common.Constants;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyAwardRuleModelVO {
    String value;

    public String[] gachaProcessingRule() {
        return Arrays.stream(value.split(Constants.COMMA))
                .filter(DefaultLogicFactory.LogicModel::isProcessing)
                .toArray(String[]::new);
    }
}
