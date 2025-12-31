package re.yuugu.hzx.domain.strategy.service.rule.tree.engine;

import lombok.extern.slf4j.Slf4j;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionVO;
import re.yuugu.hzx.domain.strategy.model.vo.RuleTreeEdgeVO;
import re.yuugu.hzx.domain.strategy.model.vo.RuleTreeNodeVO;
import re.yuugu.hzx.domain.strategy.model.vo.RuleTreeVO;
import re.yuugu.hzx.domain.strategy.service.rule.tree.ILogicTreeNode;
import re.yuugu.hzx.domain.strategy.service.rule.tree.factory.DefaultRuleTreeFactory;

import java.util.List;
import java.util.Map;

/**
 * @ author anon
 * @ description 根据factory构建的规则树，执行规则
 * @ create 2025/12/28 17:03
 */
@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {
    private final Map<String, ILogicTreeNode> ILogicTreeNodeGroup;
    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> iLogicTreeNodeGroup, RuleTreeVO ruleTreeVO) {
        this.ILogicTreeNodeGroup = iLogicTreeNodeGroup;
        this.ruleTreeVO = ruleTreeVO;
    }

    @Override
    public DefaultRuleTreeFactory.TreeAwardVO process(Long strategyId,String userId, Integer awardId) {
        DefaultRuleTreeFactory.TreeAwardVO treeAwardVO = null;
        //1. 获取树,其根节点,节点集合
        String nextNode = ruleTreeVO.getRootNode();
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();

        RuleTreeNodeVO node = treeNodeMap.get(nextNode);
        //2. 沿二叉树路径处理
        while (nextNode != null) {
            ILogicTreeNode logicTreeNode = ILogicTreeNodeGroup.get(node.getRuleKey());
            DefaultRuleTreeFactory.RuleTreeAction logicEntity = logicTreeNode.logic(userId, strategyId, awardId, node.getRuleValue());

            RuleActionVO action = logicEntity.getRuleActionVO();
            treeAwardVO = logicEntity.getTreeAwardVO();

            nextNode=nextNode(action,node.getTreeEdgeVOList());
            node = treeNodeMap.get(nextNode);
        }
        return treeAwardVO;
    }

    private String nextNode(RuleActionVO action, List<RuleTreeEdgeVO> edges){
        if(edges==null || edges.isEmpty()){
            return null;
        }
        if (RuleActionVO.NULL.getCode().equals(action.getCode())){
            return null;
        }
        // 根据 action 获取下一个节点
        for(RuleTreeEdgeVO edge:edges){
            if(decisionLogic(action.getCode(),edge)){
                return edge.getRuleNodeTo();
            }
        }
        throw new RuntimeException("决策树-引擎：规则配置有误");
    }
    private boolean decisionLogic(String actionCode, RuleTreeEdgeVO nodeLine) {
        switch (nodeLine.getRuleTreeEdgeCompOp()) {
            case EQUAL:
                return actionCode.equals(nodeLine.getRuleAction().getCode());
            // 以下规则暂时不需要实现
            case GT:
            case LT:
            case GE:
            case LE:
            default:
                return false;
        }
    }

}
