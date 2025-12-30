package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.RuleTreeEdge;

import java.util.List;

/**
 * @ author anon
 * @ description RuleTreeEdgeDao
 * @ create 2025/12/29 16:52
 */
@Mapper
public interface IRuleTreeEdgeDao {
    List<RuleTreeEdge> queryEdgeListByTreeId(Integer treeId);
}
