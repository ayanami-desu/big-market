package re.yuugu.hzx.domain.award.service.distribute;

import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.award.model.entity.AwardDistributeEntity;

import java.util.Map;

/**
 * @ author anon
 * @ description AwardDistributeService
 * @ create 2026/1/27 13:39
 */
@Service
public class AwardDistributeService {
    private final Map<String,IAwardDistribute> awardDistributeGroup;
    public AwardDistributeService(Map<String, IAwardDistribute> awardDistributeGroup) {
        this.awardDistributeGroup = awardDistributeGroup;
    }

    public void distribute(AwardDistributeEntity awardDistributeEntity) {
        String awardKey = awardDistributeEntity.getAwardKey();
        IAwardDistribute distribute = awardDistributeGroup.get(awardKey);
        if(distribute==null){
            throw new RuntimeException("分发奖品，奖品" + awardKey + "对应的服务不存在");
        }
        distribute.dispatchAward(awardDistributeEntity);
    }
}
