package re.yuugu.hzx.domain.acitivity.repository;

import re.yuugu.hzx.domain.acitivity.model.entity.ActivityCountEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivitySkuEntity;

/**
 * @ author anon
 * @ description IActivityRepository
 * @ create 2026/1/4 17:38
 */

public interface IActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryActivityById(Long activityId);

    ActivityCountEntity queryActivityCountByActivityCountId(Long activityCountId);
}
