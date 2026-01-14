package re.yuugu.hzx.domain.acitivity.service.quota.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.acitivity.dispatch.IGachaDispatch;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityCountEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivitySkuEntity;
import re.yuugu.hzx.domain.acitivity.model.vo.SkuStockKeyVO;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;
import re.yuugu.hzx.domain.acitivity.service.quota.chain.AbstractActionChain;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description ActivitySkuStockActionChain
 * @ create 2026/1/5 00:56
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {
    @Resource
    private IGachaDispatch gachaDispatch;
    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("activity_sku_stock_action, 扣减sku库存");
        boolean status = gachaDispatch.subtractActivitySkuStock(activitySkuEntity.getSku(),activityEntity.getEndDateTime());
        if (!status) {
            // 库存扣减失败
            log.info("库存扣减失败");
            throw new AppException(ResponseCode.ACTIVITY_STOCK_ERR.getCode(), ResponseCode.ACTIVITY_STOCK_ERR.getInfo());
        }
        // 发送库存消费消息
        activityRepository.sendConsumeSkuStock(SkuStockKeyVO.builder()
                .sku(activitySkuEntity.getSku())
                .activityId(activitySkuEntity.getActivityId())
                .build());
        return getNext().action(activitySkuEntity, activityEntity, activityCountEntity);
    }
}
