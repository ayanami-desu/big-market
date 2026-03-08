package re.yuugu.hzx.test.infra;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ author hzx
 * @ description Nacos
 * @ create 2026/3/7 20:36
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@RefreshScope
public class Nacos {
    @Value("${degrade-switch.value}")
    private Boolean switchValue;

    @Test
    public void test() {
        log.info("switchValue:{}", switchValue);
        if (switchValue) {
            log.info("open");
        }else{
            log.info("close");
        }
    }
}
