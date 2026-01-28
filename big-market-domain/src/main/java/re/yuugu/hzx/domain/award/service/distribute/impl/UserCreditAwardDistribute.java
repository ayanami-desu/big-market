package re.yuugu.hzx.domain.award.service.distribute.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import re.yuugu.hzx.domain.award.model.aggregate.DistributeCreditAwardAggregate;
import re.yuugu.hzx.domain.award.model.entity.AwardDistributeEntity;
import re.yuugu.hzx.domain.award.model.entity.CreditAwardEntity;
import re.yuugu.hzx.domain.award.model.entity.UserAwardRecordEntity;
import re.yuugu.hzx.domain.award.repository.IAwardRepository;
import re.yuugu.hzx.domain.award.service.distribute.IAwardDistribute;
import re.yuugu.hzx.types.common.Constants;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ author anon
 * @ description UserCreditAwardDistribute
 * @ create 2026/1/27 13:34
 */
@Component("user_credit_random")
public class UserCreditAwardDistribute implements IAwardDistribute {
    @Resource
    private IAwardRepository awardRepository;

    @Override
    public void dispatchAward(AwardDistributeEntity awardDistributeEntity) {
        Integer awardId = awardDistributeEntity.getAwardId();
        String awardConfig = awardDistributeEntity.getAwardConfig();
        if (awardConfig == null || StringUtils.isEmpty(awardConfig)) {
            awardConfig = awardRepository.queryAwardConfigById(awardId);
        }
        // 生成积分值
        // awardConfig e.g. 1,100
        String[] parts = awardConfig.split(Constants.COMMA);
        if (parts.length != 2) {
            throw new RuntimeException(String.format("award_config 「%s」配置不是一个范围值，如 1,100", awardConfig));
        }
        BigDecimal credit = generateRandomCredit(new BigDecimal(parts[0]), new BigDecimal(parts[1]));
        CreditAwardEntity creditAwardEntity = CreditAwardEntity.builder()
                .creditAmount(credit)
                .userId(awardDistributeEntity.getUserId())
                .build();
        UserAwardRecordEntity userAwardRecordEntity = UserAwardRecordEntity.builder()
                .awardId(awardId)
                .userId(awardDistributeEntity.getUserId())
                .orderId(awardDistributeEntity.getOrderId())
                .build();
        DistributeCreditAwardAggregate aggregate = new DistributeCreditAwardAggregate();
        aggregate.setUserId(awardDistributeEntity.getUserId());
        aggregate.setCreditAwardEntity(creditAwardEntity);
        aggregate.setUserAwardRecord(userAwardRecordEntity);
        awardRepository.doSaveDistributeCreditAwardAggregate(aggregate);
    }

    private BigDecimal generateRandomCredit(BigDecimal min, BigDecimal max) {
        int scale = 2;
        Objects.requireNonNull(min, "min");
        Objects.requireNonNull(max, "max");

        int cmp = min.compareTo(max);
        if (cmp > 0) throw new IllegalArgumentException("min > max");
        if (cmp == 0) return min.setScale(scale, RoundingMode.HALF_UP);

        // 生成 [0, 1) 的随机 BigDecimal，分辨率与 scale 相关（多给几位更平滑）
        int p = scale + 16; // 额外多几位，减少台阶感
        BigInteger mod = BigInteger.TEN.pow(p);
        BigInteger r = new BigInteger(mod.bitLength(), ThreadLocalRandom.current()).mod(mod);

        BigDecimal u = new BigDecimal(r).movePointLeft(p); // [0,1)
        BigDecimal val = min.add(max.subtract(min).multiply(u));

        return val.setScale(scale, RoundingMode.HALF_UP);
    }
}
