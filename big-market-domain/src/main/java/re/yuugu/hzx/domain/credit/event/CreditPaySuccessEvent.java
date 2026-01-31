package re.yuugu.hzx.domain.credit.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.types.event.BaseEvent;

import java.util.Date;

/**
 * @ author anon
 * @ description CreditPaySuccessEvent
 * @ create 2026/1/29 21:36
 */
@Component
public class CreditPaySuccessEvent  extends BaseEvent<CreditPaySuccessEvent.ActivityOrderEvent> {
    @Value("${spring.rabbitmq.topic.credit_pay_success}")
    private String topic;

    @Override
    public EventMessage<ActivityOrderEvent> buildEventMessage(ActivityOrderEvent data) {
        return BaseEvent.EventMessage.<CreditPaySuccessEvent.ActivityOrderEvent>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timeStamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ActivityOrderEvent{
        String userId;
        String outBusinessNo;
    }
}
