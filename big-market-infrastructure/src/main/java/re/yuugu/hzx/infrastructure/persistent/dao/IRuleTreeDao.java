package re.yuugu.hzx.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import re.yuugu.hzx.infrastructure.persistent.po.RuleTree;

/**
 * @ author anon
 * @ description RuleTreeDao
 * @ create 2025/12/29 16:52
 */
@Mapper
public interface IRuleTreeDao {
    RuleTree queryRuleTreeByRootNode(String rootNode);
}
