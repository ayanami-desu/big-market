package re.yuugu.hzx.domain.rebate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ author anon
 * @ description RebateTypeVO
 * @ create 2026/1/24 17:16
 */
@Getter
@AllArgsConstructor
public enum RebateTypeVO {
    POINT("POINT","用户活动积分"),
    SKU("SKU","活动库存充值商品"),
    ;

    private final String code;
    private final String info;
}
