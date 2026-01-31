package re.yuugu.hzx.domain.credit.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.credit.event.CreditPaySuccessEvent;
import re.yuugu.hzx.domain.credit.model.entity.CreditAdjustEntity;
import re.yuugu.hzx.domain.credit.model.entity.UserCreditOrderEntity;
import re.yuugu.hzx.types.event.EventTask;

/**
 * @ author anon
 * @ description CreateCreditOrderAggregate
 * @ create 2026/1/28 14:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCreditAdjustAggregate {
    private String userId;
    private CreditAdjustEntity creditAdjustEntity;
    private UserCreditOrderEntity userCreditOrderEntity;
    private EventTask<CreditPaySuccessEvent.ActivityOrderEvent> eventTask;
}
