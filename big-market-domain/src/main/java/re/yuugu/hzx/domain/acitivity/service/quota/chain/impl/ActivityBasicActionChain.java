package re.yuugu.hzx.domain.acitivity.service.quota.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityCountEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivitySkuEntity;
import re.yuugu.hzx.domain.acitivity.model.vo.ActivityStateVO;
import re.yuugu.hzx.domain.acitivity.service.quota.chain.AbstractActionChain;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import java.util.Date;

/**
 * @ author anon
 * @ description ActivityBasicActionChain
 * @ create 2026/1/5 00:55
 */
@Slf4j
@Component("activity_basic_action")
public class ActivityBasicActionChain extends AbstractActionChain {

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("activity_basic_action, 活动起始日期、状态、库存校验");
        if(!ActivityStateVO.opening.getCode().equals(activityEntity.getState())){
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERR.getCode(),ResponseCode.ACTIVITY_STATE_ERR.getInfo());
        }
        Date currentTime = new Date();
        if(currentTime.before(activityEntity.getBeginDateTime()) || currentTime.after(activityEntity.getEndDateTime())){
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERR.getCode(),ResponseCode.ACTIVITY_DATE_ERR.getInfo());
        }
        if(activitySkuEntity.getStockCountSurplus()<=0){
            throw new AppException(ResponseCode.ACTIVITY_STOCK_ERR.getCode(),ResponseCode.ACTIVITY_STOCK_ERR.getInfo());
        }
        return getNext().action(activitySkuEntity, activityEntity, activityCountEntity);
    }
}
