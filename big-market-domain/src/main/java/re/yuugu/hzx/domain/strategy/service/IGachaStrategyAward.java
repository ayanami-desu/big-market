package re.yuugu.hzx.domain.strategy.service;

import re.yuugu.hzx.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @ author anon
 * @ description 策略中的奖品
 * @ create 2026/1/1 15:24
 */
public interface IGachaStrategyAward {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);
}
