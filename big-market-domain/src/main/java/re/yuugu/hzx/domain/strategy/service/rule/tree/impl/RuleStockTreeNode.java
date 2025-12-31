package re.yuugu.hzx.domain.strategy.service.rule.tree.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionVO;
import re.yuugu.hzx.domain.strategy.model.vo.StrategyAwardStockKeyVO;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.domain.strategy.service.rule.tree.ILogicTreeNode;
import re.yuugu.hzx.domain.strategy.service.rule.tree.factory.DefaultRuleTreeFactory;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description RuleStockTreeNode
 * @ create 2025/12/28 16:58
 */
@Slf4j
@Component("rule_stock")
public class RuleStockTreeNode implements ILogicTreeNode {
    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public DefaultRuleTreeFactory.RuleTreeAction logic(String userId, Long strategyId, Integer awardId,String ruleValue) {
        boolean status = strategyRepository.subtractAwardStock(strategyId,awardId);
        if(status){
            //发送扣减库存消息
            strategyRepository.sendConsumeAwardStock(StrategyAwardStockKeyVO.builder()
                    .awardId(awardId)
                    .strategyId(strategyId)
                    .build());
            return DefaultRuleTreeFactory.RuleTreeAction.builder()
                    .treeAwardVO(DefaultRuleTreeFactory.TreeAwardVO.builder()
                            .awardId(awardId)
                            .awardConfig("我是库存充足的奖品")
                            .build())
                    .ruleActionVO(RuleActionVO.NULL)
                    .build();
        }
        return DefaultRuleTreeFactory.RuleTreeAction.builder()
                .treeAwardVO(DefaultRuleTreeFactory.TreeAwardVO.builder()
                        .awardId(awardId)
                        .awardConfig("我是库存不足的奖品")
                        .build())
                .ruleActionVO(RuleActionVO.TAKE_OVER)
                .build();
    }
}
