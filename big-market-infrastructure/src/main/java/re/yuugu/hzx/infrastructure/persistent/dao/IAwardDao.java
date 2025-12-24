package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.Award;

import java.util.List;

@Mapper
public interface IAwardDao {
    List<Award> queryAwardList();
}
