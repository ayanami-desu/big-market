package re.yuugu.hzx.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description StrategyAwardListRes
 * @ create 2026/1/1 14:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyAwardRes {
    private Integer awardId;
    private String awardTitle;
    private String awardSubtitle;
    private Integer sort;
}
