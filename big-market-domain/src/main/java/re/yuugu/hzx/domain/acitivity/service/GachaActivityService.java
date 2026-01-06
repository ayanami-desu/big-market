package re.yuugu.hzx.domain.acitivity.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateOrderAggregate;
import re.yuugu.hzx.domain.acitivity.model.entity.*;
import re.yuugu.hzx.domain.acitivity.model.vo.OrderState;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;
import re.yuugu.hzx.domain.acitivity.service.rule.chain.factory.DefaultActionChainFactory;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ author anon
 * @ description GachaActivityService
 * @ create 2026/1/4 21:31
 */
@Service
public class GachaActivityService extends AbstractGachaActivity {
    @Resource
    private IActivityRepository activityRepository;

    public GachaActivityService(IActivityRepository activityRepository, DefaultActionChainFactory defaultActionChainFactory) {
        super(activityRepository, defaultActionChainFactory);
    }

    @Override
    protected void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
        activityRepository.saveOrder(createOrderAggregate);
    }

    @Override
    protected CreateOrderAggregate aggregateOrder(ActivityChargeEntity activityChargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        ActivityOrderEntity activityOrderEntity = new ActivityOrderEntity();


        activityOrderEntity.setUserId(activityChargeEntity.getUserId());
        activityOrderEntity.setSku(activityChargeEntity.getSku());
        activityOrderEntity.setBizId(activityChargeEntity.getBizId());

        activityOrderEntity.setActivityId(activitySkuEntity.getActivityId());
        activityOrderEntity.setActivityName(activityEntity.getActivityName());
        activityOrderEntity.setStrategyId(activityEntity.getStrategyId());

        activityOrderEntity.setTotalCount(activityCountEntity.getTotalCount());
        activityOrderEntity.setMonthCount(activityCountEntity.getMonthCount());
        activityOrderEntity.setDayCount(activityCountEntity.getDayCount());

        // 公司里一般会有专门的雪花算法UUID服务，我们这里直接生成个12位就可以了。
        activityOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        activityOrderEntity.setOrderTime(new Date());
        activityOrderEntity.setState(OrderState.completed.getCode());

        CreateOrderAggregate createOrderAggregate = new CreateOrderAggregate();
        createOrderAggregate.setActivityOrderEntity(activityOrderEntity);
        return createOrderAggregate;
    }
}
