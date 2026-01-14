package re.yuugu.hzx.domain.acitivity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description ActivityPartakeEntity
 * @ create 2026/1/11 17:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityPartakeEntity {
    String userId;
    Long activityId;
}
