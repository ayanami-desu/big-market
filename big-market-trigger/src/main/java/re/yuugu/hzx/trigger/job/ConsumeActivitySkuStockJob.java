package re.yuugu.hzx.trigger.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.acitivity.model.vo.SkuStockKeyVO;
import re.yuugu.hzx.domain.acitivity.service.ISkuStock;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description ConsumeActivitySkuStockJob
 * @ create 2026/1/9 23:41
 */
@Slf4j
@Component
public class ConsumeActivitySkuStockJob {
    @Resource
    private ISkuStock skuStock;

    @Scheduled(cron = "*/5 * * * * ?")
    public void exec(){
        try {
            log.info("定时任务，消耗sku库存【延迟队列获取，降低对数据库的更新频次，不要产生竞争】");
            SkuStockKeyVO skuStockKeyVO = skuStock.takeQueueValue();
            if (null == skuStockKeyVO) return;
            skuStock.updateSkuStock(skuStockKeyVO.getSku());
        } catch (Exception e) {
            log.error("定时任务，sku消耗库存失败", e);
        }
    }
}
