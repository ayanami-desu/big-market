package re.yuugu.hzx.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import re.yuugu.hzx.infrastructure.persistent.redis.IRedisService;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description RedisTest
 * @ create 2026/1/11 15:05
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Resource
    private IRedisService redisService;

    @Test
    public void test_clear_redis() {
        redisService.setAtomicLong("big_market_test",8964);
        redisService.clearBiz("big_market_*");

        log.info("测试结果:{}", redisService.getAtomicLong("big_market_test"));
    }
}
