package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.RuleTreeNode;

import java.util.List;

/**
 * @ author anon
 * @ description RuleTreeNodeDao
 * @ create 2025/12/29 16:52
 */
@Mapper
public interface IRuleTreeNodeDao {
    List<RuleTreeNode> queryNodeListByTreeId(Integer treeId);
}
