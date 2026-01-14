package re.yuugu.hzx.domain.acitivity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description GachaActivityAccountMonth
 * @ create 2026/1/11 22:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityAccountMonthEntity {
    /** 用户 ID */
    private String userId;
    /** 活动 ID */
    private Long activityId;
    /** 月（yyyy-mm） */
    private String month;
    /** 月次数 */
    private Integer monthCount;
    /** 月次数-剩余 */
    private Integer monthCountSurplus;
}
