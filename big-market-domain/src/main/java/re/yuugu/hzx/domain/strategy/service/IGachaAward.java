package re.yuugu.hzx.domain.strategy.service;

import re.yuugu.hzx.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @ author anon
 * @ description IGachaAward
 * @ create 2026/1/1 15:24
 */
public interface IGachaAward {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);
}
