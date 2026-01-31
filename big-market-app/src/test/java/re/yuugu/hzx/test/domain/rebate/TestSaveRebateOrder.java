package re.yuugu.hzx.test.domain.rebate;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import re.yuugu.hzx.domain.rebate.model.entity.RebateBehaviorEntity;
import re.yuugu.hzx.domain.rebate.model.vo.BehaviorTypeVO;
import re.yuugu.hzx.domain.rebate.service.IBehaviorRebateService;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * @ author anon
 * @ description TestSaveRebateOrder
 * @ create 2026/1/25 10:26
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSaveRebateOrder {
    @Resource
    private IBehaviorRebateService behaviorRebateService;

    @Test
    public void test_DailySignRebate() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            RebateBehaviorEntity rebateBehaviorEntity = new RebateBehaviorEntity();
            rebateBehaviorEntity.setBehaviorType(BehaviorTypeVO.DAILY_SIGN);
            rebateBehaviorEntity.setUserId("hzx");
            rebateBehaviorEntity.setOutBusinessNo("2026-01-29");

            behaviorRebateService.saveRebateOrder(rebateBehaviorEntity);
            Thread.sleep(500);
        }
        new CountDownLatch(1).await();
    }
}
