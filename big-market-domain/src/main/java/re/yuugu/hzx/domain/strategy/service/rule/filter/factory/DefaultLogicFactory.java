package re.yuugu.hzx.domain.strategy.service.rule.filter.factory;



import re.yuugu.hzx.domain.strategy.model.entity.RuleActionEntity;
import re.yuugu.hzx.domain.strategy.service.annotation.LogicStrategy;
import re.yuugu.hzx.domain.strategy.service.rule.filter.ILogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class DefaultLogicFactory {

    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    // 构造函数
    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public <T extends RuleActionEntity.GachaEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_LOCK("rule_lock","【抽奖中规则】抽奖n次后解锁奖品","processing"),
        RULE_LUCK_AWARD("rule_luck_award","【抽奖后规则】如抽出带锁奖品则改发其他随机奖品","after")
        ;

        private final String code;
        private final String info;
        private final String type;

        public static boolean isProcessing(String code) {
            return "processing".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }
    }

}

