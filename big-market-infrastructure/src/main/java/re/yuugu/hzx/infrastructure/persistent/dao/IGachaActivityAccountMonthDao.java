package re.yuugu.hzx.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.GachaActivityAccountMonth;

/**
 * @ author anon
 * @ description ActivityAccountMonthDao
 * @ create 2026/1/13 21:06
 */
@Mapper
public interface IGachaActivityAccountMonthDao {
    @DBRouter(key="userId")
    GachaActivityAccountMonth queryActivityAccountMonthById(GachaActivityAccountMonth gachaActivityAccountMonth);

    int updateAccountMonthQuota(GachaActivityAccountMonth gachaActivityAccountMonth);

    void insert(GachaActivityAccountMonth gachaActivityAccountMonth);
}
