package re.yuugu.hzx.domain.acitivity.service;

import re.yuugu.hzx.domain.acitivity.model.entity.ActivityChargeEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityOrderEntity;

/**
 * @ author anon
 * @ description IGachaOrder 用户获得抽奖次数
 * @ create 2026/1/4 18:27
 */
public interface IGachaOrder {
    ActivityOrderEntity createGachaActivityOrder(ActivityChargeEntity activityChargeEntity);
}
