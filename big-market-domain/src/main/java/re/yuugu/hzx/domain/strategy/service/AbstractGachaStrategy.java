package re.yuugu.hzx.domain.strategy.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import re.yuugu.hzx.domain.strategy.model.entity.AwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.GachaAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.GachaFactorEntity;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;
import re.yuugu.hzx.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import re.yuugu.hzx.domain.strategy.service.rule.tree.factory.DefaultRuleTreeFactory;
import re.yuugu.hzx.types.enums.ResponseCode;
import re.yuugu.hzx.types.exception.AppException;

@Slf4j
public abstract class AbstractGachaStrategy implements IGachaStrategy, IGachaAwardStock {
    protected IStrategyRepository strategyRepository;
    protected DefaultLogicChainFactory defaultLogicChainFactory;
    protected DefaultRuleTreeFactory defaultRuleTreeFactory;

    public AbstractGachaStrategy(IStrategyRepository strategyRepository, DefaultLogicChainFactory defaultLogicChainFactory,DefaultRuleTreeFactory defaultRuleTreeFactory) {
        this.strategyRepository = strategyRepository;
        this.defaultLogicChainFactory=defaultLogicChainFactory;
        this.defaultRuleTreeFactory = defaultRuleTreeFactory;
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
        DefaultLogicChainFactory.ChainAwardVO chainAwardVO = processLogicChain(strategyId,userId);
        if(!DefaultLogicChainFactory.LogicChainType.RULE_DEFAULT.getCode().equals(chainAwardVO.getLogicChainType().getCode())){
            log.info(chainAwardVO.getLogicChainType().getCode());
            return buildAward(chainAwardVO.getAwardId());
        }
        //3. 规则树处理次数锁，库存，兜底奖品
        DefaultRuleTreeFactory.TreeAwardVO treeAwardVO = processLogicTree(strategyId,userId,chainAwardVO.getAwardId());
        return buildAward(treeAwardVO.getAwardId());

    }
    private GachaAwardEntity buildAward(Integer awardId){
        AwardEntity awardEntity =  strategyRepository.queryAwardDetailById(awardId);
        return GachaAwardEntity.builder()
                .awardId(awardEntity.getAwardId())
                .awardKey(awardEntity.getAwardKey())
                .awardConfig(awardEntity.getAwardConfig())
                .awardTitle(awardEntity.getAwardTitle())
                .build();
    }
    protected abstract DefaultLogicChainFactory.ChainAwardVO processLogicChain(Long strategyId,String userId);
    protected abstract DefaultRuleTreeFactory.TreeAwardVO processLogicTree(Long strategyId,String userId,Integer awardId);
}
