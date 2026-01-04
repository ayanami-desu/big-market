package re.yuugu.hzx.test.domain.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import re.yuugu.hzx.domain.strategy.service.armory.IStrategyArmory;


import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyArmoryTest {

    @Resource
    private IStrategyArmory strategyArmory;

    @Before
    public void test_assembleLotteryStrategy() {
        if (!strategyArmory.assembleLotteryStrategy(100001L)){
            throw new RuntimeException();
        }
    }
    @Test
    public void test_getRandomAwardId() {
        int times=10;
        for(int i=0;i<times;i++){
            log.info("抽奖结果:{}",strategyArmory.getRandomAwardId(String.valueOf(100001L)));
        }
        for(int i=0;i<times;i++){
            log.info("抽奖结果:{}",strategyArmory.getRandomAwardId(String.valueOf(100001L),"4000:102,103,104,105,106,107,108,109"));
        }
        for(int i=0;i<times;i++){
            log.info("抽奖结果:{}",strategyArmory.getRandomAwardId(String.valueOf(100001L),"5000:103,104,105,106,107,108,109"));
        }
        for(int i=0;i<times;i++){
            log.info("抽奖结果:{}",strategyArmory.getRandomAwardId(String.valueOf(100001L),"6000:104,105,106,107,108,109"));
        }
    }
}
