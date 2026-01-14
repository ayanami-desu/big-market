package re.yuugu.hzx.domain.acitivity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityAccountDayEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityAccountEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityAccountMonthEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.UserGachaOrderEntity;

/**
 * @ author anon
 * @ description CreateGachaOrderAggregate
 * @ create 2026/1/11 22:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateGachaOrderAggregate {
    private String userId;
    private Long activityId;
    private ActivityAccountEntity activityAccountEntity;
    private boolean isDayAccountExist=true;
    private ActivityAccountDayEntity activityAccountDayEntity;
    private boolean isMonthAccountExist=true;
    private ActivityAccountMonthEntity activityAccountMonthEntity;
    private UserGachaOrderEntity  userGachaOrderEntity;
}
