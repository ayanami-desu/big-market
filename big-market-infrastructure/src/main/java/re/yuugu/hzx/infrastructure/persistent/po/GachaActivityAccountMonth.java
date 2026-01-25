package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ author anon
 * @ description GachaActivityAccountMonth
 * @ create 2026/1/11 22:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GachaActivityAccountMonth {
    /** 自增 ID */
    private String id;
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
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

    public String currentMonth(){
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
        return dateFormatMonth.format(new Date());
    }

}
