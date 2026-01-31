package re.yuugu.hzx.domain.acitivity.service.quota.trade.impl;

import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateSkuOrderAggregate;
import re.yuugu.hzx.domain.acitivity.model.vo.ActivityQuotaOrderState;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;
import re.yuugu.hzx.domain.acitivity.service.quota.trade.ITradePolicy;

/**
 * @ author anon
 * @ description CreditPayTradePolicy
 * @ create 2026/1/28 20:56
 */
@Service("credit_pay_trade_policy")
public class CreditPayTradePolicy implements ITradePolicy {
    private final IActivityRepository  activityRepository;
    public CreditPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }
    @Override
    public void tradeAsPolicy(CreateSkuOrderAggregate createSkuOrderAggregate) {
        createSkuOrderAggregate.setActivityOrderEntityState(ActivityQuotaOrderState.wait_to_pay);
        activityRepository.doSaveCreditPayActivityOrderAggregate(createSkuOrderAggregate);
    }
}
