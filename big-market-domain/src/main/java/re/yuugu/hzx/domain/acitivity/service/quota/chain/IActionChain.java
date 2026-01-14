package re.yuugu.hzx.domain.acitivity.service.quota.chain;

import re.yuugu.hzx.domain.acitivity.model.entity.ActivityCountEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivitySkuEntity;

/**
 * @ author anon
 * @ description IActionChain
 * @ create 2026/1/5 00:38
 */
public interface IActionChain extends IActionArmory{
    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity  activityCountEntity);
}
