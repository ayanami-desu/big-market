package re.yuugu.hzx.test.domain.activity;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityChargeEntity;
import re.yuugu.hzx.domain.acitivity.service.quota.IGachaActivityQuotaOrder;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description RepoTest
 * @ create 2026/1/4 21:33
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RepoTest {
    @Resource
    private IGachaActivityQuotaOrder gachaOrder;

    @Test
    public void test_repository() {
        ActivityChargeEntity activityChargeEntity = new ActivityChargeEntity();
        activityChargeEntity.setSku(9011L);
        activityChargeEntity.setUserId("hzx");
        gachaOrder.createGachaActivityOrder(activityChargeEntity);
    }
}
