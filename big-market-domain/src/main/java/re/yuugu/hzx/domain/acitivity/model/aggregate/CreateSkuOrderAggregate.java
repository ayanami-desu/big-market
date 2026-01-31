package re.yuugu.hzx.domain.acitivity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityOrderEntity;
import re.yuugu.hzx.domain.acitivity.model.vo.ActivityQuotaOrderState;

import java.math.BigDecimal;

/**
 * @ author anon
 * @ description CreateOrderAggregate
 * @ create 2026/1/5 11:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSkuOrderAggregate {
    String userId;
    private ActivityOrderEntity activityOrderEntity;

    public void setActivityOrderEntityState(ActivityQuotaOrderState  activityQuotaOrderState) {
        activityOrderEntity.setState(activityQuotaOrderState);
    }
    public void setActivityOrderEntitySkuPrice(BigDecimal n){
        activityOrderEntity.setSkuPrice(n);
    }
}
