package cc.hisens.hardboiled.patient.db.bean;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.model
 * @fileName EdDetail
 * @date on 2017/5/20 14:39
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

//蓝牙一次测量下的所有测试数据的bean
public class EdInfo extends RealmObject {

    @PrimaryKey
    private long startTime;   //一次数据开始时间
    private long endTime;   //一次数据的结束时间
    private long duration;    //持续时间
    private int erectileStrength;  //强度

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getErectileStrength() {
        return erectileStrength;
    }

    public void setErectileStrength(int erectileStrength) {
        this.erectileStrength = erectileStrength;
    }


    @Override
    public String toString() {
        return "EdDetail{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", erectileStrength=" + erectileStrength +
                '}';
    }

    public static EdInfo convertEdDetailToEdInfo(EdRecord.EdDetail detail) {

        EdInfo edInfo = new EdInfo();
        edInfo.setStartTime(detail.start*1000);
        edInfo.setEndTime(detail.end*1000);
        edInfo.setDuration((detail.end - detail.start)*1000);
        edInfo.setErectileStrength(detail.strength);

        return edInfo;
    }
}
