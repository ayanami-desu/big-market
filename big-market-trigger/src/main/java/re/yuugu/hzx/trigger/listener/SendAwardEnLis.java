package re.yuugu.hzx.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.award.event.SendAwardMessageEvent;
import re.yuugu.hzx.domain.award.model.entity.AwardDistributeEntity;
import re.yuugu.hzx.domain.award.service.distribute.AwardDistributeService;
import re.yuugu.hzx.types.event.BaseEvent;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description SendAwardEnLis
 * @ create 2026/1/27 15:09
 */
@Slf4j
@Component
public class SendAwardEnLis {
    @Resource
    private AwardDistributeService awardDistributeService;
    @RabbitListener(queuesToDeclare = @Queue(value = "send_award_message"))
    public void listen(String msg) {
        try{
            log.info("监听奖品发放消息,{}", msg);
            BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage = JSON.parseObject(msg, new TypeReference<BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage>>() {
            }.getType());
            SendAwardMessageEvent.SendAwardMessage awardMsg = eventMessage.getData();
            AwardDistributeEntity awardDistributeEntity = AwardDistributeEntity.builder()
                    .userId(awardMsg.getUserId())
                    .awardId(awardMsg.getAwardId())
                    .awardConfig(awardMsg.getAwardConfig())
                    .awardKey(awardMsg.getAwardKey())
                    .orderId(awardMsg.getOrderId())
                    .build();
            awardDistributeService.distribute(awardDistributeEntity);
        }catch (Exception e){
            log.error("监听到消息:{};错误:{}", msg, JSON.toJSONString(e));
            throw e;
        }
    }
}
