package re.yuugu.hzx.domain.strategy.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import re.yuugu.hzx.domain.strategy.model.entity.GachaAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.GachaFactorEntity;
import re.yuugu.hzx.domain.strategy.model.entity.RuleActionEntity;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionVO;
import re.yuugu.hzx.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.domain.strategy.service.armory.IStrategyArmory;
import re.yuugu.hzx.domain.strategy.service.rule.chain.ILogicChain;
import re.yuugu.hzx.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

@Slf4j
public abstract class AbstractGachaStrategy implements IGachaStrategy {

    protected IStrategyRepository strategyRepository;
    protected IStrategyArmory strategyArmory;
    protected DefaultLogicChainFactory defaultLogicChainFactory;

    public AbstractGachaStrategy(IStrategyRepository strategyRepository, IStrategyArmory strategyArmory, DefaultLogicChainFactory defaultLogicChainFactory) {
        this.strategyRepository = strategyRepository;
        this.strategyArmory = strategyArmory;
        this.defaultLogicChainFactory=defaultLogicChainFactory;
    }

    @Override
    public GachaAwardEntity performGacha(GachaFactorEntity gachaFactor) {
        String userId = gachaFactor.getUserId();
        Long strategyId = gachaFactor.getStrategyId();
        // 1. 参数校验
        if (StringUtils.isAllBlank(userId) || strategyId == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        // 2. 责任链处理黑名单、权重范围
        ILogicChain logicChain=defaultLogicChainFactory.openLogicChain(strategyId);
        Integer gachaAwardId = logicChain.logic(strategyId,userId);

        gachaFactor.setAwardId(gachaAwardId);
        // 3. 查询该奖品配置了哪些规则
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = strategyRepository.queryStrategyAwardRuleModelVO(strategyId,gachaAwardId);

        // 4. 抽奖中规则过滤
        RuleActionEntity<RuleActionEntity.ProcessingGachaEntity> ruleActionProcessingEntity = this.doCheckProcessingGacha(gachaFactor, strategyAwardRuleModelVO.gachaProcessingRule());
        if (RuleActionVO.TAKE_OVER.getCode().equals(ruleActionProcessingEntity.getCode())){
            log.info("匹配到rule_lock规则");
            return GachaAwardEntity.builder()
                    .awardDesc("兜底奖品")
                    .build();
        }

        return GachaAwardEntity.builder()
                .awardId(gachaAwardId)
                .build();
    }

    protected abstract RuleActionEntity<RuleActionEntity.BeforeGachaEntity> doCheckBeforeGacha(GachaFactorEntity gachaFactor, String... logics);
    protected abstract RuleActionEntity<RuleActionEntity.ProcessingGachaEntity> doCheckProcessingGacha(GachaFactorEntity gachaFactor, String... logics);
}
