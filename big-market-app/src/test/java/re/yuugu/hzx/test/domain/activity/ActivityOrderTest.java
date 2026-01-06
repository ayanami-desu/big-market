package re.yuugu.hzx.test.domain.activity;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityChargeEntity;
import re.yuugu.hzx.domain.acitivity.service.IGachaOrder;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description ActivityOrderTest
 * @ create 2026/1/5 22:09
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityOrderTest {
    @Resource
    private IGachaOrder gachaOrder;

    @Test
    public void test_saveOrder() {
        ActivityChargeEntity activityChargeEntity = new ActivityChargeEntity();
        activityChargeEntity.setUserId("hzx");
        activityChargeEntity.setSku(9011L);
        activityChargeEntity.setBizId("h6Ylf9SKAM3Kmk5Q0c4CwGJmlULlEfZ7");
        String orderId = gachaOrder.createGachaActivityOrder(activityChargeEntity);
        log.info("orderId={}", orderId);
    }
}
