package re.yuugu.hzx.test.domain.activity;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityChargeEntity;
import re.yuugu.hzx.domain.acitivity.model.vo.TradeOrderTypeVO;
import re.yuugu.hzx.domain.acitivity.service.armory.IGachaActivityArmory;
import re.yuugu.hzx.domain.acitivity.service.quota.IGachaActivityQuotaOrder;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description TradePolicyTest
 * @ create 2026/1/29 23:09
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradePolicyTest {
    @Resource
    private IGachaActivityQuotaOrder gachaOrder;
    @Resource
    private IGachaActivityArmory gachaActivityArmory;

    @Test
    public void setUp() {
        boolean status = gachaActivityArmory.assembleActivitySku(9011L);
        log.info("装配活动,{}", status);
    }
    @Test
    public void test_activityOrderRebateNoPay() {
        ActivityChargeEntity activityChargeEntity = new ActivityChargeEntity();
        activityChargeEntity.setUserId("hzx");
        activityChargeEntity.setSku(9011L);
        activityChargeEntity.setTradePolicy(TradeOrderTypeVO.rebate_no_pay_trade);
        activityChargeEntity.setOutBusinessNo("hzx_SKU_2026-01-28");
        String orderId = gachaOrder.createGachaActivityOrder(activityChargeEntity);
        log.info("orderId={}", orderId);
    }
    @Test
    public void test_activityOrderCreditPay() {
        ActivityChargeEntity activityChargeEntity = new ActivityChargeEntity();
        activityChargeEntity.setUserId("hzx");
        activityChargeEntity.setSku(9011L);
        activityChargeEntity.setTradePolicy(TradeOrderTypeVO.credit_pay_trade);
        activityChargeEntity.setOutBusinessNo("h6Ylf9SKAM3Kmk5Q0c4CwGJmlULlEfZ7");
        String orderId = gachaOrder.createGachaActivityOrder(activityChargeEntity);
        log.info("orderId={}", orderId);
    }
}
