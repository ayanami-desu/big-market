package re.yuugu.hzx.domain.acitivity.service.armory;

/**
 * @ author anon
 * @ description IGachaArmory
 * @ create 2026/1/9 16:43
 */
public interface IGachaActivityArmory {
    boolean assembleActivityByActivityId(Long activityId);
    boolean assembleActivitySku(Long sku);
}
