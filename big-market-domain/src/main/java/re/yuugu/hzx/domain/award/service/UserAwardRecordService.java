package re.yuugu.hzx.domain.award.service;

import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.award.event.SendAwardMessageEvent;
import re.yuugu.hzx.domain.award.model.aggregate.CreateUserAwardRecordAggregate;
import re.yuugu.hzx.domain.award.model.entity.TaskEntity;
import re.yuugu.hzx.domain.award.model.entity.UserAwardRecordEntity;
import re.yuugu.hzx.domain.award.model.vo.TaskStateVO;
import re.yuugu.hzx.domain.award.repository.IAwardRepository;
import re.yuugu.hzx.types.event.BaseEvent;

import javax.annotation.Resource;

/**
 * @ author anon
 * @ description UserAwardRecordService
 * @ create 2026/1/15 19:43
 */
@Service
public class UserAwardRecordService implements IUserAwardRecordService {
    @Resource
    private IAwardRepository awardRepository;
    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        //消息实体对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setOrderId(userAwardRecordEntity.getOrderId());
        sendAwardMessage.setAwardConfig(userAwardRecordEntity.getAwardConfig());
        sendAwardMessage.setAwardKey(userAwardRecordEntity.getAwardKey());
        //mq 消息
        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> msg = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);

        //任务对象
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setMessage(msg);
        taskEntity.setMessageId(msg.getId());
        taskEntity.setState(TaskStateVO.create);
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        //构建聚合对象
        CreateUserAwardRecordAggregate createUserAwardRecordAggregate = new CreateUserAwardRecordAggregate();
        createUserAwardRecordAggregate.setTask(taskEntity);
        createUserAwardRecordAggregate.setUserAwardRecord(userAwardRecordEntity);

        awardRepository.doSaveUserAwardRecordAggregate(createUserAwardRecordAggregate);
    }
}
