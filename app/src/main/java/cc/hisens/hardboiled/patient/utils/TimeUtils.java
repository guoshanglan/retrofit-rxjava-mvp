package cc.hisens.hardboiled.patient.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.utils
 * @fileName TimeUtils
 * @date on 2017/6/20 11:21
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class TimeUtils {
    /**
     * Constant that contains the amount of milliseconds in a second
     */
    private static final long ONE_SECOND = 1000L;
    public static final int NORMAL_NPT_SLEEP_DURATION = 6* 60 ;  //是否大于等于6H

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime 时间区间,半闭合,如[10:00-20:00)
     * @param time       需要判断的时间
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTime(String sourceTime, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String curTime = sdf.format(new Date(time));
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        try {
            long now = sdf.parse(curTime).getTime();
            long start = sdf.parse(args[0]).getTime();
            long end = sdf.parse(args[1]).getTime();
            if (args[1].equals("00:00")) {
                args[1] = "24:00";
            }
            if (end < start) {
                return !(now >= end && now < start);
            } else {
                return (now >= start && now < end);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
    }

    /**
     * 判断某一时间段在指定时间段时间是否超过6个小h
     *
     * @param sourceTime 时间区间,半闭合,如[10:00-20:00)
     * @param startTime       需要判断的开始时间
     * @return endTime  需要判断的结束时间
     * @throws IllegalArgumentException
     */

    public static boolean isMore6h(String sourceTime,long startTime,long endTime){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String curTime = sdf.format(new Date(startTime));
        String EndTime= sdf.format(new Date(endTime));

        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        if (EndTime == null || !EndTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] inputstart=curTime.split(":");
        String[]inputend=EndTime.split(":");
        String[] args = sourceTime.split("-");
        String[] mstart=args[0].split(":"); //规定开始时间字符串分割数组
        String[]mend=args[1].split(":"); //规定结束时间
        try {
            long inputStart = Integer.valueOf(inputstart[0])*60+ Integer.valueOf(inputstart[1]);  //监测开始时间,改为分钟
            long inputEnd=Integer.valueOf(inputend[0])*60+ Integer.valueOf(inputend[1]);  //监测结束时间，改为分钟
            long start = Integer.valueOf(mstart[0])*60+ Integer.valueOf(mstart[1]);
            long end = Integer.valueOf(mend[0])*60+ Integer.valueOf(mend[1]);

            if (inputStart<end){  //如果监测开始时间小于我们规定的结束时间，就在原有的基础上加一天，
                inputStart=inputStart+24*60;
                inputEnd=inputEnd+24* 60;
            }
            if (inputEnd<inputStart){  //如果监测结束时间小于我们规定的开始时间，也在原有的基础上加一天
                inputEnd=inputEnd+24* 60;
            }
             end=end+24*60;

            if (inputStart-start>=0&&inputStart-end<=0){  //开始时间大于规定开始时间小于规定结束时间
                if (inputEnd<end){
                    return  inputEnd-inputStart>=NORMAL_NPT_SLEEP_DURATION;
                }else{

                    return  end-inputStart>=NORMAL_NPT_SLEEP_DURATION;
                }

            }else if (inputStart-start<0){   //开始时间小于规定的开始时间

                if (inputEnd<=start){  //这个就不算正常范围，

                    return false;
                }else if (inputEnd>start&&inputEnd<=end){

                    return  inputEnd-start>=NORMAL_NPT_SLEEP_DURATION;
                }else{  //否则的话肯定超过了6H

                    return true;
                }

            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }

    }






    public static int millisToMin(long millis) {
        return ((int) (millis / 60000));
    }


    /**
     * Converts milliseconds to seconds
     *
     * @param timeInMillis
     * @return The equivalent time in seconds
     */
    public static int toSecs(long timeInMillis) {
        // Rounding the result to the ceiling, otherwise a
        // System.currentTimeInMillis that happens right before a new Element
        // instantiation will be seen as 'later' than the actual creation time
        return (int) Math.ceil((double) timeInMillis / ONE_SECOND);
    }

    /**
     * Converts seconds to milliseconds, with a precision of 1 second
     *
     * @param timeInSecs the time in seconds
     * @return The equivalent time in milliseconds
     */
    public static long toMillis(int timeInSecs) {
        return timeInSecs * ONE_SECOND;
    }

    /**
     * Converts seconds to milliseconds, with a precision of 1 second
     *
     * @param timeInSecs the time in seconds
     * @return The equivalent time in milliseconds
     */
    public static long toMillis(long timeInSecs) {
        return timeInSecs * ONE_SECOND;
    }

    /**
     * Converts a long seconds value to an int seconds value and takes into account overflow
     * from the downcast by switching to Integer.MAX_VALUE.
     *
     * @param seconds Long value
     * @return Same int value unless long > Integer.MAX_VALUE in which case MAX_VALUE is returned
     */
    public static int convertTimeToInt(long seconds) {
        if (seconds > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return (int) seconds;
        }
    }

    /**
     * 返回当前时间的格式为 yyyyMMddHHmmss
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(System.currentTimeMillis());
    }

    //毫秒转秒
    public static String long2String(long time) {
        //毫秒转秒
        int sec = (int) time / 1000;
        int min = sec / 60;    //分钟
        sec = sec % 60;        //秒
        if (min < 10) {    //分钟补0
            if (sec < 10) {    //秒补0
                return "0" + min + ":0" + sec;
            } else {
                return "0" + min + ":" + sec;
            }
        } else {
            if (sec < 10) {    //秒补0
                return min + ":0" + sec;
            } else {
                return min + ":" + sec;
            }
        }
    }

    /**
     * 毫秒转化时分秒毫秒
     *
     * @param ms
     * @return
     */
    public static String formatTime(double ms) {
        Double d = new Double(ms);
        return formatTime(d.longValue());
    }

    public static String formatTime(long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "d");
        }
        if (hour > 0) {
            sb.append(hour + "h");
        }
        if (minute > 0) {
            sb.append(minute + "′");
        }
        if (second > 0) {
            sb.append(second + "″");
        }
        return sb.toString();
    }



    /**
     * 时间戳转换成字符窜
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将字符串转为时间戳
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

}
