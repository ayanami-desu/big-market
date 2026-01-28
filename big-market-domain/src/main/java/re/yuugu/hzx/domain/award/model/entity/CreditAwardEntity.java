package re.yuugu.hzx.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ author anon
 * @ description CreditAwardEntity
 * @ create 2026/1/27 14:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditAwardEntity {
    private String userId;
    private BigDecimal creditAmount;
}
