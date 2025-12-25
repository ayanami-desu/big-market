package re.yuugu.hzx.domain.strategy.service.Gacha;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import re.yuugu.hzx.domain.strategy.model.entity.GachaAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.GachaFactorEntity;
import re.yuugu.hzx.domain.strategy.model.entity.RuleActionEntity;
import re.yuugu.hzx.domain.strategy.model.entity.StrategyEntity;
import re.yuugu.hzx.domain.strategy.model.vo.RuleActionType;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.domain.strategy.service.IGachaStrategy;
import re.yuugu.hzx.domain.strategy.service.armory.IStrategyArmory;
import re.yuugu.hzx.domain.strategy.service.rule.factory.DefaultLogicFactory;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

@Slf4j
public abstract class AbstractGachaStrategy implements IGachaStrategy {

    protected IStrategyRepository strategyRepository;
    protected IStrategyArmory strategyArmory;

    public AbstractGachaStrategy(IStrategyRepository strategyRepository, IStrategyArmory strategyArmory) {
        this.strategyRepository = strategyRepository;
        this.strategyArmory = strategyArmory;
    }

    @Override
    public GachaAwardEntity performGacha(GachaFactorEntity gachaFactor) {
        String userId = gachaFactor.getUserId();
        Long strategyId = gachaFactor.getStrategyId();
        // 1. param check
        if (StringUtils.isAllBlank(userId) || strategyId == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        // 2. 查询策略配置的规则
        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntityByStrategyId(strategyId);
        if (strategyEntity == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        // 3. 规则过滤,一次只会匹配到一个规则
        RuleActionEntity<RuleActionEntity.BeforeGachaEntity> ruleActionEntity = this.doCheckBeforeGacha(gachaFactor, strategyEntity.ruleModels());
        // 4. 未匹配到规则
        if (ruleActionEntity == null) {
            log.info("未匹配到规则");
            return GachaAwardEntity.builder()
                    .awardId(strategyArmory.getRandomAwardId(String.valueOf(strategyId)))
                    .build();
        }
        // 5. 匹配到黑名单规则或权重规则
        Integer gachaAwardId = 0;
        if (RuleActionType.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())) {
            log.info("匹配到规则");
            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())) {
                log.info("匹配到黑名单规则");
                gachaAwardId = ruleActionEntity.getData().getAwardId();
            } else if (DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode().equals(ruleActionEntity.getRuleModel())) {
                log.info("匹配到权重范围规则");
                gachaAwardId = strategyArmory.getRandomAwardId(strategyId + "_" + ruleActionEntity.getData().getRuleWeightValueKey());
            }
        }
        return gachaAwardId == 0 ? null : GachaAwardEntity.builder()
                .awardId(gachaAwardId)
                .build();
    }

    protected abstract RuleActionEntity<RuleActionEntity.BeforeGachaEntity> doCheckBeforeGacha(GachaFactorEntity gachaFactor, String... logics);
}
