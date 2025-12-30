package re.yuugu.hzx.test.infrastructure;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import re.yuugu.hzx.domain.strategy.model.vo.RuleTreeVO;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description RuleTreeTest
 * @ create 2025/12/29 16:51
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleTreeTest {

    @Resource
    private IStrategyRepository strategyRepository;

    @Test
    public void test_queryRuleTreeByTreeId(){
        RuleTreeVO tree = strategyRepository.queryRuleTreeByRootNode("rule_lock");
        log.info("result:{}", JSON.toJSONString(tree));
    }

}
