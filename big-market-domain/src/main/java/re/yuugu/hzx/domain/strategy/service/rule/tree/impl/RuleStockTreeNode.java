package re.yuugu.hzx.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionVO;
import re.yuugu.hzx.domain.strategy.service.rule.tree.ILogicTreeNode;
import re.yuugu.hzx.domain.strategy.service.rule.tree.factory.DefaultRuleTreeFactory;

/**
 * @ author anon
 * @ description RuleStockTreeNode
 * @ create 2025/12/28 16:58
 */
@Slf4j
@Component("rule_stock")
public class RuleStockTreeNode implements ILogicTreeNode {
    public Long stockNum=0L;
    @Override
    public DefaultRuleTreeFactory.RuleTreeAction logic(String userId, Long strategyId, Integer awardId,String ruleValue) {
        if(stockNum<=0){
            return DefaultRuleTreeFactory.RuleTreeAction.builder()
                    .ruleActionVO(RuleActionVO.TAKE_OVER)
                    .build();
        }
        return DefaultRuleTreeFactory.RuleTreeAction.builder()
                .treeAwardVO(DefaultRuleTreeFactory.TreeAwardVO.builder()
                        .awardId(awardId)
                        .awardConfig("我是库存充足的奖品")
                        .build())
                .ruleActionVO(RuleActionVO.NULL)
                .build();
    }
}
