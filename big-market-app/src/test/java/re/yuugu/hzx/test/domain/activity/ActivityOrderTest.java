package re.yuugu.hzx.test.domain.activity;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityChargeEntity;
import re.yuugu.hzx.domain.acitivity.service.quota.IGachaActivityQuotaOrder;
import re.yuugu.hzx.domain.acitivity.service.armory.IGachaActivityArmory;
import re.yuugu.hzx.types.exception.AppException;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

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
    private IGachaActivityQuotaOrder gachaOrder;
    @Resource
    private IGachaActivityArmory  gachaActivityArmory;

    @Before
    public void setUp() {
        boolean status = gachaActivityArmory.assembleActivity(9011L);
        log.info("装配活动,{}", status);
    }
    @Test
    public void test_saveOrder_duplicate() {
        ActivityChargeEntity activityChargeEntity = new ActivityChargeEntity();
        activityChargeEntity.setUserId("hzx");
        activityChargeEntity.setSku(9011L);
        activityChargeEntity.setBizId("h6Ylf9SKAM3Kmk5Q0c4CwGJmlULlEfZ7");
        String orderId = gachaOrder.createGachaActivityOrder(activityChargeEntity);
        log.info("orderId={}", orderId);
    }

    @Test
    public void test_saveOrder() throws InterruptedException{
        for (int i = 0; i < 30; i++) {
            try {
                ActivityChargeEntity activityChargeEntity = new ActivityChargeEntity();
                activityChargeEntity.setUserId("hzx");
                activityChargeEntity.setSku(9011L);
                activityChargeEntity.setBizId(RandomStringUtils.randomAlphabetic(16));
                String orderId = gachaOrder.createGachaActivityOrder(activityChargeEntity);
                log.info("orderId={}", orderId);
            }catch (AppException e){
                log.error(e.getInfo());
            }
        }
        // 消费队列
        new CountDownLatch(1).await();
    }
}
