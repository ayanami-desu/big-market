package re.yuugu.hzx.domain.acitivity.event;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.types.event.BaseEvent;

import java.util.Date;

/**
 * @ author anon
 * @ description ActivitySkuStockZeroEvent
 * @ create 2026/1/9 22:48
 */
@Component
public class ActivitySkuStockZeroEvent extends BaseEvent<Long> {
    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    private String topic;

    @Override
    public EventMessage<Long> buildEventMessage(Long sku) {
        return EventMessage.<Long>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .data(sku)
                .timeStamp(new Date())
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }
}
