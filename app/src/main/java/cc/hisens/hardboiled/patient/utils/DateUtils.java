package cc.hisens.hardboiled.patient.utils;

import com.socks.library.KLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.utils
 * @fileName DateUtils
 * @date on 2017/5/23 16:58
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class DateUtils {
    public static boolean isToday(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(date);
        int curYear = currentCalendar.get(Calendar.YEAR);
        int curMonth = currentCalendar.get(Calendar.MONTH) + 1;
        int curDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(new Date());
        int todayYear = todayCalendar.get(Calendar.YEAR);
        int todayMonth = todayCalendar.get(Calendar.MONTH) + 1;
        int todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH);
        return (curYear == todayYear && curMonth == todayMonth && curDay == todayDay);
    }

    public static int daysThisMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得当前天的开始时间
     *
     * @param date
     * @return
     */
    public static Date getDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static long dayDurationMillis() {
        return 60 * 60 * 24 * 1000L;
    }

    /**
     * 返回指定日期的月的第一天
     *
     * @return
     */
    public static Date getMonthStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的月的最后一天
     *
     * @return
     */
    public static Date getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.getMinimum(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的月的第一天
     *
     * @return
     */
    public static Date getYearStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), 0, calendar.getActualMinimum(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的月的最后一天
     *
     * @return
     */
    public static Date getYearEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR) + 1, 0, calendar.getMinimum(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的月的第一天
     *
     * @return
     */
    public static Date getMonthOfYearStart(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), month, calendar.getActualMinimum(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的月的最后一天
     *
     * @return
     */
    public static Date getMonthOfYearEnd(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), month + 1, calendar.getMinimum(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTime();
    }

    public static String dayOfWeekFormat(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.format(Locale.getDefault(), "%d.%d", calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 返回指定日期的月的第一天
     *
     * @return
     */
    public static Date getNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    public static Date parse(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(strDate);
    }

    //由出生日期获得年龄
    public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;
            } else {
                age--;
            }
        }
        return age;
    }

    public static int betweenMonths(Date src, Date dest) {
        Calendar calendarSrc = Calendar.getInstance();
        calendarSrc.setTime(src);
        Calendar calendarDest = Calendar.getInstance();
        calendarDest.setTime(dest);

        int years = Math.abs(calendarSrc.get(Calendar.YEAR) - calendarDest.get(Calendar.YEAR));
        int months = years * 12 + calendarSrc.get(Calendar.MONTH) - calendarDest.get(Calendar.MONTH);
        if (calendarSrc.get(Calendar.DAY_OF_MONTH) < calendarDest.get(Calendar.DAY_OF_MONTH)) {
            months -= 1;
        }
        return months;
    }

    public static String timestampToText(double timeStamp) {
        Date date = new Date((long) (timeStamp * 1000));
        KLog.i(date);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        return format.format(date);
    }

    /**
     * @Param second
     */
    public static Date double2Date(double second) {
        long millisecond = (long) (second * 1000);
        return new Date(millisecond);
    }

}
