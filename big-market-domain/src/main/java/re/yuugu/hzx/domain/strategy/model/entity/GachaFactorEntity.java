package re.yuugu.hzx.domain.strategy.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// 影响抽奖结果的因素
public class GachaFactorEntity {
    private String userId;
    private Long strategyId;
}
