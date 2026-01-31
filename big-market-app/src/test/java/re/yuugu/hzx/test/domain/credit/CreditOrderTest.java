package re.yuugu.hzx.test.domain.credit;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import re.yuugu.hzx.domain.credit.model.entity.TradeEntity;
import re.yuugu.hzx.domain.credit.model.vo.TradeNameVO;
import re.yuugu.hzx.domain.credit.model.vo.TradeTypeVO;
import re.yuugu.hzx.domain.credit.service.ICreditAdjust;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

/**
 * @ author anon
 * @ description CreditOrderTest
 * @ create 2026/1/28 15:45
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditOrderTest {
    @Resource
    private ICreditAdjust creditAdjust;

    @Test
    public void test_saveCreditOrder() throws InterruptedException {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("hzx");
        tradeEntity.setTradeName(TradeNameVO.CONVERT_TO_SKU);
        tradeEntity.setTradeType(TradeTypeVO.REVERSE);
        tradeEntity.setTradeAmount(new BigDecimal(100));
        tradeEntity.setOutBusinessNo("h6Ylf9SKAM3Kmk5Q0c4CwGJmlULlEfZ7");
        String orderId = creditAdjust.createCreditPayOrder(tradeEntity);
        log.info(orderId);
        new CountDownLatch(1).await();
    }

}
