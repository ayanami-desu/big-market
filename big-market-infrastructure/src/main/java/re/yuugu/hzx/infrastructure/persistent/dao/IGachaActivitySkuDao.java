package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.GachaActivitySku;

/**
 * @ author anon
 * @ description IGachaActivitySku
 * @ create 2026/1/4 18:07
 */
@Mapper
public interface IGachaActivitySkuDao {
    GachaActivitySku queryGachaActivitySku(Long sku);
}
