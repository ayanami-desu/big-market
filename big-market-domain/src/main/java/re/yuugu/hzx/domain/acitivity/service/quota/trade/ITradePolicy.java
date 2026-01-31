package re.yuugu.hzx.domain.acitivity.service.quota.trade;

import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateSkuOrderAggregate;

/**
 * @ author anon
 * @ description ITradePolicy
 * @ create 2026/1/28 20:21
 */
public interface ITradePolicy {
    void tradeAsPolicy(CreateSkuOrderAggregate createSkuOrderAggregate);
}
