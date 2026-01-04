package re.yuugu.hzx.domain.acitivity.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import re.yuugu.hzx.domain.acitivity.model.entity.*;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;

/**
 * @ author anon
 * @ description AbstractGachaActivity
 * @ create 2026/1/4 19:10
 */
@Slf4j
public abstract class AbstractGachaActivity implements IGachaOrder {
    protected IActivityRepository activityRepository;

    public AbstractGachaActivity(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public ActivityOrderEntity createGachaActivityOrder(ActivityChargeEntity activityChargeEntity){
        //1. 获得sku实体
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityChargeEntity.getSku());

        //2. 获得活动详情
        ActivityEntity activityEntity = activityRepository.queryActivityById(activitySkuEntity.getActivityId());

        //3. 查询活动次数信息（总剩余次数）
        ActivityCountEntity activityCountEntity = activityRepository.queryActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果:{}", JSON.toJSONString(activitySkuEntity));
        log.info("查询结果:{}",JSON.toJSONString(activityEntity));
        log.info("查询结果:{}",JSON.toJSONString(activityCountEntity));

        return null;
    }
}
