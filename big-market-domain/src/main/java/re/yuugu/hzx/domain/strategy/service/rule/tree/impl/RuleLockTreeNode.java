package re.yuugu.hzx.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionVO;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.domain.strategy.service.rule.tree.ILogicTreeNode;
import re.yuugu.hzx.domain.strategy.service.rule.tree.factory.DefaultRuleTreeFactory;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description RuleLockTreeNode
 * @ create 2025/12/28 16:54
 */
@Slf4j
@Component("rule_lock")
public class RuleLockTreeNode implements ILogicTreeNode {
    @Resource
    private IStrategyRepository  strategyRepository;
    @Override
    public DefaultRuleTreeFactory.RuleTreeAction logic(String userId, Long strategyId, Integer awardId,String ruleValue) {
        log.info("规则树-抽奖次数锁");
        long cnt = Long.parseLong(ruleValue);
        long userGachaCount = strategyRepository.queryUserGachaCount(userId,strategyId);
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
