package re.yuugu.hzx.domain.strategy.model.entity;


import lombok.*;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// 规则动作
public class RuleActionEntity <T extends RuleActionEntity.GachaEntity>{

    private String code= RuleActionType.ALLOW.getCode();
    private String info= RuleActionType.ALLOW.getInfo();
    private String ruleModel;
    private T data;
    static public class GachaEntity {

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static public class BeforeGachaEntity extends RuleActionEntity.GachaEntity {
        // 用于抽奖的策略 id
        private Long strategyId;
        // 黑名单时直接返回的奖品 id
        private Integer awardId;
        private String ruleWeightValueKey;
    }
}
