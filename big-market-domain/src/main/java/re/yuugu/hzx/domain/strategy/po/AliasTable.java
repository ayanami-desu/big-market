package re.yuugu.hzx.domain.strategy.po;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class AliasTable implements Serializable {
    private int[] awardIds;
    private double[] probe;  // 整数版概率阈值
    private int[] alias;

    public AliasTable() {}
    public AliasTable(int[] awardIds, double[] probe, int[] alias) {
        this.awardIds = awardIds;
        this.probe = probe;
        this.alias = alias;
    }
    public int getN(){
        return awardIds.length;
    }
}
