package re.yuugu.hzx.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import re.yuugu.hzx.api.IGachaActivityService;
import re.yuugu.hzx.api.dto.request.ActivityDailySignReq;
import re.yuugu.hzx.api.dto.request.ActivityDrawReq;
import re.yuugu.hzx.api.dto.response.ActivityDrawRes;
import re.yuugu.hzx.api.response.Response;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityPartakeEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.UserGachaOrderEntity;
import re.yuugu.hzx.domain.acitivity.service.armory.IGachaActivityArmory;
import re.yuugu.hzx.domain.acitivity.service.partake.IGachaActivityPartake;
import re.yuugu.hzx.domain.award.model.entity.UserAwardRecordEntity;
import re.yuugu.hzx.domain.award.model.vo.AwardStateVO;
import re.yuugu.hzx.domain.award.service.IUserAwardRecordService;
import re.yuugu.hzx.domain.rebate.model.entity.RebateBehaviorEntity;
import re.yuugu.hzx.domain.rebate.model.entity.RebateOrderEntity;
import re.yuugu.hzx.domain.rebate.model.vo.BehaviorTypeVO;
import re.yuugu.hzx.domain.rebate.service.IBehaviorRebateService;
import re.yuugu.hzx.domain.strategy.model.entity.GachaAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.GachaFactorEntity;
import re.yuugu.hzx.domain.strategy.service.IGachaStrategy;
import re.yuugu.hzx.domain.strategy.service.armory.IStrategyArmory;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ author anon
 * @ description GachaController
 * @ create 2026/1/19 16:23
 */
@RestController()
@Slf4j
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/gacha/activity")
public class ActivityGachaController implements IGachaActivityService {
    @Resource
    private IGachaActivityArmory activityArmory;
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IGachaActivityPartake activityPartake;
    @Resource
    private IGachaStrategy gachaStrategy;
    @Resource
    private IUserAwardRecordService userAwardRecordService;
    @Resource
    private IBehaviorRebateService behaviorRebateService;

    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    @RequestMapping(value = "armory_activity", method = RequestMethod.POST)
    public Response<Boolean> armoryActivity(@RequestBody Long activityId) {
        try {
            log.info("armoryActivity,activityId:{}", activityId);
            boolean status = activityArmory.assembleActivityByActivityId(activityId);
            status = status && strategyArmory.assembleStrategyByActivityId(activityId);
            if (status) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(true)
                        .build();
            }
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        } catch (Exception e) {
            log.error("活动，策略，装配失败:{}", e.getMessage());
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @RequestMapping(value = "activity_draw", method = RequestMethod.POST)
    public Response<ActivityDrawRes> activityDrawAward(@RequestBody ActivityDrawReq activityDrawReq) {
        try {
            //1. 参数校验
            String userId = activityDrawReq.getUserId();
            Long activityId = activityDrawReq.getActivityId();
            log.info("activityDrawAward: userId:{},activityId:{}", userId, activityId);
            if (StringUtils.isBlank(userId) || activityId == null) {
                return Response.<ActivityDrawRes>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }
            //2. 创建抽奖订单
            UserGachaOrderEntity userGachaOrderEntity = activityPartake.createGachaActivityPartakeOrder(ActivityPartakeEntity.builder()
                    .userId(userId)
                    .activityId(activityId)
                    .build());

            //3. 执行抽奖
            GachaAwardEntity gachaAwardEntity = gachaStrategy.performGacha(GachaFactorEntity.builder()
                    .userId(userId)
                    .strategyId(userGachaOrderEntity.getStrategyId())
                    .build());

            //4. 写入中奖记录
            UserAwardRecordEntity userAwardRecordEntity = UserAwardRecordEntity.builder()
                    .userId(userId)
                    .activityId(activityId)
                    .strategyId(userGachaOrderEntity.getStrategyId())
                    .awardId(gachaAwardEntity.getAwardId())
                    .orderId(userGachaOrderEntity.getOrderId())
                    .awardTitle(gachaAwardEntity.getAwardTitle())
                    .awardTime(new Date())
                    .awardState(AwardStateVO.create)
                    .awardConfig(gachaAwardEntity.getAwardConfig())
                    .awardKey(gachaAwardEntity.getAwardKey())
                    .build();
            userAwardRecordService.saveUserAwardRecord(userAwardRecordEntity);
            return Response.<ActivityDrawRes>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(ActivityDrawRes.builder()
                            .awardId(gachaAwardEntity.getAwardId())
                            .build())
                    .build();
        } catch (AppException e) {
            log.error("活动抽奖失败", e);
            return Response.<ActivityDrawRes>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("活动抽奖失败,未知错误:{}", e.getMessage());
            return Response.<ActivityDrawRes>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "activity_daily_sign", method = RequestMethod.POST)
    @Override
    public Response<Boolean> activityDailySign(@RequestBody ActivityDailySignReq activityDailySignReq) {
        String userId = activityDailySignReq.getUserId();
        if(userId==null||StringUtils.isEmpty(userId)){
            return Response.<Boolean>builder()
                    .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                    .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                    .build();
        }
        try{
            RebateBehaviorEntity rebateBehaviorEntity =  new RebateBehaviorEntity();
            rebateBehaviorEntity.setBehaviorType(BehaviorTypeVO.DAILY_SIGN);
            rebateBehaviorEntity.setUserId(userId);
            rebateBehaviorEntity.setOutBusinessNo(dateFormatDay.format(new Date()));
            behaviorRebateService.saveRebateOrder(rebateBehaviorEntity);
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
        } catch (Exception e) {
            log.error("活动每日签到错误, userId:{}",userId,e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "activity_has_daily_sign", method = RequestMethod.POST)
    @Override
    public Response<Boolean> hasActivityDailySign(@RequestBody ActivityDailySignReq activityDailySignReq) {
        String userId = activityDailySignReq.getUserId();
        try {
            log.info("查询用户是否完成日历签到返利开始 userId:{}", userId);
            String outBusinessNo = dateFormatDay.format(new Date());
            List<RebateOrderEntity> rebateOrderEntities = behaviorRebateService.queryOrderByOutBusinessNo(userId, outBusinessNo);
            log.info("查询用户是否完成日历签到返利完成 userId:{} orders.size:{}", userId, rebateOrderEntities.size());
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(!rebateOrderEntities.isEmpty()) // 只要不为空，则表示已经做了签到
                    .build();
        } catch (Exception e) {
            log.error("查询用户是否完成日历签到返利失败 userId:{}", userId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }
}
