package re.yuugu.hzx.domain.acitivity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ author anon
 * @ description OrderTradeTypeVO
 * @ create 2026/1/29 15:05
 */
@Getter
@AllArgsConstructor
public enum TradeOrderTypeVO {
    credit_pay_trade("credit_pay_trade_policy","通过积分支付方式获得的订单"),
    rebate_no_pay_trade("rebate_no_pay_trade_policy","通过行为返利方式获得的订单"),
    ;
    private final String code;
    private final String info;
}
