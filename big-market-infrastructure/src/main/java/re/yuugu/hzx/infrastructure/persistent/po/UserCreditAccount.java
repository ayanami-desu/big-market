package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ author anon
 * @ description UserCreditAccount
 * @ create 2026/1/27 13:42
 */
@Data
public class UserCreditAccount {
    private String id;
    private String userId;
    private BigDecimal totalAmount;
    private BigDecimal availableAmount;
    /**
     * 账户状态【open - 可用，close - 冻结】
     */
    private String accountStatus;

    private Date createTime;

    private Date updateTime;
}
