package re.yuugu.hzx.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ author anon
 * @ description CreditAdjustEntity
 * @ create 2026/1/28 14:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditAdjustEntity {
    private String userId;
    private BigDecimal adjustAmount;
}
