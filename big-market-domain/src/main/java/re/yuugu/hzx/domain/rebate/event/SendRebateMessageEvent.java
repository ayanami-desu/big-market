package re.yuugu.hzx.domain.rebate.event;

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
 * @ description SendRebateMessageEvent
 * @ create 2026/1/24 16:57
 */
@Component
public class SendRebateMessageEvent extends BaseEvent<SendRebateMessageEvent.SendRebateMessage> {
    @Value("${spring.rabbitmq.topic.send_rebate_message}")
    private String topic;

    @Override
    public BaseEvent.EventMessage<SendRebateMessageEvent.SendRebateMessage> buildEventMessage(SendRebateMessageEvent.SendRebateMessage data) {
        return BaseEvent.EventMessage.<SendRebateMessageEvent.SendRebateMessage>builder()
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
    public static class SendRebateMessage {
        /**
         * 用户 id
         */
        private String userId;
        /**
         * 行为类型（sign 签到、openai_pay 支付）
         */
        private String behaviorType;

        /**
         * 返利类型（sku 活动库存充值商品、integral 用户活动积分）
         */
        private String rebateType;

        /**
         * 返利配置【sku值，积分值】
         */
        private String rebateConfig;
        /**
         * 业务 id
         */
        private String outBusinessNo;
    }
}
