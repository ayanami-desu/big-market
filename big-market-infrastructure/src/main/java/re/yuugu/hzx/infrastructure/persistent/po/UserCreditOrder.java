package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ author anon
 * @ description UserCreditOrder
 * @ create 2026/1/28 14:31
 */
@Data
public class UserCreditOrder {
    private Long id;
    private String userId;
    private String orderId;
    private String tradeName;
    /**
     * 交易类型；forward-正向、reverse-逆向
     */
    private String tradeType;
    private BigDecimal tradeAmount;
    private String outBusinessNo;
    private Date createTime;
    private Date updateTime;
}
