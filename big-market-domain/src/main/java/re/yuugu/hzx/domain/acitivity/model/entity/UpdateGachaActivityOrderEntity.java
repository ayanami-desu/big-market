package re.yuugu.hzx.domain.acitivity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description UpdateGachaActivityOrderEntity
 * @ create 2026/1/29 16:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateGachaActivityOrderEntity {
    private String userId;
        /**
     * 外部透传字段，保证订单唯一性
     */
    private String outBusinessNo;
}
