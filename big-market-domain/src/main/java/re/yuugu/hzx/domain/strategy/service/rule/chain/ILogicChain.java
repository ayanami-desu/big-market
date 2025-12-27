package re.yuugu.hzx.domain.strategy.service.rule.chain;

public interface ILogicChain extends ILoginChainArmory{

    /**
     * strategyId 策略id
     * userId 用户id
     * return 奖品id
    */
    Integer logic(Long strategyId,String userId);
}
