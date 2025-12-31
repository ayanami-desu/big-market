package re.yuugu.hzx.domain.strategy.service.armory;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyEntity;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyRuleEntity;
import re.yuugu.hzx.domain.strategy.po.AliasTable;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;


@Slf4j
@Service
public class StrategyArmory implements IStrategyArmory {
    private static final ThreadLocal<SecureRandom> TLS_SR =
            ThreadLocal.withInitial(SecureRandom::new);
    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1. 查询策略配置，即某个抽奖策略中有哪些奖品，其概率、数量、排序等信息
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
        //2. 缓存奖品剩余数量
        for(StrategyAwardEntity strategyAwardEntity : strategyAwardEntities){
            repository.cacheStrategyAwardSurplusCount(strategyId,strategyAwardEntity.getAwardId(),strategyAwardEntity.getAwardCountSurplus());
        }

        assembleOneStrategy(String.valueOf(strategyId), strategyAwardEntities);

        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        if (strategyEntity == null) return false;
        String ruleWeight = strategyEntity.getRuleWeight();
        if (ruleWeight == null) return false;
        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRuleEntity(strategyId, ruleWeight);
        if (strategyRuleEntity == null) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        Map<String, List<Integer>> ruleWeightsMap = strategyRuleEntity.getRuleWeights();
        if (ruleWeightsMap == null || ruleWeightsMap.isEmpty()) return false;
        Set<String> keySet = ruleWeightsMap.keySet();
        for (String key : keySet) {
            List<Integer> valueList = ruleWeightsMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(e -> !valueList.contains(e.getAwardId()));
            assembleOneStrategy(String.valueOf(strategyId).concat("_").concat(key), strategyAwardEntitiesClone);
        }
        return true;
    }

    @Override
    public void assembleOneStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        int n = strategyAwardEntities.size();

        //获取概率总和
        BigDecimal rateSum = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (rateSum.compareTo(BigDecimal.ZERO) <= 0) return;
        double[] scaled = new double[n];
        int[] awardIds = new int[n];
        for (int i = 0; i < n; i++) {
            StrategyAwardEntity e = strategyAwardEntities.get(i);
            awardIds[i] = e.getAwardId();

            BigDecimal p = e.getAwardRate().divide(rateSum, 16, RoundingMode.HALF_UP);
            scaled[i] = p.multiply(BigDecimal.valueOf(n)).doubleValue();
        }

        double[] probe = new double[n];
        int[] alias = new int[n];

        Deque<Integer> smaller = new ArrayDeque<>();
        Deque<Integer> larger = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (scaled[i] < 1.0) smaller.add(i);
            else larger.add(i);
        }
        while (!smaller.isEmpty() && !larger.isEmpty()) {
            int s = smaller.removeLast();
            int l = larger.removeLast();

            probe[s] = scaled[s]; // scaled[s]<1.0
            alias[s] = l; // 把 l 多余的部分借给 s

            // 把 l 借给 s 之后，更新 l 剩余容量
            scaled[l] = scaled[s] + scaled[l] - 1.0;
            if (scaled[l] < 1.0) smaller.add(l);
            else larger.add(l);
        }
        while (!smaller.isEmpty()) {
            int s = smaller.removeLast();
            probe[s] = 1.0;
            alias[s] = s;
        }
        while (!larger.isEmpty()) {
            int l = larger.removeLast();
            probe[l] = 1.0;
            alias[l] = l;
        }

        repository.storeStrategyAwardRateSearchTable(key, awardIds, probe, alias);
    }

    @Override
    public Integer getRandomAwardId(String key) {
        AliasTable table = repository.getStrategyAwardRateSearchTable(key);
        return getInteger(table);
    }

    @NonNull
    private Integer getInteger(AliasTable table) {
        if (table == null) throw new IllegalStateException("alias table missing");
        SecureRandom sr = TLS_SR.get();
        int column = sr.nextInt(table.getN());
        double u = sr.nextDouble();
        int idx = (u < table.getProbe()[column]) ? column : table.getAlias()[column];
        return table.getAwardIds()[idx];
    }

    @Override
    public Integer getRandomAwardId(String key, String ruleWeight) {
        AliasTable table = repository.getStrategyAwardRateSearchTable(key + "_" + ruleWeight);
        return getInteger(table);
    }
}
