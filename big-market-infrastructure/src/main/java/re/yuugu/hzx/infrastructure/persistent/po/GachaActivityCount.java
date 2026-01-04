package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @ author anon
 * @ description GachaActivityCount
 * @ create 2026/1/2 17:47
 */
@Data
public class GachaActivityCount {
    /**
     * 自增 ID
     */
    private Long id;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
