package re.yuugu.hzx.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionVO;
import re.yuugu.hzx.domain.strategy.service.rule.tree.ILogicTreeNode;
import re.yuugu.hzx.domain.strategy.service.rule.tree.factory.DefaultRuleTreeFactory;

/**
 * @ author anon
 * @ description RuleLockTreeNode
 * @ create 2025/12/28 16:54
 */
@Slf4j
@Component("rule_lock")
public class RuleLockTreeNode implements ILogicTreeNode {
    public Long userGachaCount =0L;
    @Override
    public DefaultRuleTreeFactory.RuleTreeAction logic(String userId, Long strategyId, Integer awardId,String ruleValue) {
        long cnt = Long.parseLong(ruleValue);
        if(cnt>userGachaCount){
            return DefaultRuleTreeFactory.RuleTreeAction.builder()
                    .ruleActionVO(RuleActionVO.TAKE_OVER)
                    .build();
        }
        return DefaultRuleTreeFactory.RuleTreeAction.builder()
                .ruleActionVO(RuleActionVO.ALLOW)
                .build();
    }
}
