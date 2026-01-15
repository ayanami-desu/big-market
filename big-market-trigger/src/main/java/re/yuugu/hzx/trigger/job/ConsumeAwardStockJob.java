package re.yuugu.hzx.trigger.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.strategy.model.vo.StrategyAwardStockKeyVO;
import re.yuugu.hzx.domain.strategy.service.IGachaAwardStock;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description ConsumeAwardStock
 * @ create 2025/12/30 23:41
 */
@Slf4j
@Component
public class ConsumeAwardStockJob {
    @Resource
    private IGachaAwardStock gachaAwardStock;

    @Scheduled(cron = "*/5 * * * * ?")
    public void exec(){
        try {
            //log.info("定时任务，更新奖品消耗库存【延迟队列获取，降低对数据库的更新频次，不要产生竞争】");
            StrategyAwardStockKeyVO strategyAwardStockKeyVO = gachaAwardStock.offerStrategyAwardQueueValue();
            if (null == strategyAwardStockKeyVO) return;
            log.info("定时任务，更新奖品消耗库存 strategyId:{} awardId:{}", strategyAwardStockKeyVO.getStrategyId(), strategyAwardStockKeyVO.getAwardId());
            gachaAwardStock.updateStrategyAwardStock(strategyAwardStockKeyVO.getStrategyId(), strategyAwardStockKeyVO.getAwardId());
        } catch (Exception e) {
            log.error("定时任务，更新奖品消耗库存失败", e);
        }
    }
}
