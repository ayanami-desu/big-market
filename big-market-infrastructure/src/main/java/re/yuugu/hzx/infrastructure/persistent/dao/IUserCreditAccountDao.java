package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
 * @ author anon
 * @ description IUserCreditAccountDao
 * @ create 2026/1/27 13:45
 */
@Mapper
public interface IUserCreditAccountDao {
    int updateUserCreditAccount(String userId, BigDecimal creditAmount);

    void insert(String userId, BigDecimal creditAmount);
}
