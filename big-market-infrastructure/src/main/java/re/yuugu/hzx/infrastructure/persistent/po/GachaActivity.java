package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @ author anon
 * @ description GachaActivity
 * @ create 2026/1/2 17:46
 */
@Data
public class GachaActivity {
    /**
     * 自增 ID
     */
    private Long id;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
