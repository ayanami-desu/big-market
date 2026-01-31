package re.yuugu.hzx.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.UserCreditOrder;

/**
 * @ author anon
 * @ description IUserCreditOrderDao
 * @ create 2026/1/28 14:34
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserCreditOrderDao {
    void insert(UserCreditOrder userCreditOrder);
}
