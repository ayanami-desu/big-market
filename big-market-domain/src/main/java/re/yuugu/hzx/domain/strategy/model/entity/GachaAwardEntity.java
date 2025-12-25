package re.yuugu.hzx.domain.strategy.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GachaAwardEntity {
    private Long strategyId;
    private Integer awardId;
    private String awardKey;
    private String awardConfig;
    private String awardDesc;
}
