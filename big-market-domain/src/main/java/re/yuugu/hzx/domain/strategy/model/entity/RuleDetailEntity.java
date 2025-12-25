package re.yuugu.hzx.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleDetailEntity {
    private String userId;
    private Long strategyId;
    private String ruleModel;
    // ruleType 为 2 时不需要奖品
    private Integer awardId;
    private String ruleValue;
}
