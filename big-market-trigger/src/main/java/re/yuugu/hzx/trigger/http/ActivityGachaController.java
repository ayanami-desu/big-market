package re.yuugu.hzx.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import re.yuugu.hzx.api.IGachaActivityService;
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
import re.yuugu.hzx.domain.strategy.model.entity.GachaAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.GachaFactorEntity;
import re.yuugu.hzx.domain.strategy.service.IGachaStrategy;
import re.yuugu.hzx.domain.strategy.service.armory.IStrategyArmory;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Date;

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

    @Override
    @RequestMapping(value = "armory_activity", method = RequestMethod.POST)
    public Response<Boolean> armoryActivity(@RequestBody Long activityId, Principal principal) {
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
}
