package re.yuugu.hzx.domain.task.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description TaskEntity
 * @ create 2026/1/15 22:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskEntity {

    /** 用户 ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    /** 消息编号 */
    private String messageId;
    /** 消息主体 */
    private String message;

}
