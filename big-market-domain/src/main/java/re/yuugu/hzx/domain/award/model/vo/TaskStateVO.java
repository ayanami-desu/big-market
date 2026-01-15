package re.yuugu.hzx.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ author anon
 * @ description TaskStateVO
 * @ create 2026/1/15 19:42
 */
@Getter
@AllArgsConstructor
public enum TaskStateVO {
    create("create","任务已创建"),
    completed("completed","任务已完成"),
    fail("fail","任务失败")
    ;

    private final String code;
    private final String info;
}
