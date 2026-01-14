package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ author anon
 * @ description GachaActivityAccountDay
 * @ create 2026/1/11 22:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GachaActivityAccountDay {
    /** 自增 ID */
    private String id;
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
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
