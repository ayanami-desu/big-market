package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ author anon
 * @ description UserAwardRecord
 * @ create 2026/1/15 19:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAwardRecord {
    private Long id;
    private String userId;
    private Long activityId;
    private Long strategyId;
    private Integer awardId;
    private String orderId;
    private String awardTitle;
    private Date awardTime;
    private String awardState;
    private Date createTime;
    private Date updateTime;
}
