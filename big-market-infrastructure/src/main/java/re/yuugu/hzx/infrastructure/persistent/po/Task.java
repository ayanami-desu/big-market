package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ author anon
 * @ description Task
 * @ create 2026/1/15 19:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long id;
    private String userId;
    private String topic;
    private String message;
    private String messageId;
    private String state;
    private Date createTime;
    private Date updateTime;
}
