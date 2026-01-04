package re.yuugu.hzx.domain.acitivity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description GachaActivityChargeEntity
 * @ create 2026/1/4 18:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityChargeEntity {
    private String userId;
    private Long sku;
}
