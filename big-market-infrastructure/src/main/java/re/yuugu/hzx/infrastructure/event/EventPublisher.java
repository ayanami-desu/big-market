package re.yuugu.hzx.infrastructure.event;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.types.event.BaseEvent;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description EventPublisher
 * @ create 2026/1/9 22:32
 */
@Slf4j
@Component
public class EventPublisher {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage){
        try{
            String msg = JSON.toJSONString(eventMessage);
            rabbitTemplate.convertAndSend(topic,msg);
            log.info("发送 mq 消息, msg:{}",msg);
        }catch (Exception e){
            log.error("mq消息发送失败，topic:{},message:{}",topic, JSON.toJSONString(eventMessage));
            throw e;
        }
    }
}
