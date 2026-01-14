package re.yuugu.hzx.domain.acitivity.service.partake;

import lombok.extern.slf4j.Slf4j;
import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateGachaOrderAggregate;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityPartakeEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.UserGachaOrderEntity;
import re.yuugu.hzx.domain.acitivity.model.vo.ActivityStateVO;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import java.util.Date;
import java.util.List;

/**
 * @ author anon
 * @ description AbstractGachaActivityPartake
 * @ create 2026/1/11 17:09
 */
@Slf4j
public abstract class AbstractGachaActivityPartake implements IGachaActivityPartake {
    protected IActivityRepository activityRepository;

    public AbstractGachaActivityPartake(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public UserGachaOrderEntity createGachaActivityPartakeOrder(ActivityPartakeEntity activityPartakeEntity) {
        Date currentTime = new Date();
        String userId = activityPartakeEntity.getUserId();
        Long activityId = activityPartakeEntity.getActivityId();

        //1. 活动状态校验
        ActivityEntity activityEntity = activityRepository.queryActivityById(activityPartakeEntity.getActivityId());
        if (!ActivityStateVO.opening.getCode().equals(activityEntity.getState())) {
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERR.getCode(), ResponseCode.ACTIVITY_STATE_ERR.getInfo());
        }

        if (currentTime.before(activityEntity.getBeginDateTime()) || currentTime.after(activityEntity.getEndDateTime())) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERR.getCode(), ResponseCode.ACTIVITY_DATE_ERR.getInfo());
        }
        //2. 是否存在未使用订单
        List<UserGachaOrderEntity> userGachaOrderEntities = activityRepository.queryNoUsedGachaOrder(userId, activityId);
        if (userGachaOrderEntities != null && !userGachaOrderEntities.isEmpty()) {
            log.info("存在未使用的订单");
            return userGachaOrderEntities.get(0);
        }
        //3. 判断活动账户库存，生成日、月账户，构造聚合对象
        CreateGachaOrderAggregate createGachaOrderAggregate = this.doAggregate(userId, activityId, currentTime);
        //4. 构造订单对象
        UserGachaOrderEntity userGachaOrderEntity = this.buildUserOrder(userId, activityId, currentTime);
        //5. 填充订单对象
        createGachaOrderAggregate.setUserGachaOrderEntity(userGachaOrderEntity);
        //6. 保存聚合对象，执行事务
        activityRepository.doSaveCreateGachaOrderAggregate(createGachaOrderAggregate);
        return userGachaOrderEntity;
    }

    protected abstract UserGachaOrderEntity buildUserOrder(String userId, Long activityId, Date currentTime);

    protected abstract CreateGachaOrderAggregate doAggregate(String userId, Long activityId, Date currentTime);
}
