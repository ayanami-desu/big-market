package re.yuugu.hzx.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.rebate.event.SendRebateMessageEvent;
import re.yuugu.hzx.domain.rebate.model.vo.TaskStateVO;
import re.yuugu.hzx.types.event.BaseEvent;

/**
 * @ author anon
 * @ description TaskEntity
 * @ create 2026/1/24 16:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskEntity {
    private String userId;
    private String topic;
    private BaseEvent.EventMessage<SendRebateMessageEvent.SendRebateMessage> message;
    private String messageId;
    private TaskStateVO state;
}
