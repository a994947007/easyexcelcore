package test;

import org.easyexcel.annotation.Entity;
import org.easyexcel.annotation.FieldName;

@Entity(sheet = 0,path = "G:/交易数据2.xlsx")
public class TestData {
    @FieldName
    private String block;
    @FieldName
    private Integer blockNumber;
    @FieldName
    private Integer cumulativeGasUsed;
    @FieldName
    private Double fromScore;
    @FieldName
    private Long gasLimit;
    @FieldName("gasPrice（Gwei）")
    private Long gasPriceGwei;
    @FieldName("gasPrice（ETH）")
    private Integer gasPriceETH;

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public Integer getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Integer blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Integer getCumulativeGasUsed() {
        return cumulativeGasUsed;
    }

    public void setCumulativeGasUsed(Integer cumulativeGasUsed) {
        this.cumulativeGasUsed = cumulativeGasUsed;
    }

    public Double getFromScore() {
        return fromScore;
    }

    public void setFromScore(Double fromScore) {
        this.fromScore = fromScore;
    }

    public Long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(Long gasLimit) {
        this.gasLimit = gasLimit;
    }

    public Long getGasPriceGwei() {
        return gasPriceGwei;
    }

    public void setGasPriceGwei(Long gasPriceGwei) {
        this.gasPriceGwei = gasPriceGwei;
    }

    public Integer getGasPriceETH() {
        return gasPriceETH;
    }

    public void setGasPriceETH(Integer gasPriceETH) {
        this.gasPriceETH = gasPriceETH;
    }

    @Override
    public String toString() {
        return "TestData{" +
                "block='" + block + '\'' +
                ", blockNumber=" + blockNumber +
                ", cumulativeGasUsed=" + cumulativeGasUsed +
                ", fromScore=" + fromScore +
                ", gasLimit=" + gasLimit +
                ", gasPriceGwei=" + gasPriceGwei +
                ", gasPriceETH=" + gasPriceETH +
                '}';
    }
}
