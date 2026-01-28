package re.yuugu.hzx.domain.award.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.award.model.entity.CreditAwardEntity;
import re.yuugu.hzx.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @ author anon
 * @ description DistributeCreditAwardAggregate
 * @ create 2026/1/27 14:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistributeCreditAwardAggregate {
    private String userId;
    private UserAwardRecordEntity userAwardRecord;
    private CreditAwardEntity creditAwardEntity;
}
