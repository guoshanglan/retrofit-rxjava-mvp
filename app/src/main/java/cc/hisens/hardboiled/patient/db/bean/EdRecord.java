package cc.hisens.hardboiled.patient.db.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

//蓝牙数据记录类

public class EdRecord {

//    @SerializedName("uid")
//    public String uid;

    @SerializedName("start")
    public long start;

    @SerializedName("end")
    public long end;

    @SerializedName("drug")
    public String drug;

    @SerializedName("issleep")
    public boolean issleep;

    @SerializedName("isIntervene")
    public boolean isIntervene;

    @SerializedName("maxStrength")
    public int maxStrength;   //最大强度

    @SerializedName("maxDuration")
    public long maxDuration;  //最大持续时间

    @SerializedName("rlist")
    public EdDetail[] rlist;


    public static class EdDetail {

        @SerializedName("start")
        public long start;

        @SerializedName("strength")
        public int strength;

        @SerializedName("end")
        public long end;


        public static EdDetail convertToEdDetail(EdInfo info) {
            EdDetail detail = new EdDetail();
            detail.start = info.getStartTime()/1000;
            detail.strength = info.getErectileStrength();
            detail.end = info.getEndTime()/1000;
            return detail;
        }

    }

    public static EdRecord convertEdToRequest(Ed ed) {

        EdRecord recordRequest = new EdRecord();
//        recordRequest.uid = UserConfig.UserInfo.getUid();

        recordRequest.start = ed.getStartTimestamp()/1000;
        recordRequest.end = ed.getEndTimestamp()/1000;
        recordRequest.maxStrength = ed.getMaxStrength();
        recordRequest.maxDuration = ed.getMaxDuration();
        recordRequest.drug = ed.getInterferenceFactor();
        recordRequest.isIntervene = ed.isInterferential();
        recordRequest.issleep = ed.isSleep();

        List<EdInfo> list = ed.getEdInfos();
        EdDetail[] details = new EdDetail[list.size()];
        for (int i=0;i<list.size();i++) {
            details[i] = EdDetail.convertToEdDetail(list.get(i));
        }
        recordRequest.rlist = details;

        return recordRequest;
    }

}
