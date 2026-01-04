package re.yuugu.hzx.domain.acitivity.service;

import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.acitivity.repository.IActivityRepository;

/**
 * @ author anon
 * @ description GachaActivityService
 * @ create 2026/1/4 21:31
 */
@Service
public class GachaActivityService extends AbstractGachaActivity{
    public GachaActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }
}
