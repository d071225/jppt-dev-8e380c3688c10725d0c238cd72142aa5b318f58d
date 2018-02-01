package com.hletong.mob.util;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.hletong.mob.HLApplication;
import com.hletong.mob.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ddq on 2016/10/25.
 */

public class SimpleDate implements Comparable {

    public static final SimpleDateFormat formatterToSecondDefault = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat formatterHlet = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
    public static final SimpleDateFormat formatterToDay = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
    public static final SimpleDateFormat formatterToMin = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());

    /**
     * 登录需要的时间
     **/
    public static SimpleDateFormat formatterLoginDate = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());

    public static final long SECOND_MILLISECONDS = 1000;
    public static final long MINUTE_MILLISECONDS = 60 * SECOND_MILLISECONDS;
    public static final long HOUR_MILLISECONDS = 60 * MINUTE_MILLISECONDS;
    public static final long DAY_MILLISECONDS = 24 * HOUR_MILLISECONDS;
    public static final long WEEK_MILLISECONDS = 7 * DAY_MILLISECONDS;

    private int year;
    private int monthOfYear;//从1开始,非从0开始
    private int dayOfMonth;

    private int hourOfDay;
    private int minute;
    private int second;

    public SimpleDate(int year, int monthOfYear, int dayOfMonth) {
        setDate(year, monthOfYear, dayOfMonth);
    }

    public SimpleDate() {
    }

    public SimpleDate(Calendar calendar) {
        setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
        setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    public void setDate(int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
    }

    public void setTime(int hourOfDay, int minute, int second) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.second = second;
    }

    public int getYear() {
        return year;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public String dateString(boolean zeroAppend, String separator) {
        return dateString(zeroAppend, separator, false);
    }

    public String dateString(boolean zeroAppend, String separator, boolean withoutYear) {
        StringBuilder builder = new StringBuilder();

        if (!withoutYear) {
            builder.append(year);
            builder.append(separator);
        }

        if (zeroAppend && monthOfYear < 10) {
            builder.append(0);
        }
        builder.append(monthOfYear);
        builder.append(separator);
        if (zeroAppend && dayOfMonth < 10) {
            builder.append(0);
        }
        builder.append(dayOfMonth);
        return builder.toString();
    }

    public String timeString(boolean zeroAppend, String separator) {
        return timeString(zeroAppend, separator, true);
    }

    public String timeString(boolean zeroAppend, String separator, boolean withSeconds) {
        StringBuilder builder = new StringBuilder();
        if (zeroAppend && hourOfDay < 10) {
            builder.append(0);
        }
        builder.append(hourOfDay);
        builder.append(separator);
        if (zeroAppend && minute < 10) {
            builder.append(0);
        }
        builder.append(minute);

        if (!withSeconds)
            return builder.toString();

        builder.append(separator);
        if (zeroAppend && second < 10) {
            builder.append(0);
        }
        builder.append(second);
        return builder.toString();
    }

    public String timeString() {
        return timeString(true, ":");
    }

    public String chineseFormat(boolean appendZero, boolean withYear) {
        StringBuilder builder = new StringBuilder();

        if (withYear) {
            builder.append(year);
            builder.append("年");
        }

        if (appendZero && monthOfYear < 10) {
            builder.append(0);
        }
        builder.append(monthOfYear);
        builder.append("月");
        if (appendZero && dayOfMonth < 10) {
            builder.append(0);
        }
        builder.append(dayOfMonth);
        builder.append("日");
        return builder.toString();
    }

    public String chineseFormat() {
        return chineseFormat(false, true);
    }

    public String chineseFormatWithoutYear() {
        return chineseFormat(false, false);
    }

    /**
     * 时间显示的分类
     * 今天，昨天，前天，前天以前的显示星期几（本周），上周，更早
     *
     * @return
     */
    public String getDateDescription(Resources mResources) {
        Calendar calendar = getTimeInMillsSecond();
        Calendar today = getToday();
        if (sameDay(calendar, today)) {
            return mResources.getString(R.string.today);
        } else if (sameDay(calendar, getYesterday())) {
            return mResources.getString(R.string.yesterday);
        } else if (sameDay(calendar, getTheDayBeforeYesterday())) {
            return mResources.getString(R.string.the_day_before_yesterday);
        }

        int weeks = weekDiffer(today, calendar);
        if (weeks >= 2) {
            return mResources.getString(R.string.long_ago);
        } else if (weeks == 1) {
            return mResources.getString(R.string.last_week);
        } else {
            String[] week = mResources.getStringArray(R.array.week);
            return week[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        }
    }

    /**
     * 时间显示
     * 今天显示今天和时分，昨天显示昨天和时分，前天显示前天和时分
     * 前天以前显示月日，今年以前显示年月日
     *
     * @return
     */
    public String getDateDescriptionWithTime() {
        Calendar calendar = getTimeInMillsSecond();
        Calendar today = getToday();
        if (sameDay(calendar, today)) {
            return "今天 " + getHourWithMinute();
        } else if (sameDay(calendar, getYesterday())) {
            return "昨天 " + getHourWithMinute();
        } else if (sameDay(calendar, getTheDayBeforeYesterday())) {
            return "前天 " + getHourWithMinute();
        } else {
            if (sameYear(calendar, today)) {
                return chineseFormatWithoutYear();
            } else {
                return chineseFormat();
            }
        }
    }

    private String getHourWithMinute() {
        return timeString(true, ":", false);
    }

    private boolean sameDay(Calendar mCalendar, Calendar mCalendar2) {
        return mCalendar.get(Calendar.YEAR) == mCalendar2.get(Calendar.YEAR)
                && mCalendar.get(Calendar.MONTH) == mCalendar2.get(Calendar.MONTH)
                && mCalendar.get(Calendar.DAY_OF_MONTH) == mCalendar2.get(Calendar.DAY_OF_MONTH);
    }

    private boolean sameYear(Calendar mCalendar, Calendar mCalendar2) {
        return mCalendar.get(Calendar.YEAR) == mCalendar2.get(Calendar.YEAR);
    }

    /**
     * 两个日期的周之差
     *
     * @param today 今天
     * @param other
     * @return 2=>更早
     */
    private int weekDiffer(Calendar today, Calendar other) {

        if (today.get(Calendar.YEAR) == other.get(Calendar.YEAR)) {//同年
            return today.get(Calendar.WEEK_OF_YEAR) - other.get(Calendar.WEEK_OF_YEAR);
        }

        //校准时间到同一时刻,方便对比
        Calendar ct = Calendar.getInstance();
        ct.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 0, 0, 1);//取00:00:01
        Calendar co = Calendar.getInstance();
        co.set(other.get(Calendar.YEAR), other.get(Calendar.MONTH), other.get(Calendar.DAY_OF_MONTH), 0, 0, 1);//取00:00:01
        //两个时间之间的时间差
        long delta = Math.abs(ct.getTimeInMillis() - co.getTimeInMillis());
        if (delta > 13 * DAY_MILLISECONDS) {//已经超出两周
            return 2;//更早
        } else if (delta > 6) {//已经超出一周
            return 1;
        } else {
            //把时间往前平移一周
            ct.setTimeInMillis(ct.getTimeInMillis() - WEEK_MILLISECONDS);
            co.setTimeInMillis(co.getTimeInMillis() - WEEK_MILLISECONDS);
            return ct.get(Calendar.WEEK_OF_YEAR) - co.get(Calendar.WEEK_OF_YEAR);
        }
    }

    private Calendar getTimeInMillsSecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear - 1, dayOfMonth, hourOfDay, minute, second);
        calendar.setTimeInMillis(calendar.getTimeInMillis() + HLApplication.getDelta());
        return calendar;
    }

    private Calendar getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis() + HLApplication.getDelta());
        return calendar;
    }

    private Calendar getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis() + HLApplication.getDelta() - DAY_MILLISECONDS);
        return calendar;
    }

    private Calendar getTheDayBeforeYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis() + HLApplication.getDelta() - 2 * DAY_MILLISECONDS);
        return calendar;
    }

    public String dateString() {
        return dateString(true, ".");
    }

    public static SimpleDate parse(String s) {
        SimpleDate sd;

        Date date = parseDate(s);
        if (date == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        sd = new SimpleDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        sd.setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));

        return sd;
    }

    public static Date parseDate(String s) {
        if (s == null)
            return null;
        SimpleDateFormat sdf = null;
        Date date = null;
        if (s.length() == 6) {
            sdf = new SimpleDateFormat("HHmmss", Locale.getDefault());
        } else if (s.length() == 5) {
            sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        } else if (s.length() == 8) {
            sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        } else if (s.length() == 10) {
            sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        } else if (s.length() == 13) {
            try {
                date = new Date(Long.parseLong(s));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (s.length() == 14) {
            sdf = formatterHlet;
        } else {
            sdf = formatterToSecondDefault;
        }

        try {
            if (date == null && sdf != null)
                date = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static SimpleDate parseMilliSecondsFormat(String s) {
        if (s == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(s));
        SimpleDate sd = new SimpleDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        sd.setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        return sd;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        SimpleDate sd = (SimpleDate) o;
        if (year != sd.year)
            return year - sd.year;

        if (monthOfYear != sd.monthOfYear)
            return monthOfYear - sd.monthOfYear;

        if (dayOfMonth != sd.dayOfMonth)
            return dayOfMonth - sd.dayOfMonth;

        if (hourOfDay != sd.hourOfDay)
            return hourOfDay - sd.hourOfDay;

        if (minute != sd.minute)
            return minute - sd.minute;

        if (second != sd.second)
            return second - sd.second;
        return 0;
    }

    public static String format(String strDate) {
        Date date = parseStrDate(strDate, null);
        if (date != null) {
            return formatDate(date);
        }
        return null;
    }

    public static String format(String strDate, SimpleDateFormat simpleDateFormat) {
        Date date = parseStrDate(strDate, simpleDateFormat);
        if (date != null) {
            return formatDate(date);
        }
        return null;
    }

    public static Date parseStrDate(String strDate, SimpleDateFormat simpleDateFormat) {
        try {
            if (simpleDateFormat != null) {
                return simpleDateFormat.parse(strDate);
            }
            return formatterToSecondDefault.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDate(Date date) {
        return formatDate(date, formatterToDay);
    }

    public static String formatDate(Date date, SimpleDateFormat dateFormat) {
        return dateFormat.format(date);
    }

}
