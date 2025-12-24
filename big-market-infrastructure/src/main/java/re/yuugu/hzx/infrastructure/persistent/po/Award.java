package re.yuugu.hzx.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;
@Data
public class Award {
    private Long id;
    private Integer awardId;
    private String awardKey;
    private String awardConfig;
    private String awardDesc;
    private Date CreateTime;
    private Date UpdateTime;
}
