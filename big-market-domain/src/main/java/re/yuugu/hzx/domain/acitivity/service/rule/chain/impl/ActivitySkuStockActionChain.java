package re.yuugu.hzx.domain.acitivity.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityCountEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivitySkuEntity;
import re.yuugu.hzx.domain.acitivity.service.rule.chain.AbstractActionChain;

/**
 * @ author anon
 * @ description ActivitySkuStockActionChain
 * @ create 2026/1/5 00:56
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("activity_sku_stock_action, 校验sku库存是否充足");
        return getNext().action(activitySkuEntity, activityEntity, activityCountEntity);
    }
}
