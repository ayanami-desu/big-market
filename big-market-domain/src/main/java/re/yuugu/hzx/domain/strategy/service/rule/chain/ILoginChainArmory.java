package re.yuugu.hzx.domain.strategy.service.rule.chain;

public interface ILoginChainArmory {
    ILogicChain appendNext(ILogicChain next);
    ILogicChain getNext();
}
