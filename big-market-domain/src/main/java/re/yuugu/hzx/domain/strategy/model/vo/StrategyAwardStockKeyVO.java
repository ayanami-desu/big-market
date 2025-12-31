package re.yuugu.hzx.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description StrategyAwardStockKeyVO
 * @ create 2025/12/30 23:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyAwardStockKeyVO {
    Long strategyId;
    Integer awardId;
}
