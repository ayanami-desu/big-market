package re.yuugu.hzx.domain.rebate.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import re.yuugu.hzx.domain.rebate.event.SendRebateMessageEvent;
import re.yuugu.hzx.domain.rebate.model.aggregate.CreateRebateOrderAggregate;
import re.yuugu.hzx.domain.rebate.model.entity.RebateBehaviorEntity;
import re.yuugu.hzx.domain.rebate.model.entity.RebateOrderEntity;
import re.yuugu.hzx.domain.rebate.model.entity.TaskEntity;
import re.yuugu.hzx.domain.rebate.model.vo.BehaviorRebateVO;
import re.yuugu.hzx.domain.rebate.model.vo.TaskStateVO;
import re.yuugu.hzx.domain.rebate.repository.IBehaviorRebateRepository;
import re.yuugu.hzx.types.common.Constants;
import re.yuugu.hzx.types.event.BaseEvent;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ author anon
 * @ description BehaviorRebateService
 * @ create 2026/1/24 16:37
 */
@Service
public class BehaviorRebateService implements IBehaviorRebateService {
    @Resource
    private IBehaviorRebateRepository behaviorRebateRepository;
    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;

    @Override
    public List<String> saveRebateOrder(RebateBehaviorEntity rebateBehaviorEntity) {
        String userId = rebateBehaviorEntity.getUserId();
        //1. 查询数据库中返利行为对应的返利配置
        List<BehaviorRebateVO> behaviorRebateVOList = behaviorRebateRepository.queryBehaviorRebateConfig(rebateBehaviorEntity.getBehaviorType().getCode());
        if (behaviorRebateVOList == null || behaviorRebateVOList.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        //2. 循环
        for (BehaviorRebateVO behaviorRebateVO : behaviorRebateVOList) {
            String bizId = userId + Constants.UNDERLINE + behaviorRebateVO.getRebateType()
                    + Constants.UNDERLINE + rebateBehaviorEntity.getOutBusinessNo();
            // 订单对象
            RebateOrderEntity rebateOrderEntity = RebateOrderEntity.builder()
                    .userId(userId)
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .behaviorType(behaviorRebateVO.getBehaviorType())
                    .rebateDesc(behaviorRebateVO.getRebateDesc())
                    .rebateType(behaviorRebateVO.getRebateType())
                    .rebateConfig(behaviorRebateVO.getRebateConfig())
                    .bizId(bizId)
                    .outBusinessNo(rebateBehaviorEntity.getOutBusinessNo())
                    .build();
            // SendRebateMessage 消息对象
            SendRebateMessageEvent.SendRebateMessage msg = new SendRebateMessageEvent.SendRebateMessage();
            msg.setUserId(userId);
            msg.setBehaviorType(behaviorRebateVO.getBehaviorType().getCode());
            msg.setRebateType(behaviorRebateVO.getRebateType().getCode());
            msg.setBizId(bizId);
            // mq 消息对象
            BaseEvent.EventMessage<SendRebateMessageEvent.SendRebateMessage> mqMsg = sendRebateMessageEvent.buildEventMessage(msg);

            // task 对象
            TaskEntity taskEntity = TaskEntity.builder()
                    .userId(userId)
                    .topic(sendRebateMessageEvent.topic())
                    .message(mqMsg)
                    .messageId(mqMsg.getId())
                    .state(TaskStateVO.create)
                    .build();
            // 构建聚合对象
            CreateRebateOrderAggregate createRebateOrderAggregate = new CreateRebateOrderAggregate();
            createRebateOrderAggregate.setUserId(userId);
            createRebateOrderAggregate.setRebateOrderEntity(rebateOrderEntity);
            createRebateOrderAggregate.setTaskEntity(taskEntity);

            // 保存聚合对象
            behaviorRebateRepository.doSaveRebateOrderAggregate(createRebateOrderAggregate);
            result.add(rebateOrderEntity.getOrderId());
        }
        return result;
    }

    @Override
    public List<RebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo) {
        return behaviorRebateRepository.queryOrderByOutBusinessNo(userId, outBusinessNo);
    }
}
