package re.yuugu.hzx.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.acitivity.model.entity.UpdateGachaActivityOrderEntity;
import re.yuugu.hzx.domain.acitivity.service.quota.IGachaActivityQuotaOrder;
import re.yuugu.hzx.domain.credit.event.CreditPaySuccessEvent;
import re.yuugu.hzx.types.event.BaseEvent;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description CreditPaySuccessEnLis
 * @ create 2026/1/29 23:43
 */
@Slf4j
@Component
public class CreditPaySuccessEnLis {
    @Resource
    private IGachaActivityQuotaOrder gachaActivityQuotaOrder;

    @RabbitListener(queuesToDeclare = @Queue(value = "credit_pay_success"))
    public void listen(String msg) {
        try{
            log.info("监听积分支付成功消息,{}", msg);
            BaseEvent.EventMessage<CreditPaySuccessEvent.ActivityOrderEvent> eventMessage = JSON.parseObject(msg, new TypeReference<BaseEvent.EventMessage<CreditPaySuccessEvent.ActivityOrderEvent>>() {
            }.getType());
            CreditPaySuccessEvent.ActivityOrderEvent successMsg = eventMessage.getData();

            //变更活动订单状态
            UpdateGachaActivityOrderEntity updateGachaActivityOrderEntity = new UpdateGachaActivityOrderEntity();
            updateGachaActivityOrderEntity.setOutBusinessNo(successMsg.getOutBusinessNo());
            updateGachaActivityOrderEntity.setUserId(successMsg.getUserId());
            gachaActivityQuotaOrder.updateGachaActivityOrderState(updateGachaActivityOrderEntity);
        }catch (Exception e){
            log.error("监听积分支付成功消息，进行交易商品发货失败 message: {}", msg, e);
            throw e;
        }

    }
}
