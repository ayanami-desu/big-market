package re.yuugu.hzx.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.GachaActivityOrder;

/**
 * @ author anon
 * @ description IGachaActivityOrderDao
 * @ create 2026/1/5 11:48
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IGachaActivityOrderDao {
    void insert(GachaActivityOrder gachaActivityOrder);
}
