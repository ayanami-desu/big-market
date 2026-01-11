package re.yuugu.hzx.domain.acitivity.dispatch;

import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ author anon
 * @ description GachaDispatch
 * @ create 2026/1/9 16:29
 */
@Service
public class GachaDispatch implements IGachaDispatch{
    @Resource
    private IActivityRepository  activityRepository;
    @Override
    public boolean subtractActivitySkuStock(Long sku, Date endDateTime) {
        return activityRepository.subtractActivitySkuStock(sku,endDateTime);
    }
}
