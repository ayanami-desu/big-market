package re.yuugu.hzx.domain.rebate.repository;

import re.yuugu.hzx.domain.rebate.model.aggregate.CreateRebateOrderAggregate;
import re.yuugu.hzx.domain.rebate.model.vo.BehaviorRebateVO;

import java.util.List;

/**
 * @ author anon
 * @ description IBehaviorRebateRepository
 * @ create 2026/1/24 16:37
 */
public interface IBehaviorRebateRepository {
    List<BehaviorRebateVO> queryBehaviorRebateConfig(String behaviorType);

    void doSaveRebateOrderAggregate(CreateRebateOrderAggregate createRebateOrderAggregate);
}
