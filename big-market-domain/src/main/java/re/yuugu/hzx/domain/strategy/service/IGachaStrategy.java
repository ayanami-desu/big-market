package re.yuugu.hzx.domain.strategy.service;

import re.yuugu.hzx.domain.strategy.model.entity.GachaAwardEntity;
import re.yuugu.hzx.domain.strategy.model.entity.GachaFactorEntity;

public interface IGachaStrategy {

    GachaAwardEntity performGacha(GachaFactorEntity gachaFactor);
}
