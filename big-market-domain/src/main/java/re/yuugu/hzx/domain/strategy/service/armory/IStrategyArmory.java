package re.yuugu.hzx.domain.strategy.service.armory;

import re.yuugu.hzx.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

public interface IStrategyArmory {


    boolean assembleLotteryStrategy(Long strategyId);
    void assembleOneStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities);
    Integer getRandomAwardId(String key);
    Integer getRandomAwardId(String key,String ruleWeight);
}
