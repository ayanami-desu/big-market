package re.yuugu.hzx.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description AwardDistributeEntity
 * @ create 2026/1/27 13:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AwardDistributeEntity {
    private String userId;
    private Integer awardId;
    private String awardConfig;
    private String awardKey;
    private String orderId;
}
