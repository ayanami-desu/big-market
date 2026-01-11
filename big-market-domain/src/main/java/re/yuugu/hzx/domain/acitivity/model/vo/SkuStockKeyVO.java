package re.yuugu.hzx.domain.acitivity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author anon
 * @ description SkuStockKeyVO
 * @ create 2026/1/9 16:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkuStockKeyVO {
    private Long sku;
    private Long activityId;
}
