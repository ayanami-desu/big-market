package re.yuugu.hzx.domain.acitivity.service.armory;

import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivitySkuEntity;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description GachaActivityArmory 缓存sku剩余数量
 * @ create 2026/1/9 16:44
 */
@Service
public class GachaActivityArmory implements IGachaActivityArmory{
    @Resource
    private IActivityRepository activityRepository;
    @Override
    public boolean assembleActivity(Long sku) {
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);
        if(activitySkuEntity==null){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        activityRepository.cacheActivitySkuStockCount(sku,activitySkuEntity.getStockCountSurplus());
        //查询活动次数，缓存到redis
        activityRepository.queryActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        //查询活动，缓存到redis
        activityRepository.queryActivityById(activitySkuEntity.getActivityId());
        return false;
    }
}
