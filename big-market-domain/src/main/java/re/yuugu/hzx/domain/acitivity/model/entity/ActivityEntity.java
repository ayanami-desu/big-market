package re.yuugu.hzx.domain.acitivity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ author anon
 * @ description ActivityEntity
 * @ create 2026/1/4 19:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityEntity {
    /**
     * 活动 ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 开始时间
     */
    private Date beginDateTime;

    /**
     * 结束时间
     */
    private Date endDateTime;

    /**
     * 抽奖策略 ID
     */
    private Long strategyId;

    /**
     * 活动状态
     */
    private String state;
}
