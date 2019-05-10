package cc.hisens.hardboiled.patient.db.bean;

import java.util.ArrayList;
import java.util.List;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.model
 * @fileName Ed
 * @date on 2017/6/6 17:02
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 * 蓝牙的数据接收类bean，一次测量下
 */


public class Ed extends RealmObject {

    public static final String START_SLEEP = "startTimestamp";
    public static final String NOTES = "notes";

    //开始睡眠时间
    @PrimaryKey
    private long startTimestamp;

    //结束睡眠时间
    private long endTimestamp;

    //是否在睡眠状态下测试
    private boolean isSleep;

    //是否有干扰
    private boolean isInterferential;

    //干扰因素（可为药物和其他因素，多个选项时中间用逗号隔开）
    private String interferenceFactor;

    //一次测量状态下的所有勃起数据
    private RealmList<EdInfo> edInfos;    //这样设置是为了在数据库中插入其他的数据对象

    //最大强度
    private int maxStrength;

    //最大强度持续时间
    private long maxDuration;

    //数据是否同步
    private boolean isSync = false;


    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public boolean isSleep() {
        return isSleep;
    }

    public void setSleep(boolean sleep) {
        isSleep = sleep;
    }

    public RealmList<EdInfo> getEdInfos() {
        return edInfos;
    }

    public void setEdInfos(List<EdInfo> edInfos) {
        this.edInfos = new RealmList<>();
        this.edInfos.addAll(edInfos);
    }

    public boolean isInterferential() {
        return isInterferential;
    }

    public void setInterferential(boolean interferential) {
        isInterferential = interferential;
    }

    public String getInterferenceFactor() {
        return interferenceFactor;
    }

    public void setInterferenceFactor(String interferenceFactor) {
        this.interferenceFactor = interferenceFactor;
    }

    public int getMaxStrength() {
        return maxStrength;
    }

    public void setMaxStrength(int maxStrength) {
        this.maxStrength = maxStrength;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    @Override
    public String toString() {
        return "Ed{" +
                "startTimestamp=" + startTimestamp +
                ", endTimestamp=" + endTimestamp +
                ", edInfos=" + edInfos +
                '}';
    }

    public int getMaxDurationMin() {
        return (int) Math.ceil(Math.max(1, (maxDuration / 1000 * 1.0f) / 60));
    }

    public static Ed convertEdResultToEd(EdRecord recordResult) {

        Ed ed = new Ed();
        ed.setStartTimestamp(recordResult.start*1000);
        ed.setEndTimestamp(recordResult.end*1000);
        ed.setMaxStrength(recordResult.maxStrength);
        ed.setMaxDuration(recordResult.maxDuration);
        ed.setInterferential(recordResult.isIntervene);
        ed.setInterferenceFactor(recordResult.drug);
        ed.setSleep(recordResult.issleep);

        EdRecord.EdDetail[] details = recordResult.rlist;
        if (details != null) {
            List<EdInfo> list = new ArrayList<>();
            for (int i = 0; i < details.length; i++) {
                list.add(EdInfo.convertEdDetailToEdInfo(details[i]));
            }
            ed.setEdInfos(list);
        }
        return ed;
    }


    public static List<Ed> convertEdArray(EdRecord[] results) {

        if (results == null || results.length == 0) return null;

        List<Ed> list = new ArrayList<>();

        for (int i = 0; i < results.length; i++) {
            list.add(convertEdResultToEd(results[i]));
        }

        return list;
    }

}
