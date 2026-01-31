package re.yuugu.hzx.domain.credit.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.credit.event.CreditPaySuccessEvent;
import re.yuugu.hzx.domain.credit.model.aggregate.CreateCreditAdjustAggregate;
import re.yuugu.hzx.domain.credit.model.entity.CreditAdjustEntity;
import re.yuugu.hzx.domain.credit.model.entity.TradeEntity;
import re.yuugu.hzx.domain.credit.model.entity.UserCreditOrderEntity;
import re.yuugu.hzx.domain.credit.repository.ICreditRepository;
import re.yuugu.hzx.types.event.BaseEvent;
import re.yuugu.hzx.types.event.EventTask;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description CreditAdjustService
 * @ create 2026/1/28 14:42
 */
@Service
public class CreditAdjustService implements ICreditAdjust {
    @Resource
    private ICreditRepository creditRepository;
    @Resource
    private CreditPaySuccessEvent creditPaySuccessEvent;

    @Override
    public String createCreditPayOrder(TradeEntity tradeEntity) {
        //1. 积分调额对象
        CreditAdjustEntity creditAdjustEntity = CreditAdjustEntity.builder()
                .userId(tradeEntity.getUserId())
                .adjustAmount(tradeEntity.getTradeAmount())
                .build();
        //2. 积分订单对象
        UserCreditOrderEntity userCreditOrderEntity = UserCreditOrderEntity.builder()
                .userId(tradeEntity.getUserId())
                .tradeName(tradeEntity.getTradeName())
                .tradeType(tradeEntity.getTradeType())
                .tradeAmount(tradeEntity.getTradeAmount())
                .outBusinessNo(tradeEntity.getOutBusinessNo())
                .orderId(RandomStringUtils.randomNumeric(12))
                .build();
        //3. 消息对象
        CreditPaySuccessEvent.ActivityOrderEvent msg = new CreditPaySuccessEvent.ActivityOrderEvent();
        msg.setOutBusinessNo(tradeEntity.getOutBusinessNo());
        msg.setUserId(tradeEntity.getUserId());
        //4. mq消息对象
        BaseEvent.EventMessage<CreditPaySuccessEvent.ActivityOrderEvent> mqMsg = creditPaySuccessEvent.buildEventMessage(msg);
        //5. task
        EventTask<CreditPaySuccessEvent.ActivityOrderEvent> eventTask = new EventTask<>(mqMsg,tradeEntity.getUserId(),creditPaySuccessEvent.topic());
        //6. 聚合对象
        CreateCreditAdjustAggregate createCreditAdjustAggregate = new CreateCreditAdjustAggregate();
        createCreditAdjustAggregate.setUserId(tradeEntity.getUserId());
        createCreditAdjustAggregate.setCreditAdjustEntity(creditAdjustEntity);
        createCreditAdjustAggregate.setUserCreditOrderEntity(userCreditOrderEntity);
        createCreditAdjustAggregate.setEventTask(eventTask);
        creditRepository.doSaveCreditPayOrderAggregate(createCreditAdjustAggregate);
        return userCreditOrderEntity.getOrderId();
    }

    @Override
    public String adjustCredit(TradeEntity tradeEntity) {
        //1. 积分调额对象
        CreditAdjustEntity creditAdjustEntity = CreditAdjustEntity.builder()
                .userId(tradeEntity.getUserId())
                .adjustAmount(tradeEntity.getTradeAmount())
                .build();
        //2. 积分订单对象
        UserCreditOrderEntity userCreditOrderEntity = UserCreditOrderEntity.builder()
                .userId(tradeEntity.getUserId())
                .tradeName(tradeEntity.getTradeName())
                .tradeType(tradeEntity.getTradeType())
                .tradeAmount(tradeEntity.getTradeAmount())
                .outBusinessNo(tradeEntity.getOutBusinessNo())
                .orderId(RandomStringUtils.randomNumeric(12))
                .build();
        //3. 聚合对象
        CreateCreditAdjustAggregate createCreditAdjustAggregate = new CreateCreditAdjustAggregate();
        createCreditAdjustAggregate.setUserId(tradeEntity.getUserId());
        createCreditAdjustAggregate.setCreditAdjustEntity(creditAdjustEntity);
        createCreditAdjustAggregate.setUserCreditOrderEntity(userCreditOrderEntity);
        creditRepository.doSaveCreditAdjustAggregate(createCreditAdjustAggregate);
        return userCreditOrderEntity.getOrderId();
    }
}
