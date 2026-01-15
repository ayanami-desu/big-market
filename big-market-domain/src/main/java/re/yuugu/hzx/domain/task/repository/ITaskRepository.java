package re.yuugu.hzx.domain.task.repository;

import re.yuugu.hzx.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @ author anon
 * @ description ITaskRepository
 * @ create 2026/1/15 21:36
 */
public interface ITaskRepository {
    /**
     * 查询发送MQ失败和超时1分钟未发送的MQ
     *
     * @return 未发送的任务消息列表10条
     */
    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateStateToCompleted(String userId, String messageId);

    void updateStateToFail(String userId, String messageId);
}
