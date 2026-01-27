package re.yuugu.hzx.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description ActivityDailySignReq
 * @ create 2026/1/26 14:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityDailySignReq {
    private String userId;
    private Long activityId;
}
