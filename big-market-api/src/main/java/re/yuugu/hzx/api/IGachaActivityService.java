package re.yuugu.hzx.api;

import re.yuugu.hzx.api.dto.request.ActivityDrawReq;
import re.yuugu.hzx.api.dto.response.ActivityDrawRes;
import re.yuugu.hzx.api.response.Response;

import java.security.Principal;

/**
 * @ author anon
 * @ description IGachaActivityService
 * @ create 2026/1/19 16:27
 */
public interface IGachaActivityService {
    Response<Boolean> armoryActivity(Long activityId, Principal principal);
    Response<ActivityDrawRes> activityDrawAward(ActivityDrawReq activityDrawReq);
}
