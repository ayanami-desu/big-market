package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.GachaActivityAccount;

/**
 * @ author anon
 * @ description IGachaActivityAccount
 * @ create 2026/1/5 21:41
 */
@Mapper
public interface IGachaActivityAccountDao {
    //TODO 为什么这里不用设置分库分表注解？
    int updateAccountQuota(GachaActivityAccount gachaActivityAccount);

    void insert(GachaActivityAccount gachaActivityAccount);
}
