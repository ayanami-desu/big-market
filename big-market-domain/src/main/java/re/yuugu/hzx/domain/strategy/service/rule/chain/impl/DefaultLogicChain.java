package re.yuugu.hzx.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.strategy.service.armory.IStrategyArmory;
import re.yuugu.hzx.domain.strategy.service.rule.chain.AbstractLoginChain;

import javax.annotation.Resource;


@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLoginChain {

    @Resource
    private IStrategyArmory  strategyArmory;
    @Override
    public Integer logic(Long strategyId, String userId) {
        log.info("责任链-默认处理");
        return strategyArmory.getRandomAwardId(String.valueOf(strategyId));
    }

    @Override
    protected String ruleModel() {
        return "default";
    }
}
