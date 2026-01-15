package re.yuugu.hzx.infrastructure.persistent.repository;

import org.springframework.stereotype.Repository;
import re.yuugu.hzx.domain.task.model.entity.TaskEntity;
import re.yuugu.hzx.domain.task.repository.ITaskRepository;
import re.yuugu.hzx.infrastructure.event.EventPublisher;
import re.yuugu.hzx.infrastructure.persistent.dao.ITaskDao;
import re.yuugu.hzx.infrastructure.persistent.po.Task;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ author anon
 * @ description TaskRepository
 * @ create 2026/1/15 21:36
 */
@Repository
public class TaskRepository implements ITaskRepository {
    @Resource
    private ITaskDao taskDao;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        List<TaskEntity> taskEntities = new ArrayList<>();
        List<Task> tasks = taskDao.queryNoSendMessageTaskList();
        if (tasks == null || tasks.isEmpty()) {
            return null;
        }
        for (Task task : tasks) {
            TaskEntity taskEntity = TaskEntity.builder()
                    .userId(task.getUserId())
                    .topic(task.getTopic())
                    .messageId(task.getMessageId())
                    .message(task.getMessage())
                    .build();
            taskEntities.add(taskEntity);
        }
        return taskEntities;
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        eventPublisher.publishString(taskEntity.getTopic(), taskEntity.getMessage());
    }

    @Override
    public void updateStateToCompleted(String userId, String messageId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setMessageId(messageId);
        taskDao.updateStateToCompleted(task);
    }

    @Override
    public void updateStateToFail(String userId, String messageId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setMessageId(messageId);
        taskDao.updateStateToCompleted(task);
    }
}
