package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import re.yuugu.hzx.infrastructure.persistent.po.StrategyAward;

import java.util.List;

@Mapper
public interface IStrategyAwardDao {
    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);

    StrategyAward queryStrategyAwardRuleModels(@Param("strategyId") Long strategyId, @Param("awardId")Integer gachaAwardId);
}
