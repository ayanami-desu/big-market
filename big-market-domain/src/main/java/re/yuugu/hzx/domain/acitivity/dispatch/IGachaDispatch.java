package re.yuugu.hzx.domain.acitivity.dispatch;

import java.util.Date;

/**
 * @ author anon
 * @ description IGachadispatch
 * @ create 2026/1/9 16:29
 */
public interface IGachaDispatch {
    boolean subtractActivitySkuStock(Long sku, Date endDateTime);
}
