package re.yuugu.hzx.domain.strategy.service.rule.chain;

import re.yuugu.hzx.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;

public interface ILogicChain extends ILoginChainArmory{

    /**
     * strategyId 策略id
     * userId 用户id
     * return 奖品id
    */
    DefaultLogicChainFactory.ChainAwardVO logic(Long strategyId, String userId);
}
