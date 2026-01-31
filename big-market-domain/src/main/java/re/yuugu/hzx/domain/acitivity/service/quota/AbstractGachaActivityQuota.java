package re.yuugu.hzx.domain.acitivity.service.quota;

import lombok.extern.slf4j.Slf4j;
import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateSkuOrderAggregate;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityChargeEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityCountEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivitySkuEntity;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;
import re.yuugu.hzx.domain.acitivity.service.quota.chain.IActionChain;
import re.yuugu.hzx.domain.acitivity.service.quota.chain.factory.DefaultActionChainFactory;
import re.yuugu.hzx.domain.acitivity.service.quota.trade.ITradePolicy;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import java.util.Map;

/**
 * @ author anon
 * @ description AbstractGachaActivity
 * @ create 2026/1/4 19:10
 */
@Slf4j
public abstract class AbstractGachaActivityQuota implements IGachaActivityQuotaOrder, IActivityQuotaSkuStock {
    protected IActivityRepository activityRepository;
    protected DefaultActionChainFactory defaultActionChainFactory;
    protected Map<String, ITradePolicy> tradePolicyGroup;

    public AbstractGachaActivityQuota(IActivityRepository activityRepository, DefaultActionChainFactory defaultActionChainFactory, Map<String, ITradePolicy> tradePolicyGroup) {
        this.activityRepository = activityRepository;
        this.defaultActionChainFactory = defaultActionChainFactory;
        this.tradePolicyGroup = tradePolicyGroup;
    }

    @Override
    public String createGachaActivityOrder(ActivityChargeEntity activityChargeEntity){
        //0. 检查参数
        String userId = activityChargeEntity.getUserId();
        String outBusinessNo = activityChargeEntity.getOutBusinessNo();
        Long sku = activityChargeEntity.getSku();
        if(userId==null||outBusinessNo==null||sku==null){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        //1. 查询基础信息
        //1.1 获得sku实体
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityChargeEntity.getSku());
        //1.2 获得活动详情
        ActivityEntity activityEntity = activityRepository.queryActivityById(activitySkuEntity.getActivityId());
        //1.3 查询活动次数信息（用户可在此sku上获得的抽奖次数）
        ActivityCountEntity activityCountEntity = activityRepository.queryActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        //2. 使用责任链对活动规则进行过滤
        IActionChain actionChain = defaultActionChainFactory.openActionChain();
        boolean status = actionChain.action(activitySkuEntity,activityEntity,activityCountEntity);
        if(!status){
            log.info("活动规则责任链过滤失败");
            return null;
        }
        //3. 构建聚合对象
        CreateSkuOrderAggregate createSkuOrderAggregate = aggregateOrder(activityChargeEntity,activitySkuEntity,activityEntity,activityCountEntity);

        //4. 调用交易策略
        ITradePolicy policy = tradePolicyGroup.get(activityChargeEntity.getTradePolicy().getCode());
        if(policy==null){
            throw new RuntimeException("未找到对应的交易策略: "+activityChargeEntity.getTradePolicy());
        }
        policy.tradeAsPolicy(createSkuOrderAggregate);
        return createSkuOrderAggregate.getActivityOrderEntity().getOrderId();
    }


    protected abstract CreateSkuOrderAggregate aggregateOrder(ActivityChargeEntity activityChargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);

}
