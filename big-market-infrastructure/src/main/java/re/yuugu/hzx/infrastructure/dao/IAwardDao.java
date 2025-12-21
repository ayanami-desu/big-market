package re.yuugu.hzx.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.dao.po.Award;

import java.util.List;

@Mapper
public interface IAwardDao {
    List<Award> queryAwardList();
}
