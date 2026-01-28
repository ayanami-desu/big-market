package re.yuugu.hzx.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.UserAwardRecord;

/**
 * @ author anon
 * @ description IUserAwardRecordDao
 * @ create 2026/1/15 21:07
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserAwardRecordDao {
    void insert(UserAwardRecord userAwardRecord);

    int updateUserAwardRecordStateToCompleted(UserAwardRecord userAwardRecordReq);
}
