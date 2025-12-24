package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.Strategy;

@Mapper
public interface IStrategyDao {
    Strategy queryStrategyByStrategyId(Long strategyId);
}
