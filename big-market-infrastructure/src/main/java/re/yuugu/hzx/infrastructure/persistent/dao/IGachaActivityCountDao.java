package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.GachaActivityCount;

/**
 * @ author anon
 * @ description IGachaActivityCountDao
 * @ create 2026/1/4 18:04
 */
@Mapper
public interface IGachaActivityCountDao {
    GachaActivityCount queryGachaActivityCountByActivityCountId(Long activityCountId);
}
