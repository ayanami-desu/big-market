package re.yuugu.hzx.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.award.event.SendAwardMessageEvent;
import re.yuugu.hzx.domain.award.model.vo.TaskStateVO;
import re.yuugu.hzx.types.event.BaseEvent;

/**
 * @ author anon
 * @ description TaskEntity
 * @ create 2026/1/15 19:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskEntity {
    private String userId;
    private String topic;
    private BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> message;
    private String messageId;
    private TaskStateVO state;
}
