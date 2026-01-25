package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.UserBehaviorRebate;

import java.util.List;

/**
 * @ author anon
 * @ description IUserBehaviorRebateDao
 * @ create 2026/1/25 09:40
 */
@Mapper
public interface IUserBehaviorRebateDao {
    List<UserBehaviorRebate> queryBehaviorRebateConfig(String behaviorType);
}
