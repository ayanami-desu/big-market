package re.yuugu.hzx.domain.acitivity.service.quota.chain;

/**
 * @ author anon
 * @ description AbstractActionChain
 * @ create 2026/1/5 00:43
 */
public abstract class AbstractActionChain implements  IActionChain{
    private IActionChain next;
    @Override
    public IActionChain appendNext(IActionChain next) {
        this.next = next;
        return next;
    }

    @Override
    public IActionChain getNext() {
        return next;
    }
}
