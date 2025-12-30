package re.yuugu.hzx.test.domain;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import re.yuugu.hzx.domain.strategy.model.entity.GachaAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.GachaFactorEntity;
import re.yuugu.hzx.domain.strategy.service.IGachaStrategy;
import re.yuugu.hzx.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import re.yuugu.hzx.domain.strategy.service.rule.tree.impl.RuleLockTreeNode;
import re.yuugu.hzx.domain.strategy.service.rule.tree.impl.RuleStockTreeNode;

import javax.annotation.Resource;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GachaTest {

    @Resource
    private IGachaStrategy gachaStrategy;
    @Resource
    private RuleWeightLogicChain ruleWeightLogicChain;
    @Resource
    private RuleLockTreeNode ruleLockLogicFilter;
    @Resource
    private RuleStockTreeNode ruleStockTreeNode;

    @Before
    public void setUserPoint(){
        ReflectionTestUtils.setField(ruleWeightLogicChain,"userPoint",2000L);
    }
    @Before
    public void setUserGachaCount(){
        ReflectionTestUtils.setField(ruleLockLogicFilter,"userGachaCount",0L);
    }
    @Before
    public void setStockCount(){
        ReflectionTestUtils.setField(ruleStockTreeNode,"stockNum",0L);
    }

    @Test
    public void test_performGacha() {
        GachaFactorEntity gachaFactorEntity = new GachaFactorEntity();
        gachaFactorEntity.setStrategyId(100001L);
        gachaFactorEntity.setUserId("hzx");
        GachaAwardEntity gachaAwardEntity = gachaStrategy.performGacha(gachaFactorEntity);
        log.info("gachaFactorEntity={}", JSON.toJSONString(gachaFactorEntity) );
        log.info("gachaAwardEntity={}", JSON.toJSONString(gachaAwardEntity));
    }
    @Test
    public void test_performGacha_blacklist() {
        GachaFactorEntity gachaFactorEntity = new GachaFactorEntity();
        gachaFactorEntity.setStrategyId(100001L);
        gachaFactorEntity.setUserId("user101");
        GachaAwardEntity gachaAwardEntity = gachaStrategy.performGacha(gachaFactorEntity);
        log.info("gachaFactorEntity={}", JSON.toJSONString(gachaFactorEntity) );
        log.info("gachaAwardEntity={}", JSON.toJSONString(gachaAwardEntity));
    }
    @Test
    public void test_performGacha_lock() {
        GachaFactorEntity gachaFactorEntity = new GachaFactorEntity();
        gachaFactorEntity.setStrategyId(100001L);
        gachaFactorEntity.setUserId("hzx");
        for(int i=0;i<1;i++){
            GachaAwardEntity gachaAwardEntity = gachaStrategy.performGacha(gachaFactorEntity);
            log.info("gachaFactorEntity={}", JSON.toJSONString(gachaFactorEntity) );
            log.info("gachaAwardEntity={}", JSON.toJSONString(gachaAwardEntity));
        }
    }
}
