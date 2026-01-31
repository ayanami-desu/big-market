package re.yuugu.hzx.domain.acitivity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.acitivity.model.vo.TradeOrderTypeVO;

/**
 * @ author anon
 * @ description GachaActivityChargeEntity
 * @ create 2026/1/4 18:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityChargeEntity {
    private String userId;
    private Long sku;
    /**
     * 外部透传字段，保证订单唯一性
     */
    private String outBusinessNo;
    private TradeOrderTypeVO tradePolicy;
}
