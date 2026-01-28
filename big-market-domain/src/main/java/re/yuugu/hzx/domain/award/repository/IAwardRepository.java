package re.yuugu.hzx.domain.award.repository;

import re.yuugu.hzx.domain.award.model.aggregate.CreateUserAwardRecordAggregate;
import re.yuugu.hzx.domain.award.model.aggregate.DistributeCreditAwardAggregate;

/**
 * @ author anon
 * @ description IAwardRepository
 * @ create 2026/1/15 19:43
 */
public interface IAwardRepository {
    void doSaveUserAwardRecordAggregate(CreateUserAwardRecordAggregate createUserAwardRecordAggregate);

    void doSaveDistributeCreditAwardAggregate(DistributeCreditAwardAggregate aggregate);

    String queryAwardConfigById(Integer awardId);
}
