package re.yuugu.hzx.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.Task;

import java.util.List;

/**
 * @ author anon
 * @ description ITaskDao
 * @ create 2026/1/15 19:48
 */
@Mapper
public interface ITaskDao {
    void insert(Task task);

    @DBRouter(key = "userId")
    void updateStateToCompleted(Task task);

    @DBRouter(key = "userId")
    void updateStateToFail(Task task);

    List<Task> queryNoSendMessageTaskList();
}
