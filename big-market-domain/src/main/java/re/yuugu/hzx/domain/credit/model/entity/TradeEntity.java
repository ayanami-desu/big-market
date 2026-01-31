package re.yuugu.hzx.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import re.yuugu.hzx.domain.credit.model.vo.TradeNameVO;
import re.yuugu.hzx.domain.credit.model.vo.TradeTypeVO;

import java.math.BigDecimal;

/**
 * @ author anon
 * @ description TradeEntity
 * @ create 2026/1/28 14:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeEntity {
    private String userId;
    private TradeNameVO tradeName;
    /**
     * 交易类型；forward-正向、reverse-逆向
     */
    private TradeTypeVO tradeType;
    private BigDecimal tradeAmount;
    private String outBusinessNo;
}
