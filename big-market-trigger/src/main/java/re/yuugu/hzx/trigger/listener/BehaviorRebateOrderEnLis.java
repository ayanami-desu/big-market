package re.yuugu.hzx.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityChargeEntity;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;
import re.yuugu.hzx.domain.acitivity.service.quota.IGachaActivityQuotaOrder;
import re.yuugu.hzx.domain.rebate.event.SendRebateMessageEvent;
import re.yuugu.hzx.domain.rebate.model.vo.RebateTypeVO;
import re.yuugu.hzx.types.event.BaseEvent;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description BehaviorRebateOrderEnLis
 * @ create 2026/1/25 11:34
 */
@Slf4j
@Component
public class BehaviorRebateOrderEnLis {
    @Resource
    private IGachaActivityQuotaOrder gachaActivityQuotaOrder;
    @Resource
    private IActivityRepository activityRepository;

    @RabbitListener(queuesToDeclare = @Queue(value = "send_rebate_message"))
    public void listen(String msg) {
        try {
            log.info("监听行为返利消息,{}", msg);
            BaseEvent.EventMessage<SendRebateMessageEvent.SendRebateMessage> eventMessage = JSON.parseObject(msg, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.SendRebateMessage>>() {
            }.getType());
            SendRebateMessageEvent.SendRebateMessage rebateMsg = eventMessage.getData();
            if(rebateMsg.getRebateType().equals(RebateTypeVO.SKU.getCode())){
                ActivityChargeEntity activityChargeEntity = new ActivityChargeEntity();
                activityChargeEntity.setUserId("hzx");
                activityChargeEntity.setSku(9011L);
                activityChargeEntity.setBizId(rebateMsg.getBizId());
                gachaActivityQuotaOrder.createGachaActivityOrder(activityChargeEntity);
            }else{
                log.info("其他rebate类型暂不处理");
            }
            //todo 处理其他rebate类型
        } catch (Exception e) {
            log.error("监听到消息:{};错误:{}", msg, JSON.toJSONString(e));
            throw e;
        }
    }
}
