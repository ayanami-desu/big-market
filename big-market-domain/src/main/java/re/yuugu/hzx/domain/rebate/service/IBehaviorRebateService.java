package re.yuugu.hzx.domain.rebate.service;

import re.yuugu.hzx.domain.rebate.model.entity.RebateBehaviorEntity;

import java.util.List;

/**
 * @ author anon
 * @ description SaveRebateOrder
 * @ create 2026/1/24 15:53
 */
public interface IBehaviorRebateService {
    /**
     * @return 创建的订单号，一种返利行为可能对应多种返利结果
     */
    List<String> saveRebateOrder(RebateBehaviorEntity rebateBehaviorEntity);
}
