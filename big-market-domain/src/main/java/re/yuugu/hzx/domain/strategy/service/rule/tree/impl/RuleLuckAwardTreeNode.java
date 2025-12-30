package re.yuugu.hzx.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionVO;
import re.yuugu.hzx.domain.strategy.service.rule.tree.ILogicTreeNode;
import re.yuugu.hzx.domain.strategy.service.rule.tree.factory.DefaultRuleTreeFactory;
import re.yuugu.hzx.types.common.Constants;

/**
 * @ author anon
 * @ description RuleLuckAwardTreeNode
 * @ create 2025/12/28 16:54
 */
@Slf4j
@Component("rule_luck_award")
public class RuleLuckAwardTreeNode implements ILogicTreeNode {
    @Override
    public DefaultRuleTreeFactory.RuleTreeAction logic(String userId, Long strategyId, Integer awardId,String ruleValue) {
        int l,r;
        String[] parts = ruleValue.split(Constants.COMMA);
        l = Integer.parseInt(parts[0]);
        r = Integer.parseInt(parts[1]);
        return DefaultRuleTreeFactory.RuleTreeAction.builder()
                .ruleActionVO(RuleActionVO.ALLOW)
                .treeAwardVO(DefaultRuleTreeFactory.TreeAwardVO.builder()
                        .awardId(awardId)
                        .awardConfig(String.valueOf((l+r/2)))
                        .build())
                .build();
    }
}
