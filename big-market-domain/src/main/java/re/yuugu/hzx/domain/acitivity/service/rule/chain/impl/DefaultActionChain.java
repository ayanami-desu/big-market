package re.yuugu.hzx.domain.acitivity.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityCountEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivitySkuEntity;
import re.yuugu.hzx.domain.acitivity.service.rule.chain.AbstractActionChain;

/**
 * @ author anon
 * @ description DefaultActionChain
 * @ create 2026/1/5 11:06
 */
@Slf4j
@Component("activity_default")
public class DefaultActionChain extends AbstractActionChain {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("默认责任链,返回 true");
        return true;
    }
}
