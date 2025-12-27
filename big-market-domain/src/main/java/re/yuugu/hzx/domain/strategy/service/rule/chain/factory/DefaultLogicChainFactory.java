package re.yuugu.hzx.domain.strategy.service.rule.chain.factory;


import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyEntity;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.domain.strategy.service.rule.chain.ILogicChain;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

import java.util.Map;

@Service
public class DefaultLogicChainFactory {
    private final Map<String, ILogicChain> logicChainGroup;

    private final IStrategyRepository strategyRepository;

    public DefaultLogicChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository strategyRepository) {
        this.logicChainGroup = logicChainGroup;
        this.strategyRepository = strategyRepository;
    }
    public ILogicChain openLogicChain(Long strategyId){
        // 1. 查询策略配置的规则
        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntityByStrategyId(strategyId);
        if (strategyEntity == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        String[] ruleValues = strategyEntity.ruleModels();
        if  (ruleValues == null || ruleValues.length == 0) {
            return logicChainGroup.get("default");
        }
        ILogicChain logicChain = logicChainGroup.get(ruleValues[0]);
        ILogicChain cur =  logicChain;
        for (int i = 1; i < ruleValues.length; i++) {
            cur = cur.appendNext(logicChainGroup.get(ruleValues[i]));
        }
        // 将最后一个设置为默认责任链
        cur.appendNext(logicChainGroup.get("default"));
        return logicChain;
    }
}
