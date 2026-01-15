package re.yuugu.hzx.domain.task.service;

import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.task.model.entity.TaskEntity;
import re.yuugu.hzx.domain.task.repository.ITaskRepository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ author anon
 * @ description TaskService
 * @ create 2026/1/15 21:35
 */
@Service
public class TaskService implements ITaskService {
    @Resource
    private ITaskRepository taskRepository;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        return taskRepository.queryNoSendMessageTaskList();
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        taskRepository.sendMessage(taskEntity);
    }

    @Override
    public void updateStateToCompleted(String userId, String messageId) {
        taskRepository.updateStateToCompleted(userId, messageId);
    }

    @Override
    public void updateStateToFail(String userId, String messageId) {
        taskRepository.updateStateToFail(userId, messageId);
    }
}
