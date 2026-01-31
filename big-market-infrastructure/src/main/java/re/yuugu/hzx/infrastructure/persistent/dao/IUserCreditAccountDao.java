package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import re.yuugu.hzx.infrastructure.persistent.po.UserCreditAccount;

import java.math.BigDecimal;

/**
 * @ author anon
 * @ description IUserCreditAccountDao
 * @ create 2026/1/27 13:45
 */
@Mapper
public interface IUserCreditAccountDao {
    int updateUserCreditAccount(String userId, BigDecimal creditAmount);

    void insert(String userId, @Param("creditAmount") BigDecimal creditAmount);

    int minusUpdateUserCreditAccount(String userId, @Param("creditAmount") BigDecimal creditAmount);

    UserCreditAccount queryUserCreditAccountByUserId(String userId);
}
