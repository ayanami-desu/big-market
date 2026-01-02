package re.yuugu.hzx.infrastructure.persistent.repository;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Repository;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyEntity;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyRuleEntity;
import re.yuugu.hzx.domain.strategy.model.vo.*;
import re.yuugu.hzx.domain.strategy.po.AliasTable;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.infrastructure.persistent.dao.*;
import re.yuugu.hzx.infrastructure.persistent.po.*;
import re.yuugu.hzx.infrastructure.persistent.redis.IRedisService;
import re.yuugu.hzx.types.common.Constants;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class StrategyRepository implements IStrategyRepository {
    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;
    @Resource
    private IRuleTreeDao ruleTreeDao;

    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;

    @Resource
    private IRuleTreeEdgeDao ruleTreeEdgeDao;

    @Resource
    private IRedisService redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        // 从 redis 读取
        String cacheKey = Constants.RedisKeys.STRATEGY_AWARD_LIST_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        if (strategyAwardEntities != null && !strategyAwardEntities.isEmpty()) {
            return strategyAwardEntities;
        }
        // 从数据库中读取
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        if(strategyAwards==null || strategyAwards.isEmpty()){
            log.error("未查询到奖品列表 strategyId:{}",strategyId);
        }
        strategyAwardEntities = new ArrayList<>();
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .awardTitle(strategyAward.getAwardTitle())
                    .awardSubtitle(strategyAward.getAwardSubtitle())
                    .awardCount(strategyAward.getAwardCount())
                    .awardRate(strategyAward.getAwardRate())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .sort(strategyAward.getSort())
                    .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }
        //写回 redis
        redisService.setValue(cacheKey, strategyAwardEntities);
        return strategyAwardEntities;
    }

    @Override
    public void storeStrategyAwardRateSearchTable(String key, int[] awardIds, double[] probe, int[] alias) {
        AliasTable aliasTable = new AliasTable(awardIds, probe, alias);
        redisService.setValue(Constants.RedisKeys.STRATEGY_AWARD_ALIAS_KEY + key, aliasTable);
    }

    @Override
    public AliasTable getStrategyAwardRateSearchTable(String key) {
        String cacheKey = Constants.RedisKeys.STRATEGY_AWARD_ALIAS_KEY + key;
        if(!redisService.isExists(cacheKey)){
            throw new AppException(ResponseCode.STRATEGY_NOT_CONFIGURED.getCode(),ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        return redisService.getValue(cacheKey);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        String cacheKey = Constants.RedisKeys.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (strategyEntity != null) {
            return strategyEntity;
        }
        // 从数据库读
        Strategy strategy = strategyDao.queryStrategyByStrategyId(strategyId);
        if (strategy == null) {
            return null;
        }
        strategyEntity = StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .ruleModels(strategy.getRuleModels())
                .build();
        redisService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRuleEntity(Long strategyId, String ruleModel) {
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);
        StrategyRule s = strategyRuleDao.queryStrategyRule(strategyRuleReq);
        if (s == null) {
            return null;
        }
        return StrategyRuleEntity.builder()
                .strategyId(s.getStrategyId())
                .awardId(s.getAwardId())
                .ruleType(s.getRuleType())
                .ruleModel(s.getRuleModel())
                .ruleValue(s.getRuleValue())
                .ruleDesc(s.getRuleDesc())
                .build();
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel, Integer awardId) {
        String cacheKey = Constants.RedisKeys.STRATEGY_RULE_VALUE_KEY
                + strategyId
                + "_" + ruleModel
                + "_" + (awardId == null ? "no_award_id" : awardId);
        String ruleValue = redisService.getValue(cacheKey);
        if (ruleValue != null) {
            return ruleValue;
        }
        // query database
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);
        strategyRuleReq.setAwardId(awardId);
        StrategyRule res = strategyRuleDao.queryStrategyRuleValue(strategyRuleReq);
        redisService.setValue(cacheKey, res.getRuleValue());
        return res.getRuleValue();
    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer gachaAwardId) {
        String cacheKey = Constants.RedisKeys.STRATEGY_AWARD_RULE_MODELS
                + strategyId
                + "_" + gachaAwardId;
        String models = redisService.getValue(cacheKey);
        if (models != null) {
            return StrategyAwardRuleModelVO.builder()
                    .value(models)
                    .build();
        }
        StrategyAward res = strategyAwardDao.queryStrategyAwardRuleModels(strategyId, gachaAwardId);
        redisService.setValue(cacheKey, res.getRuleModels());
        return StrategyAwardRuleModelVO.builder()
                .value(res.getRuleModels())
                .build();
    }

    @Override
    public RuleTreeVO queryRuleTreeByRootNode(String rootNode) {
        String cacheKey = Constants.RedisKeys.STRATEGY_RULE_TREE_VO + rootNode;
        RuleTreeVO ruleTreeVO = redisService.getValue(cacheKey);
        if (ruleTreeVO != null) {
            return ruleTreeVO;
        }
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByRootNode(rootNode);
        if (null == ruleTree) {
            return null;
        }
        List<RuleTreeNode> nodeEntities = ruleTreeNodeDao.queryNodeListByTreeId(ruleTree.getTreeId());
        List<RuleTreeEdge> edgeEntities = ruleTreeEdgeDao.queryEdgeListByTreeId(ruleTree.getTreeId());
        // build rule tree

        // 1. 实例化 edgeVO; 边可以为空
        List<RuleTreeEdgeVO> edgeVOs = new ArrayList<>();
        for (RuleTreeEdge edgeEntity : edgeEntities) {
            edgeVOs.add(RuleTreeEdgeVO.builder()
                    .treeId(edgeEntity.getTreeId())
                    .ruleNodeFrom(edgeEntity.getRuleNodeFrom())
                    .ruleNodeTo(edgeEntity.getRuleNodeTo())
                    .ruleTreeEdgeCompOp(RuleTreeEdgeCompOpVO.getByName(edgeEntity.getRuleTreeEdgeCompOp()))
                    .ruleAction(RuleActionVO.getByName(edgeEntity.getRuleAction()))
                    .build());
        }
        // 2, 实例化 nodeVO
        if (nodeEntities == null || nodeEntities.isEmpty()) {
            throw new RuntimeException("树节点数量不能为0");
        }
        Map<String, RuleTreeNodeVO> nodeVOs = new HashMap<>();
        for (RuleTreeNode nodeEntity : nodeEntities) {
            String key = nodeEntity.getRuleKey();
            nodeVOs.put(nodeEntity.getRuleKey(), RuleTreeNodeVO.builder()
                    .nodeId(nodeEntity.getNodeId())
                    .ruleKey(nodeEntity.getRuleKey())
                    .ruleDesc(nodeEntity.getRuleDesc())
                    .ruleValue(nodeEntity.getRuleValue())
                    .treeEdgeVOList(edgeVOs.stream().filter(e -> e.getRuleNodeFrom().equals(key)).collect(Collectors.toList()))
                    .build());
        }
        ruleTreeVO = RuleTreeVO.builder()
                .treeId(ruleTree.getTreeId())
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .rootNode(ruleTree.getRootNode())
                .treeNodeMap(nodeVOs)
                .build();
        redisService.setValue(cacheKey, ruleTreeVO);
        // 3. 返回树
        return ruleTreeVO;
    }

    @Override
    public void cacheStrategyAwardSurplusCount(Long strategyId, Integer awardId, Integer awardCountSurplus) {
        String cacheKey = Constants.RedisKeys.STRATEGY_AWARD_SURPLUS_COUNT + strategyId + "_" + awardId;
        if(redisService.isExists(cacheKey)){
            return;
        }
        redisService.setAtomicLong(cacheKey, awardCountSurplus);
    }

    @Override
    public boolean subtractAwardStock(Long strategyId, Integer awardId) {
        String cacheKey = Constants.RedisKeys.STRATEGY_AWARD_SURPLUS_COUNT + strategyId + "_" + awardId;
        long surplus = redisService.decr(cacheKey);
        if (surplus < 0) {
            log.info("库存不足");
            return false;
        }
        //加锁
        //已经上锁代表这个库存数已经被扣减过
        String lockKey = cacheKey+"_" + surplus;
        boolean lock = redisService.setNX(lockKey);
        if (!lock) {
            log.info("出现异常：库存数不一致");
        }
        return lock;
    }

    @Override
    public void sendConsumeAwardStock(StrategyAwardStockKeyVO strategyAwardStockKeyVO) {
        String cacheKey = Constants.RedisKeys.STRATEGY_AWARD_STOCK_QUEUE;
        RBlockingQueue<StrategyAwardStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<StrategyAwardStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        // 3s 后再加入队列
        delayedQueue.offer(strategyAwardStockKeyVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public StrategyAwardStockKeyVO offerStrategyAwardQueueValue() throws InterruptedException {
        String cacheKey = Constants.RedisKeys.STRATEGY_AWARD_STOCK_QUEUE;
        RBlockingQueue<StrategyAwardStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        return blockingQueue.take();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        strategyAwardDao.updateStrategyAwardStock(strategyId, awardId);
    }
}
