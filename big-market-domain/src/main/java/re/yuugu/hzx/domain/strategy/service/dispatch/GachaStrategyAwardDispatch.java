package re.yuugu.hzx.domain.strategy.service.dispatch;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.strategy.po.AliasTable;
import re.yuugu.hzx.domain.strategy.repository.IStrategyRepository;

import javax.annotation.Resource;
import java.security.SecureRandom;

/**
 * @ author anon
 * @ description GachaStrategyAwardDispatch
 * @ create 2026/1/19 17:19
 */
@Service
public class GachaStrategyAwardDispatch implements IGachaStrategyAwardDispatch {
    private static final ThreadLocal<SecureRandom> TLS_SR =
            ThreadLocal.withInitial(SecureRandom::new);

    @Resource
    private IStrategyRepository strategyRepository;
    @Override
    public Integer getRandomAwardId(String key) {
        AliasTable table = strategyRepository.getStrategyAwardRateSearchTable(key);
        return getInteger(table);
    }

    @Override
    public Integer getRandomAwardId(String key, String ruleWeight) {
        AliasTable table = strategyRepository.getStrategyAwardRateSearchTable(key + "_" + ruleWeight);
        return getInteger(table);
    }

    @NonNull
    private Integer getInteger(AliasTable table) {
        if (table == null) throw new IllegalStateException("alias table missing");
        SecureRandom sr = TLS_SR.get();
        int column = sr.nextInt(table.getN());
        double u = sr.nextDouble();
        int idx = (u < table.getProbe()[column]) ? column : table.getAlias()[column];
        return table.getAwardIds()[idx];
    }
}
