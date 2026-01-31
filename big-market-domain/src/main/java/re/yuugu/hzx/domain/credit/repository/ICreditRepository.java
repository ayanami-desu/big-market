package re.yuugu.hzx.domain.credit.repository;

import re.yuugu.hzx.domain.credit.model.aggregate.CreateCreditAdjustAggregate;

/**
 * @ author anon
 * @ description ICreditRepository
 * @ create 2026/1/28 14:36
 */
public interface ICreditRepository {
    void doSaveCreditPayOrderAggregate(CreateCreditAdjustAggregate createCreditAdjustAggregate);

    void doSaveCreditAdjustAggregate(CreateCreditAdjustAggregate createCreditAdjustAggregate);
}
