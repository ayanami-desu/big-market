package re.yuugu.hzx.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.acitivity.service.ISkuStock;
import re.yuugu.hzx.types.event.BaseEvent;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description ActivitySkuStockEnLis
 * @ create 2026/1/9 23:45
 */
@Slf4j
@Component
public class ActivitySkuStockEnLis {
    @Resource
    private ISkuStock skuStock;

    @RabbitListener(queuesToDeclare = @Queue(value = "activity_sku_stock_zero"))
    public void listen(String msg) {
        try {
            log.info("监听库存为0消息,{}", msg);
            BaseEvent.EventMessage<Long> eventMessage = JSON.parseObject(msg, new TypeReference<BaseEvent.EventMessage<Long>>() {
            }.getType());
            Long sku = eventMessage.getData();
            skuStock.clearSkuStock(sku);
            skuStock.clearQueueValue();
        } catch (Exception e) {
            log.error("监听到消息:{};错误:{}", msg, JSON.toJSONString(e));
            throw e;
        }
    }
}
