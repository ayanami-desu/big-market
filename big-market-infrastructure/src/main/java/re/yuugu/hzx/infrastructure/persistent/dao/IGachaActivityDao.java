package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.GachaActivity;

/**
 * @ author anon
 * @ description IGachaActivityDao
 * @ create 2026/1/4 19:47
 */
@Mapper
public interface IGachaActivityDao {
    GachaActivity queryActivityById(Long activityId);

    Long queryStrategyIdByActivityId(Long activityId);

    Long queryActivityIdByStrategyId(Long strategyId);
}
