package re.yuugu.hzx.domain.strategy.service.Gacha;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyAwardEntity;
import re.yuugu.hzx.domain.strategy.model.vo.RuleTreeVO;
import re.yuugu.hzx.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.domain.strategy.service.AbstractGachaStrategy;
import re.yuugu.hzx.domain.strategy.service.IGachaAward;
import re.yuugu.hzx.domain.strategy.service.rule.chain.ILogicChain;
import re.yuugu.hzx.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import re.yuugu.hzx.domain.strategy.service.rule.tree.engine.DecisionTreeEngine;
import re.yuugu.hzx.domain.strategy.service.rule.tree.factory.DefaultRuleTreeFactory;

import java.util.List;

@Slf4j
@Service
public class DefaultGachaStrategy extends AbstractGachaStrategy implements IGachaAward {
    // 子类直接用父类的字段值
    public DefaultGachaStrategy(IStrategyRepository strategyRepository, DefaultLogicChainFactory defaultLogicChainFactory,DefaultRuleTreeFactory defaultRuleTreeFactory) {
        super(strategyRepository, defaultLogicChainFactory, defaultRuleTreeFactory);
    }

    @Override
    protected DefaultLogicChainFactory.ChainAwardVO processLogicChain(Long strategyId, String userId) {
        ILogicChain logicChain = defaultLogicChainFactory.openLogicChain(strategyId);
        return logicChain.logic(strategyId,userId);
    }

    @Override
    protected DefaultRuleTreeFactory.TreeAwardVO processLogicTree(Long strategyId, String userId, Integer awardId) {
        log.info("开始规则树处理,{},{}",strategyId,awardId);
        StrategyAwardRuleModelVO strategyAwardRuleModelVO= strategyRepository.queryStrategyAwardRuleModelVO(strategyId,awardId);
        if(strategyAwardRuleModelVO==null){
            //未配置规则，直接返回
            log.info("strategyAwardRuleModelVO is null,{},{}",strategyId,awardId);
            return DefaultRuleTreeFactory.TreeAwardVO.builder()
                    .awardId(awardId)
                    .build();
        }
        //TODO 这里应该根据树根节点对规则进行过滤
        log.info(strategyAwardRuleModelVO.getValue());
        //设置一个 mock 值
//        RuleTreeVO tree = strategyRepository.queryRuleTreeByRootNode(strategyAwardRuleModelVO.getValue());
        RuleTreeVO tree = strategyRepository.queryRuleTreeByRootNode("rule_lock");
        if (null == tree) {
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key，未在库表 rule_tree、rule_tree_node、rule_tree_line 配置对应的规则树信息");
        }
        DecisionTreeEngine engine = defaultRuleTreeFactory.openRuleTreeEngine(tree);

        return engine.process(strategyId,userId,awardId);
    }

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        return strategyRepository.queryStrategyAwardList(strategyId);
    }
}
