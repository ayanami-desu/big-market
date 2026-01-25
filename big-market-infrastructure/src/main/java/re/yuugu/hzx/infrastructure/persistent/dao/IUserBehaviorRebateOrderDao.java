package re.yuugu.hzx.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.UserBehaviorRebateOrder;

/**
 * @ author anon
 * @ description IUserBehaviorRebateOrderDao
 * @ create 2026/1/25 09:40
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserBehaviorRebateOrderDao {
    void insert(UserBehaviorRebateOrder userBehaviorRebateOrder);
}
