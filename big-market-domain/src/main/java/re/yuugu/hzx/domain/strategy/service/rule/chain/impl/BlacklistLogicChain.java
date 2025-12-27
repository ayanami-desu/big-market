package re.yuugu.hzx.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.domain.strategy.service.rule.chain.AbstractLoginChain;
import re.yuugu.hzx.types.common.Constants;

import javax.annotation.Resource;


@Slf4j
@Component("rule_blacklist")
public class BlacklistLogicChain extends AbstractLoginChain {

    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public Integer logic(Long strategyId, String userId) {
        log.info("责任链-黑名单: strategyId:{},userId:{}",strategyId,userId);
        String ruleValue = strategyRepository.queryStrategyRuleValue(strategyId,this.ruleModel(),null);
        String[] splitValue = ruleValue.split(Constants.COLON);
        Integer blacklistAwardId = Integer.valueOf(splitValue[0]);
        String[] blacklist = splitValue[1].split(Constants.COMMA);
        for(String s: blacklist){
            if (userId.equals(s)){
                log.info("责任链-黑名单:接管");
                return blacklistAwardId;
            }
        }
        log.info("责任链-黑名单:放行");
        return getNext().logic(strategyId,userId);
    }

    @Override
    protected String ruleModel() {
        return "rule_blacklist";
    }
}
