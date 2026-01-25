package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @ author anon
 * @ description UserBehaviorRebate
 * @ create 2026/1/24 15:45
 */
@Data
public class UserBehaviorRebate {
    /** 自增 ID */
    private Integer id;

    /** 行为类型（sign 签到、openai_pay 支付） */
    private String behaviorType;

    /** 返利描述 */
    private String rebateDesc;

    /** 返利类型（sku 活动库存充值商品、integral 用户活动积分） */
    private String rebateType;

    /** 返利配置【sku值，积分值】 */
    private String rebateConfig;
    /**
     * 返利行为状态 open开启，close关闭
     */
    private String state;
    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}
