package re.yuugu.hzx.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import re.yuugu.hzx.api.IGachaStrategyService;
import re.yuugu.hzx.api.dto.request.RandomGachaReq;
import re.yuugu.hzx.api.dto.request.StrategyAwardReq;
import re.yuugu.hzx.api.dto.response.RandomGachaAwardRes;
import re.yuugu.hzx.api.dto.response.StrategyAwardRes;
import re.yuugu.hzx.domain.strategy.model.entity.GachaAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.GachaFactorEntity;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyAwardEntity;
import re.yuugu.hzx.domain.strategy.service.IGachaStrategyAward;
import re.yuugu.hzx.domain.strategy.service.IGachaStrategy;
import re.yuugu.hzx.domain.strategy.service.armory.IStrategyArmory;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.model.Response;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ author anon
 * @ description StrategyGachaController
 * @ create 2026/1/1 15:06
 */
@RestController()
@Slf4j
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/gacha/strategy")
public class StrategyGachaController implements IGachaStrategyService {
    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IGachaStrategyAward gachaStrategyAward;
    @Resource
    private IGachaStrategy gachaStrategy;

    @Override
    @RequestMapping(value = "armory_strategy", method = RequestMethod.POST)
    public Response<Boolean> armoryStrategy(@RequestBody Long strategyId) {
        try {
            boolean status = strategyArmory.assembleLotteryStrategy(strategyId);
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
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @RequestMapping(value = "query_strategy_award_list", method = RequestMethod.POST)
    public Response<List<StrategyAwardRes>> queryStrategyAwardList(@RequestBody StrategyAwardReq strategyAwardReq) {
        try {
            List<StrategyAwardEntity> strategyAwardEntities = gachaStrategyAward.queryStrategyAwardList(strategyAwardReq.getStrategyId());
            List<StrategyAwardRes> strategyAwardRes = new ArrayList<>();
            for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
                strategyAwardRes.add(StrategyAwardRes.builder()
                        .awardId(strategyAwardEntity.getAwardId())
                        .awardTitle(strategyAwardEntity.getAwardTitle())
                        .awardSubtitle(strategyAwardEntity.getAwardSubtitle())
                        .sort(strategyAwardEntity.getSort())
                        .build());
            }
            return Response.<List<StrategyAwardRes>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(strategyAwardRes)
                    .build();
        } catch (Exception e) {
            log.error("查询抽奖奖品列表配置失败 strategyId：{}", strategyAwardReq.getStrategyId(), e);
            return Response.<List<StrategyAwardRes>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @Override
    @RequestMapping(value = "random_gacha", method = RequestMethod.POST)
    public Response<RandomGachaAwardRes> randomGacha(@RequestBody RandomGachaReq randomGachaReq) {
        try {
            GachaAwardEntity award = gachaStrategy.performGacha( GachaFactorEntity.builder()
                    .strategyId(randomGachaReq.getStrategyId())
                    .userId(randomGachaReq.getUserId())
                    .build());
            return Response.<RandomGachaAwardRes>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(RandomGachaAwardRes.builder()
                            .awardId(award.getAwardId())
                            .build())
                    .build();
        } catch (Exception e) {
            log.error("随机抽奖失败:{}",randomGachaReq.getStrategyId(), e);
            return Response.<RandomGachaAwardRes>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
