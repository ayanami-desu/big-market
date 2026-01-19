package re.yuugu.hzx.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description ActivityDrawReq
 * @ create 2026/1/19 16:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityDrawReq {
    Long activityId;
    String userId;
}
