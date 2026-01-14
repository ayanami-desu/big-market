package re.yuugu.hzx.test.domain.activity;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityPartakeEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.UserGachaOrderEntity;
import re.yuugu.hzx.domain.acitivity.service.partake.IGachaActivityPartake;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description PartakeTest
 * @ create 2026/1/14 21:31
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PartakeTest {
    @Resource
    private IGachaActivityPartake  gachaActivityPartake;

    @Test
    public void test_partake_order(){
        ActivityPartakeEntity activityPartakeEntity = new ActivityPartakeEntity();
        activityPartakeEntity.setActivityId(100301L);
        activityPartakeEntity.setUserId("hzx");
        UserGachaOrderEntity userGachaOrderEntity = gachaActivityPartake.createGachaActivityPartakeOrder(activityPartakeEntity);
        log.info("userGachaOrderEntity={}", JSON.toJSONString(userGachaOrderEntity));
    }

}
