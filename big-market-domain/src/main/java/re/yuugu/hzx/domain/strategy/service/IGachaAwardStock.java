package re.yuugu.hzx.domain.strategy.service;

import re.yuugu.hzx.domain.strategy.model.vo.StrategyAwardStockKeyVO;

/**
 * @ author anon
 * @ description IGachaStock
 * @ create 2026/1/15 21:38
 */
public interface IGachaAwardStock {

    StrategyAwardStockKeyVO offerStrategyAwardQueueValue();

    void updateStrategyAwardStock(Long strategyId, Integer awardId);
}
