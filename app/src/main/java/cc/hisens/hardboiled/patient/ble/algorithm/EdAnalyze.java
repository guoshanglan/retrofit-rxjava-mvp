package cc.hisens.hardboiled.patient.ble.algorithm;

import android.text.TextUtils;


import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.hisens.hardboiled.patient.db.bean.Ed;
import cc.hisens.hardboiled.patient.db.bean.EdInfo;
import cc.hisens.hardboiled.patient.utils.TimeUtils;


/**
 * @author Waiban
 * @package cc.hisens.hardboiled.algorithm
 * @fileName EdAnalyze
 * @date on 2017/6/8 17:32
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 *
 * 蓝牙数据的分析model
 */

public class EdAnalyze {

    public static final int NPT_INVALID = -1;

    public static final int NPT_NORMAL = 0;
    public static final int NPT_MIXED = 1;
    public static final int NPT_ORGANIC = 2;

    public static final int ED_INVALID = -1;
    public static final int ED_NORMAL = 0;
    public static final int ED_ORGANIC = 1;
    public static final int ED_PSYCHIC = 2;

    public static final String NPT_INTERVAL = "21:00-08:00";  //正常夜间的NPT时间

    public static final String NPT_START = "21:00";  //正常夜间的NPT开始时间
    public static final String NPT_END = "08:00";  //正常夜间的NPT结束时间
    public static final int NORMAL_NPT_SLEEP_DURATION = 6* 3600 * 1000;  //是否大于等于6H

    public static final int ONE_MINUTE = 60 * 1000;

    public static boolean isNPT(Ed ed) {
        return TimeUtils.isInTime(EdAnalyze.NPT_INTERVAL, ed.getStartTimestamp())
                && TextUtils.isEmpty(ed.getInterferenceFactor());
    }

    /**
     * 正常夜间勃起
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isNormalNPT(long startTime, long endTime) {


        return isMoreThan6h(startTime, endTime) && isNightEd(startTime);
    }


    /**
     * 判断开始时间不在范围内到结束时间在正常时间范围内 这个区间在正常区间内是否超过6个小时
     *
     * @param startTime
     * @param endTime
     * @return
     */

    public static boolean isStartMore6h(long startTime, long endTime) {



        return isMoreThan6h(startTime, endTime) && isNightEd(startTime);
    }


    /**
     判断开始时间在范围内到结束时间不在正常时间范围内 这个区间在正常区间内是否超过6个小时
     *
     * @param startTime
     * @param endTime
     * @return
     */

    public static boolean isEndMore6h(long startTime, long endTime) {


        return isMoreThan6h(startTime, endTime) && isNightEd(startTime);
    }

    /**
     判断开始时间不在范围内到结束时间不在正常时间范围内
     *
     * @param startTime
     * @param startTime
     * @return
     */

    public static boolean isNotNormalNpt(long startTime) {


        return isNightEd(startTime);
    }




    public static boolean isNightEd(long startTime) {
        return TimeUtils.isInTime(NPT_INTERVAL, startTime);
    }

    public static boolean isMoreThan6h(long startTime, long endTime) {
        return (startTime > 0 && endTime > 0) && (endTime - startTime >= NORMAL_NPT_SLEEP_DURATION);
    }

    /**
     * @param erectileStrength 勃起强度(g)
     * @param duration         持续时间(min)
     * @return
     */
    public static int analyseNPT(int erectileStrength, int duration) {
        int res = NPT_INVALID;
        if (erectileStrength >= 567) {
            if (duration >= 10) {
                res = NPT_NORMAL;
            } else if (duration < 10) {
                res = NPT_MIXED;
            }
        } else if (erectileStrength >= 283 && erectileStrength < 567) {
            res = NPT_MIXED;
        } else if (erectileStrength < 28340) {
            res = NPT_ORGANIC;
        }
        return res;
    }

