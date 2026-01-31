package re.yuugu.hzx.domain.credit.service;

import re.yuugu.hzx.domain.credit.model.entity.TradeEntity;

/**
 * @ author anon
 * @ description ICreditAdjust
 * @ create 2026/1/28 14:40
 */
public interface ICreditAdjust {
    String createCreditPayOrder(TradeEntity tradeEntity);

    String adjustCredit(TradeEntity tradeEntity);
}
