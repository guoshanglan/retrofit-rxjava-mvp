package cc.hisens.hardboiled.patient.ble.algorithm;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.algorithm
 * @fileName ErectionDataModel
 * @date on 2017/6/26 17:17
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class ErectionDataModel {

    private int data1;
    private int data2;
    private long timestamp;

    public ErectionDataModel(int data1, long timestamp) {
        this.data1 = data1;
        this.timestamp = timestamp;
    }

    public int getData1() {
        return data1;
    }

    public void setData1(int data1) {
        this.data1 = data1;
    }

    public int getData2() {
        return data2;
    }

    public void setData2(int data2) {
        this.data2 = data2;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ErectionDataModel{" +
                "data1=" + data1 +
                ", timestamp=" + timestamp +
                '}';
    }

}
