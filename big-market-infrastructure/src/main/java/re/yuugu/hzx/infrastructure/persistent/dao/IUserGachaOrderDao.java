package re.yuugu.hzx.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.UserGachaOrder;

import java.util.List;

/**
 * @ author anon
 * @ description IUserGachaOrderDao
 * @ create 2026/1/14 22:13
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserGachaOrderDao {
    @DBRouter(key="userId")
    List<UserGachaOrder> queryNoUsedGachaOrder(UserGachaOrder userGachaOrder);

    void insert(UserGachaOrder userGachaOrder);
}
