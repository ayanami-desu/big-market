package re.yuugu.hzx.domain.acitivity.service.partake;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.acitivity.model.aggregate.CreateGachaOrderAggregate;
import re.yuugu.hzx.domain.acitivity.model.entity.*;
import re.yuugu.hzx.domain.acitivity.model.vo.ActivityPartakeOrderState;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ author anon
 * @ description GachaActivityPartakeService
 * @ create 2026/1/11 17:09
 */
@Service
@Slf4j
public class GachaActivityPartakeService extends AbstractGachaActivityPartake{
    private final SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    public GachaActivityPartakeService(IActivityRepository activityRepository) {
        super(activityRepository);
    }

    @Override
    protected CreateGachaOrderAggregate doAggregate(String userId, Long activityId, Date currentTime) {
        ActivityAccountEntity activityAccountEntity = activityRepository.queryActivityAccountById(userId,activityId);
        if( activityAccountEntity==null || activityAccountEntity.getTotalCountSurplus()<=0){
            log.warn("活动账户不存在或活动账户库存不足");
            throw new AppException(ResponseCode.ACTIVITY_PARTAKE_STOCK_ERR.getCode(),ResponseCode.ACTIVITY_PARTAKE_STOCK_ERR.getInfo());
        }
        String month = dateFormatMonth.format(currentTime);
        String day = dateFormatDay.format(currentTime);
        CreateGachaOrderAggregate orderAggregate = new CreateGachaOrderAggregate();
        orderAggregate.setActivityId(activityId);
        orderAggregate.setUserId(userId);
        orderAggregate.setActivityAccountEntity(activityAccountEntity);

        ActivityAccountDayEntity activityAccountDayEntity = activityRepository.queryActivityAccountDayById(userId,activityId,day);
        if(activityAccountDayEntity==null){
            orderAggregate.setDayAccountExist(false);
            activityAccountDayEntity =  new ActivityAccountDayEntity();
            activityAccountDayEntity.setActivityId(activityId);
            activityAccountDayEntity.setUserId(userId);
            activityAccountDayEntity.setDay(day);
            activityAccountDayEntity.setDayCount(activityAccountEntity.getDayCount());
            activityAccountDayEntity.setDayCountSurplus(activityAccountEntity.getDayCountSurplus());
        }
        if(activityAccountDayEntity.getDayCountSurplus()<=0){
            throw new AppException(ResponseCode.ACTIVITY_PARTAKE_STOCK_DAY_ERR.getCode(),ResponseCode.ACTIVITY_PARTAKE_STOCK_DAY_ERR.getInfo());
        }
        orderAggregate.setActivityAccountDayEntity(activityAccountDayEntity);

        ActivityAccountMonthEntity activityAccountMonthEntity = activityRepository.queryActivityAccountMonthById(userId,activityId,month);
        if(activityAccountMonthEntity==null){
            orderAggregate.setMonthAccountExist(false);
            activityAccountMonthEntity =  new ActivityAccountMonthEntity();
            activityAccountMonthEntity.setActivityId(activityId);
            activityAccountMonthEntity.setUserId(userId);
            activityAccountMonthEntity.setMonth(month);
            activityAccountMonthEntity.setMonthCount(activityAccountEntity.getMonthCount());
            activityAccountMonthEntity.setMonthCountSurplus(activityAccountEntity.getMonthCountSurplus());
        }
        if(activityAccountMonthEntity.getMonthCountSurplus()<=0){
            throw new AppException(ResponseCode.ACTIVITY_PARTAKE_STOCK_MONTH_ERR.getCode(),ResponseCode.ACTIVITY_PARTAKE_STOCK_MONTH_ERR.getInfo());
        }
        orderAggregate.setActivityAccountMonthEntity(activityAccountMonthEntity);

        return orderAggregate;
    }
    @Override
    protected UserGachaOrderEntity buildUserOrder(String userId, Long activityId, Date currentTime) {
        ActivityEntity activityEntity = activityRepository.queryActivityById(activityId);

        UserGachaOrderEntity userGachaOrderEntity = new UserGachaOrderEntity();
        userGachaOrderEntity.setUserId(userId);
        userGachaOrderEntity.setActivityId(activityId);
        userGachaOrderEntity.setOrderTime(currentTime);
        userGachaOrderEntity.setActivityName(activityEntity.getActivityName());
        userGachaOrderEntity.setOrderState(ActivityPartakeOrderState.create);
        userGachaOrderEntity.setStrategyId(activityEntity.getStrategyId());
        userGachaOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        return userGachaOrderEntity;
    }
}
