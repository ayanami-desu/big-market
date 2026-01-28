package re.yuugu.hzx.domain.award.service.distribute;

import re.yuugu.hzx.domain.award.model.entity.AwardDistributeEntity;

/**
 * @ author anon
 * @ description IAwardDistribute
 * @ create 2026/1/27 13:33
 */
public interface IAwardDistribute {
    void dispatchAward(AwardDistributeEntity awardDistributeEntity);
}
