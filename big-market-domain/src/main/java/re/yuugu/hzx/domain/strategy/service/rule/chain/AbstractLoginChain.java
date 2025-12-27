package re.yuugu.hzx.domain.strategy.service.rule.chain;

public abstract class AbstractLoginChain implements ILogicChain{

    private ILogicChain next;

    @Override
    public ILogicChain getNext() {
        return next;
    }
    @Override
    public ILogicChain appendNext(ILogicChain next){
        this.next = next;
        return next;
    }

    protected abstract String ruleModel();

}