    /**
     * 得到包括勃起开始时间，勃起结束时间，
     * 勃起时长，勃起强度
     * todo 周径变化
     *
     * @param modelList 从嵌入式中得到带时间戳的数据
     * @return
     */
    /*public static List<EdInfo> analyzeEdInfo(List<ErectionDataModel> list) {
        List<EdInfo> edInfoList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            // 用于计算的缓冲器大小
            int bufSize = 2;
            int initErectionData = (list.get(0).getData1() + list.get(0).getData2()) / 2;
            // 只有满足勃起强度最大值大于初始勃起强度值的70%，该次勃起有效
            int minAvailableErectionStrength = (int) (initErectionData * 1.7f);
            ErectionDataModel startErection = null;
            ErectionDataModel endErection = null;
            int maxErectionStrength = -1;
            // 与初始勃起强度的误差范围为3g
            int errorLimit = 2;

            // 记录初始值
            for (int i = 1; i < list.size() - bufSize; i++) {
                // 判断勃起开始时间
                if (startErection == null) {
                    boolean isStart = true;
                    for (int j = 0; j < bufSize; j++) {
                        int data = (list.get(i + j).getData1() + list.get(i + j).getData2()) / 2;
                        KLog.i("计算="+data+" "+initErectionData+" "+isStart);
                        isStart &= (Math.abs(data - initErectionData) >= errorLimit);
                    }
                    if (isStart) {
                        startErection = list.get(i);
                    }else KLog.i("isStart="+isStart);
                }

                if (startErection != null) {
                    // 记录最大勃起强度
                    if (list.get(i).getData1() >= minAvailableErectionStrength) {
                        maxErectionStrength = Math.max(maxErectionStrength, list.get(i).getData1());
                    }else {
                      KLog.i("===="+list.get(i).getData1()+" "+minAvailableErectionStrength+" "+i);
                    }
                    // 记录结束
                    boolean isEnd = false;
                    for (int j = 0; j < bufSize; j++) {
                        int data = (list.get(i + j).getData1() + list.get(i + j).getData2()) / 2;
                        isEnd |= (Math.abs(data - initErectionData) <= errorLimit);
                    }
                    if (isEnd) {
                        endErection = list.get(i + bufSize - 1);
                    }else KLog.i("isEnd="+isEnd);
                }else KLog.i("startErection="+startErection);

                // 记录该次勃起数据
                if (endErection != null) {
                    if (maxErectionStrength > 0) {
                        EdInfo edInfo = new EdInfo();
                        edInfo.setStartTime(startErection.getTimestamp());
                        edInfo.setEndTime(endErection.getTimestamp());
                        edInfo.setDuration(endErection.getTimestamp() - startErection.getTimestamp());
                        edInfo.setErectileStrength(maxErectionStrength);
                        KLog.i("力度="+edInfo.getErectileStrength()+" "+edInfo.getDuration());
                        // TODO: 2017/6/28 周径变化
                        // 添加到list
                        edInfoList.add(edInfo);
                    }else  KLog.i("maxErectionStrength="+maxErectionStrength);
                    // 重设所有数据
                    startErection = null;
                    endErection = null;
                    maxErectionStrength = -1;
                }else KLog.i("endErection="+endErection);
            }
        }
        return edInfoList;
    }*/
    public static List<EdInfo> analyzeEdInfo(List<ErectionDataModel> modelList) {

        List<EdInfo> edInfoList = new ArrayList<>();
        if (modelList == null || modelList.size() == 0) {
            return edInfoList;
        }

        //当前连续力度
        int currentStrength = 0;

        //该次连续力度的起始时间戳
        long startTimestamp = 0;

        //该次连续力度的结束时间戳
        long endTimestamp = 0;

        //最后一个不为0的力度的时间戳
        long finalStrengthTimestamp = 0;

        int startIndex = 0;
        int endIndex = 0;

        for (int i = 0; i < modelList.size(); i++) {

            ErectionDataModel dataModel = modelList.get(i);

            if (dataModel.getData1() > 0 && i != modelList.size() - 1) {

                if (dataModel.getData1() == currentStrength) {

                    if (startTimestamp == 0) {

                        startTimestamp = dataModel.getTimestamp();

                    }
                    ++endIndex;

                    //两个strength相等的数据之间有断层，开始的时间要重新计算
                    if (checkDataMiss(startTimestamp, dataModel.getTimestamp(), startIndex, endIndex)) {

                        endTimestamp = dataModel.getTimestamp();
                        EdInfo edInfo = createEdInfo(startTimestamp, endTimestamp,
                                startIndex, endIndex, currentStrength);
                        edInfoList.add(edInfo);

                        startIndex = i;
                        endIndex = startIndex + 1;
                        startTimestamp = dataModel.getTimestamp();

                    }


                } else {

                    if (startTimestamp != 0) {//一次连续力度

                        endTimestamp = dataModel.getTimestamp();

                        EdInfo edInfo = createEdInfo(startTimestamp, endTimestamp,
                                startIndex, endIndex, currentStrength);
                        edInfoList.add(edInfo);

                    }

                    startIndex = i;
                    endIndex = startIndex + 1;
                    startTimestamp = dataModel.getTimestamp();
                    currentStrength = dataModel.getData1();
                }

                finalStrengthTimestamp = dataModel.getTimestamp();

            } else if (startTimestamp != 0) {

                /**
                 * data 为0是为了填充数据包，对data为0的数据不做处理
                 * */
                if (dataModel.getData1() == 0) {

                    endTimestamp = finalStrengthTimestamp;

                } else {

                    endTimestamp = dataModel.getTimestamp();

                }

                EdInfo edInfo = createEdInfo(startTimestamp, endTimestamp,
                        startIndex, endIndex, currentStrength);
                edInfoList.add(edInfo);
                KLog.i("力度===" + edInfo.getErectileStrength() + " " + edInfo.getDuration() + " " + startTimestamp + " " + endTimestamp);
//                startTimestamp = 0;
//                currentStrength = 0;
            }
        }

        return edInfoList;
    }

    private static Map<String, Long> getDuration(long startTimestamp, long endTimestamp, int startIndex, int endIndex) {
        long duration = endTimestamp - startTimestamp;
        int times = endIndex - startIndex;
        if (duration > times * ONE_MINUTE) {
            duration = times * ONE_MINUTE;
            endTimestamp = startTimestamp + duration;
        }
        Map<String, Long> map = new HashMap<>();
        map.put("duration", duration);
        map.put("endTimeStamp", endTimestamp);
        return map;
    }


    private static EdInfo createEdInfo(long startTimestamp, long endTimestamp, int startIndex, int endIndex, int strength) {

        Map<String, Long> map = getDuration(startTimestamp, endTimestamp, startIndex, endIndex);

        EdInfo edInfo = new EdInfo();
        edInfo.setStartTime(startTimestamp);
        edInfo.setEndTime(map.get("endTimeStamp"));
        edInfo.setDuration(map.get("duration"));
        edInfo.setErectileStrength(strength);

        return edInfo;
    }

    private static boolean checkDataMiss(long startTimeStamp, long endTimeStamp, int startIndex, int endIndex) {

        long duration = endTimeStamp - startTimeStamp;
        int times = endIndex - startIndex;
        if (duration > times * ONE_MINUTE) {
            return true;
        }

        return false;
    }
}
