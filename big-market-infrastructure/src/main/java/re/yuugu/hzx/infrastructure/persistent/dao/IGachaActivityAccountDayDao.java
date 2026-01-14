package re.yuugu.hzx.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.GachaActivityAccountDay;

/**
 * @ author anon
 * @ description IActivityAccountDayDao
 * @ create 2026/1/13 21:06
 */
@Mapper
public interface IGachaActivityAccountDayDao {
    @DBRouter(key="userId")
    GachaActivityAccountDay queryActivityAccountDayById(GachaActivityAccountDay gachaActivityAccountDay);

    void insert(GachaActivityAccountDay gachaActivityAccountDay);

    int updateAccountDayQuota(GachaActivityAccountDay gachaActivityAccountDay);
}
