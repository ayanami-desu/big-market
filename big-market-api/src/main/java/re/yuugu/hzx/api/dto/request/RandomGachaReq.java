package re.yuugu.hzx.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description RandomGachaReq
 * @ create 2026/1/1 15:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RandomGachaReq {
    String userId;
    Long strategyId;
}
