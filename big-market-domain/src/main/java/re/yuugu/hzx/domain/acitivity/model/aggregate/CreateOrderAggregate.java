package re.yuugu.hzx.domain.acitivity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.acitivity.model.entity.ActivityOrderEntity;

/**
 * @ author anon
 * @ description CreateOrderAggregate
 * @ create 2026/1/5 11:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderAggregate {
    private ActivityOrderEntity activityOrderEntity;
}
