package re.yuugu.hzx.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.GachaActivityAccount;

/**
 * @ author anon
 * @ description IGachaActivityAccount
 * @ create 2026/1/5 21:41
 */
@Mapper
public interface IGachaActivityAccountDao {
    @DBRouter(key = "userId")
    GachaActivityAccount queryActivityAccountById(GachaActivityAccount activityAccount);

    //@DbRouter 非事务中时使用，默认使用userId作为路由键
    int updateAccountQuota(GachaActivityAccount activityAccount);

    void insert(GachaActivityAccount gachaActivityAccount);


    int updateAccountQuotaSubtraction(GachaActivityAccount build);
}
