package re.yuugu.hzx.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityChargeEntity;
import re.yuugu.hzx.domain.acitivity.model.vo.TradeOrderTypeVO;
import re.yuugu.hzx.domain.acitivity.service.quota.IGachaActivityQuotaOrder;
import re.yuugu.hzx.domain.credit.model.entity.TradeEntity;
import re.yuugu.hzx.domain.credit.model.vo.TradeNameVO;
import re.yuugu.hzx.domain.credit.model.vo.TradeTypeVO;
import re.yuugu.hzx.domain.credit.service.ICreditAdjust;
import re.yuugu.hzx.domain.rebate.event.SendRebateMessageEvent;
import re.yuugu.hzx.domain.rebate.model.vo.RebateTypeVO;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.event.BaseEvent;
import re.yuugu.hzx.types.exception.AppException;

import javax.annotation.Resource;
import java.math.BigDecimal;

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
    private ICreditAdjust creditAdjust;

    @RabbitListener(queuesToDeclare = @Queue(value = "send_rebate_message"))
    public void listen(String msg) {
        try {
            log.info("监听行为返利消息,{}", msg);
            BaseEvent.EventMessage<SendRebateMessageEvent.SendRebateMessage> eventMessage = JSON.parseObject(msg, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.SendRebateMessage>>() {
            }.getType());
            SendRebateMessageEvent.SendRebateMessage rebateMsg = eventMessage.getData();
            RebateTypeVO rebateType = RebateTypeVO.valueOf(rebateMsg.getRebateType());
            switch (rebateType) {
                case SKU:
                    ActivityChargeEntity activityChargeEntity = new ActivityChargeEntity();
                    activityChargeEntity.setUserId(rebateMsg.getUserId());
                    activityChargeEntity.setSku(Long.valueOf(rebateMsg.getRebateConfig()));
                    activityChargeEntity.setTradePolicy(TradeOrderTypeVO.rebate_no_pay_trade);
                    activityChargeEntity.setOutBusinessNo(rebateMsg.getOutBusinessNo());
                    gachaActivityQuotaOrder.createGachaActivityOrder(activityChargeEntity);
                    break;
                case POINT:
                    TradeEntity tradeEntity = new TradeEntity();
                    tradeEntity.setUserId(rebateMsg.getUserId());
                    tradeEntity.setTradeName(TradeNameVO.REBATE_CREDIT);
                    tradeEntity.setTradeType(TradeTypeVO.FORWARD);
                    tradeEntity.setTradeAmount(new BigDecimal(rebateMsg.getRebateConfig()));
                    tradeEntity.setOutBusinessNo(rebateMsg.getOutBusinessNo());
                    creditAdjust.adjustCredit(tradeEntity);
                    break;
                default:
                    throw new AppException(ResponseCode.SWITCH_NO_MATCH_OPTION.getCode(),ResponseCode.SWITCH_NO_MATCH_OPTION.getInfo());
            }
        } catch (Exception e) {
            log.error("监听到消息:{};错误:{}", msg, JSON.toJSONString(e));
            throw e;
        }
    }
}
