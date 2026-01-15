package re.yuugu.hzx.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.award.model.vo.AwardStateVO;

import java.util.Date;

/**
 * @ author anon
 * @ description UserAwardResultEntity
 * @ create 2026/1/15 11:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAwardRecordEntity {
    private String userId;
    private Long activityId;
    private Long strategyId;
    private Integer awardId;
    private String orderId;
    private String awardTitle;
    private Date awardTime;
    private AwardStateVO awardState;
}
