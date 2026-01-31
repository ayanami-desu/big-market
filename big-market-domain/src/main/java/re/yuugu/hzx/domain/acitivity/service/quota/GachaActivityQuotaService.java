package re.yuugu.hzx.domain.acitivity.service.quota;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateSkuOrderAggregate;
import re.yuugu.hzx.domain.acitivity.model.entity.*;
import re.yuugu.hzx.domain.acitivity.model.vo.SkuStockKeyVO;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;
import re.yuugu.hzx.domain.acitivity.service.quota.chain.factory.DefaultActionChainFactory;
import re.yuugu.hzx.domain.acitivity.service.quota.trade.ITradePolicy;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @ author anon
 * @ description GachaActivityService
 * @ create 2026/1/4 21:31
 */
@Service
public class GachaActivityQuotaService extends AbstractGachaActivityQuota {
    @Resource
    private IActivityRepository activityRepository;

    public GachaActivityQuotaService(IActivityRepository activityRepository, DefaultActionChainFactory defaultActionChainFactory, Map<String, ITradePolicy> tradePolicyGroup) {
        super(activityRepository, defaultActionChainFactory, tradePolicyGroup);
    }


    @Override
    protected CreateSkuOrderAggregate aggregateOrder(ActivityChargeEntity activityChargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        ActivityOrderEntity activityOrderEntity = new ActivityOrderEntity();

        activityOrderEntity.setUserId(activityChargeEntity.getUserId());
        activityOrderEntity.setSku(activityChargeEntity.getSku());
        activityOrderEntity.setSkuPrice(activitySkuEntity.getSkuPrice());
        activityOrderEntity.setOutBusinessNo(activityChargeEntity.getOutBusinessNo());

        activityOrderEntity.setActivityId(activitySkuEntity.getActivityId());
        activityOrderEntity.setActivityName(activityEntity.getActivityName());
        activityOrderEntity.setStrategyId(activityEntity.getStrategyId());

        activityOrderEntity.setTotalCount(activityCountEntity.getTotalCount());
        activityOrderEntity.setMonthCount(activityCountEntity.getMonthCount());
        activityOrderEntity.setDayCount(activityCountEntity.getDayCount());

        // 公司里一般会有专门的雪花算法UUID服务，我们这里直接生成个12位就可以了。
        activityOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        activityOrderEntity.setOrderTime(new Date());

        CreateSkuOrderAggregate createSkuOrderAggregate = new CreateSkuOrderAggregate();
        createSkuOrderAggregate.setActivityOrderEntity(activityOrderEntity);
        return createSkuOrderAggregate;
    }

    @Override
    public SkuStockKeyVO takeQueueValue() {
        return activityRepository.takeQueueValue();
    }

    @Override
    public void clearQueueValue() {
        activityRepository.clearQueueValue();
    }

    @Override
    public void updateSkuStock(Long sku) {
        activityRepository.updateSkuStock(sku);
    }

    @Override
    public void clearSkuStock(Long sku) {
        activityRepository.clearSkuStock(sku);
    }

    @Override
    public void updateGachaActivityOrderState(UpdateGachaActivityOrderEntity updateGachaActivityOrderEntity) {
        activityRepository.updateGachaActivityOrderState(updateGachaActivityOrderEntity);
    }
}
