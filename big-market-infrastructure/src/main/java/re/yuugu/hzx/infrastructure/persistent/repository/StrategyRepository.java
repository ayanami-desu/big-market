package re.yuugu.hzx.infrastructure.persistent.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyEntity;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyRuleEntity;
import re.yuugu.hzx.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import re.yuugu.hzx.domain.strategy.po.AliasTable;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.infrastructure.persistent.dao.IStrategyAwardDao;
import re.yuugu.hzx.infrastructure.persistent.dao.IStrategyDao;
import re.yuugu.hzx.infrastructure.persistent.dao.IStrategyRuleDao;
import re.yuugu.hzx.infrastructure.persistent.po.Strategy;
import re.yuugu.hzx.infrastructure.persistent.po.StrategyAward;
import re.yuugu.hzx.infrastructure.persistent.po.StrategyRule;
import re.yuugu.hzx.infrastructure.persistent.redis.IRedisService;
import re.yuugu.hzx.types.common.Constants;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class StrategyRepository implements IStrategyRepository {
    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IStrategyDao  strategyDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Resource
    private IRedisService  redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId){
        // 从 redis 读取
        String cacheKey = Constants.RedisKeys.STRATEGY_AWARD_KEY+strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        if (strategyAwardEntities!=null && !strategyAwardEntities.isEmpty()){
            return strategyAwardEntities;
        }
        // 从数据库中读取
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity= StrategyAwardEntity.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .awardCount(strategyAward.getAwardCount())
                    .awardRate(strategyAward.getAwardRate())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }
        //写回 redis
        redisService.setValue(cacheKey,strategyAwardEntities);
        return strategyAwardEntities;
    }

    @Override
    public void storeStrategyAwardRateSearchTable(String key,int[] awardIds, double[] probe, int[] alias) {
        AliasTable aliasTable = new AliasTable(awardIds,probe,alias);
        redisService.setValue(Constants.RedisKeys.STRATEGY_AWARD_ALIAS_KEY+key,aliasTable);
    }

    @Override
    public AliasTable getStrategyAwardRateSearchTable(String key) {
        return redisService.getValue(Constants.RedisKeys.STRATEGY_AWARD_ALIAS_KEY+key);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        String cacheKey = Constants.RedisKeys.STRATEGY_KEY+strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (strategyEntity!=null){
            return strategyEntity;
        }
        // 从数据库读
        Strategy strategy = strategyDao.queryStrategyByStrategyId(strategyId);
        if (strategy==null){
            return null;
        }
        strategyEntity = StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .ruleModels(strategy.getRuleModels())
                .build();
        redisService.setValue(cacheKey,strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRuleEntity(Long strategyId, String ruleModel) {
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);
        StrategyRule s = strategyRuleDao.queryStrategyRule(strategyRuleReq);
        if (s==null){
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
        if (ruleValue!=null){return ruleValue;}
        // query database
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);
        strategyRuleReq.setAwardId(awardId);
        StrategyRule res = strategyRuleDao.queryStrategyRuleValue(strategyRuleReq);
        redisService.setValue(cacheKey,res.getRuleValue());
        return res.getRuleValue();
    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer gachaAwardId) {
        String cacheKey = Constants.RedisKeys.STRATEGY_AWARD_RULE_MODELS
                + strategyId
                + "_" + gachaAwardId;
        String models = redisService.getValue(cacheKey);
        if(models!=null){
            return StrategyAwardRuleModelVO.builder()
                    .value(models)
                    .build();
        }
        StrategyAward res =  strategyAwardDao.queryStrategyAwardRuleModels(strategyId,gachaAwardId);
        redisService.setValue(cacheKey,res.getRuleModels());
        return StrategyAwardRuleModelVO.builder()
                .value(res.getRuleModels())
                .build();
    }
}
