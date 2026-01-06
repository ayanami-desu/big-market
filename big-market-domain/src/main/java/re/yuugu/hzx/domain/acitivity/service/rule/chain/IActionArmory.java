package re.yuugu.hzx.domain.acitivity.service.rule.chain;


/**
 * @ author anon
 * @ description IActionArmory
 * @ create 2026/1/5 00:38
 */
public interface IActionArmory {
    IActionChain appendNext(IActionChain next);
    IActionChain getNext();
}
