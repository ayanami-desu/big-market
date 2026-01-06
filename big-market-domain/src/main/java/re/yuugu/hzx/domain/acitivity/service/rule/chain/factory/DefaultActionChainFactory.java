package re.yuugu.hzx.domain.acitivity.service.rule.chain.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.acitivity.service.rule.chain.IActionChain;

import java.util.Map;

/**
 * @ author anon
 * @ description DefaultActionChainFactory
 * @ create 2026/1/5 00:43
 */
@Slf4j
@Service
public class DefaultActionChainFactory {
    private final IActionChain actionChain;

    public DefaultActionChainFactory(Map<String, IActionChain> actionChainGroup) {
        actionChain = actionChainGroup.get(ChainType.ACTIVITY_BASIC_ACTION_CHAIN.getCode());
        IActionChain skuChain = actionChainGroup.get(ChainType.ACTIVITY_SKU_STOCK_ACTION_CHAIN.getCode());
        actionChain.appendNext(skuChain);
        skuChain.appendNext(actionChainGroup.get(ChainType.ACTIVITY_DEFAULT.getCode()));
    }

    public IActionChain openActionChain() {
        return this.actionChain;
    }

    @Getter
    @AllArgsConstructor
    public enum ChainType {
        ACTIVITY_BASIC_ACTION_CHAIN("activity_basic_action", "活动基础信息校验"),
        ACTIVITY_SKU_STOCK_ACTION_CHAIN("activity_sku_stock_action", "sku 库存"),
        ACTIVITY_DEFAULT("activity_default","默认，始终返回true"),
        ;
        private final String code;
        private final String info;
    }
}
