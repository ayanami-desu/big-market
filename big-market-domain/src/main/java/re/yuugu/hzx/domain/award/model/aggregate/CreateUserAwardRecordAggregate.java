package re.yuugu.hzx.domain.award.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.award.event.SendAwardMessageEvent;
import re.yuugu.hzx.domain.award.model.entity.UserAwardRecordEntity;
import re.yuugu.hzx.types.event.EventTask;

/**
 * @ author anon
 * @ description CreateUserAwardRecordAggregate
 * @ create 2026/1/15 20:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserAwardRecordAggregate {
    private UserAwardRecordEntity userAwardRecord;
    private EventTask<SendAwardMessageEvent.SendAwardMessage> eventTask;
}
