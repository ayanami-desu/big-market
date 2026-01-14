package re.yuugu.hzx.domain.acitivity.service.partake;

import re.yuugu.hzx.domain.acitivity.model.entity.ActivityPartakeEntity;
import re.yuugu.hzx.domain.acitivity.model.entity.UserGachaOrderEntity;

/**
 * @ author anon
 * @ description IGachaActivityPartake
 * @ create 2026/1/11 17:07
 */
public interface IGachaActivityPartake {
    UserGachaOrderEntity createGachaActivityPartakeOrder(ActivityPartakeEntity  activityPartakeEntity);
}
