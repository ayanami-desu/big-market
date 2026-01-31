package re.yuugu.hzx.types.event;

import com.alibaba.fastjson.JSON;
import lombok.*;


/**
 * @ author anon
 * @ description Task
 * @ create 2026/1/29 22:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventTask<T> {
    private String userId;
    private String topic;
    private BaseEvent.EventMessage<T> message;
    private String messageId;
    private TaskStateVO state;

    public String getMessageAsJSONString() {
        return JSON.toJSONString(message);
    }
    public EventTask(BaseEvent.EventMessage<T> message, String userId, String topic) {
        this.message = message;
        this.userId = userId;
        this.topic = topic;
        this.messageId=message.getId();
        this.state = TaskStateVO.create;
    }
    @Getter
    @AllArgsConstructor
    public enum TaskStateVO {
        create("create","任务已创建"),
        completed("completed","任务已完成"),
        fail("fail","任务失败")
        ;

        private final String code;
        private final String info;
    }
}
