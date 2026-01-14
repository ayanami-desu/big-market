package re.yuugu.hzx.domain.acitivity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description GachaActivityAccountDay
 * @ create 2026/1/11 22:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityAccountDayEntity {
    /** 用户 ID */
    private String userId;
    /** 活动 ID */
    private Long activityId;
    /** 日期（yyyy-mm-dd） */
    private String day;
    /** 日次数 */
    private Integer dayCount;
    /** 日次数-剩余 */
    private Integer dayCountSurplus;
}
