package re.yuugu.hzx.test.infrastructure;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import re.yuugu.hzx.infrastructure.dao.IAwardDao;
import re.yuugu.hzx.infrastructure.dao.po.Award;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardDaoTest {

    @Resource
    private  IAwardDao awardDao;

    @Test
    public void test_queryAwardList(){
        List<Award> awardList = awardDao.queryAwardList();
        log.info("测试结果:{}", JSON.toJSONString(awardList));
    }
}
