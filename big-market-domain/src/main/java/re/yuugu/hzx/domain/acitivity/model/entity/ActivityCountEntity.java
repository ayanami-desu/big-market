package re.yuugu.hzx.domain.acitivity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description ActivityCountEntity
 * @ create 2026/1/4 19:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityCountEntity {
    /**
     * 活动次数编号
     */
    private Long activityCountId;

    /**
     * 总次数
     */
    private Integer totalCount;

    /**
     * 日次数
     */
    private Integer dayCount;

    /**
     * 月次数
     */
    private Integer monthCount;
}
