package re.yuugu.hzx.domain.rebate.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.rebate.event.SendRebateMessageEvent;
import re.yuugu.hzx.domain.rebate.model.entity.RebateOrderEntity;
import re.yuugu.hzx.types.event.EventTask;

/**
 * @ author anon
 * @ description CreateRebateOrderAggregate
 * @ create 2026/1/24 16:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRebateOrderAggregate {
    private String userId;
    private RebateOrderEntity rebateOrderEntity;
    private EventTask<SendRebateMessageEvent.SendRebateMessage> eventTask;
}
