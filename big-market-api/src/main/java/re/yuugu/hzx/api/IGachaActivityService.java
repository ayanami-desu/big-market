package re.yuugu.hzx.api;

import re.yuugu.hzx.api.dto.request.ActivityDailySignReq;
import re.yuugu.hzx.api.dto.request.ActivityDrawReq;
import re.yuugu.hzx.api.dto.response.ActivityDrawRes;
import re.yuugu.hzx.api.response.Response;

/**
 * @ author anon
 * @ description IGachaActivityService
 * @ create 2026/1/19 16:27
 */
public interface IGachaActivityService {
    Response<Boolean> armoryActivity(Long activityId);
    Response<ActivityDrawRes> activityDrawAward(ActivityDrawReq activityDrawReq);
    Response<Boolean> activityDailySign(ActivityDailySignReq  activityDailySignReq);
    Response<Boolean> hasActivityDailySign(ActivityDailySignReq  activityDailySignReq);
}
