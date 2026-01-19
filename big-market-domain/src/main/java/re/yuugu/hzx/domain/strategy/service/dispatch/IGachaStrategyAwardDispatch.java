package re.yuugu.hzx.domain.strategy.service.dispatch;

/**
 * @ author anon
 * @ description IGachaStrategyAwardDispatch
 * @ create 2026/1/19 17:19
 */
public interface IGachaStrategyAwardDispatch {
    Integer getRandomAwardId(String key);
    Integer getRandomAwardId(String key,String ruleWeight);
}
