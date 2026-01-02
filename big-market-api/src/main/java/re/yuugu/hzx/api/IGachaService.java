package re.yuugu.hzx.api;

import re.yuugu.hzx.api.dto.request.RandomGachaReq;
import re.yuugu.hzx.api.dto.request.StrategyAwardReq;
import re.yuugu.hzx.api.dto.response.RandomGachaAwardRes;
import re.yuugu.hzx.api.dto.response.StrategyAwardRes;
import re.yuugu.hzx.types.model.Response;

import java.util.List;

/**
 * @ author anon
 * @ description IGachaService
 * @ create 2025/12/31 16:38
 */
public interface IGachaService {
    Response<Boolean> armoryStrategy(Long strategyId);
    Response<List<StrategyAwardRes>> queryStrategyAwardList(StrategyAwardReq strategyAwardListReq);
    Response<RandomGachaAwardRes>  randomGacha(RandomGachaReq randomGachaReq);
}
