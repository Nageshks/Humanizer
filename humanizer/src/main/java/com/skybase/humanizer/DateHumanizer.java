package com.skybase.humanizer;

import android.annotation.SuppressLint;

import androidx.annotation.IntDef;

import com.skybase.humanizer.helper.DateFormats;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


@SuppressLint("SimpleDateFormat")
public class DateHumanizer {
    /**
     * This Type Return String associate with given date as Today, #Something# ago.
     * This type ignore timeFlag
     */
    public static final int TYPE_PRETTY_EVERYTHING = 420;
    /**
     * This Type Return String associate with given date as Today, tomorrow, yesterday only else date.
     */
    public static final int TYPE_PRETTY_TODAY = 653;
    /**
     * This Type Return date as dd MMM yy format.
     */
    public static final int TYPE_DD_MMM_YYYY = 563;
    /**
     * This Type Return date as dd MMM yy format.
     */
    public static final int TYPE_EEE_DD_MMM_YYYY = 533;
    /**
     * This Type Return date as dd-MM-yyyy format.
     */
    public static final int TYPE_DD_MM_YYYY = 368;
    /**
     * This Type Return date as dd MMM format.
     */
    public static final int TYPE_DD_MMM = 789;
    /**
     * This Type disable date
     */
    public static final int TYPE_DATE_DISABLE = 969;


    /**
     * This Type define time in HH-MM format.
     */
    public static final int TYPE_TIME_HH_MM = 449;

    /**
     * This Type define time in HH-MM-A format.
     */
    public static final int TYPE_TIME_HH_MM_A = 450;

    /**
     * This Type define time in HH-MM-A format.
     */
    public static final int TYPE_TIME_DISABLE = 451;

    public static String humanize(String dateValue, @HumanizerTypeDate int dateFlag, @HumanizerTypeTime int timeFlag) {
        if (dateValue == null) {
            return "";
        }
        if (!dateValue.contains("T")) {
            return "";
        }
        try {
            switch (dateFlag) {
                case TYPE_PRETTY_EVERYTHING:
                    return convertToPrettyEverything(dateValue);
                case TYPE_PRETTY_TODAY:
                    return convertToPrettyToday(dateValue, timeFlag);
                case TYPE_DATE_DISABLE:
                    return convertToTimeOnly(dateValue, timeFlag);
                case TYPE_DD_MMM_YYYY:
                    return convertToDateLMonthYear(dateValue, timeFlag);
                case TYPE_DD_MM_YYYY:
                    return convertToDateMonthYear(dateValue, timeFlag);
                case TYPE_DD_MMM:
                    return convertToDateMonth(dateValue, timeFlag);
                case TYPE_EEE_DD_MMM_YYYY:
                    return convertToDayDateMonthYear(dateValue, timeFlag);

            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public static String humanize(String dateValue, @HumanizerTypeDate int dateFlag) {
        return humanize(dateValue, dateFlag, TYPE_TIME_DISABLE);
    }

    public static String humanize(String dateValue) {
        return humanize(dateValue, TYPE_DD_MMM_YYYY, TYPE_TIME_DISABLE);
    }

    private static String convertToDateLMonthYear(String dateValue, @HumanizerTypeTime int timeFlag) {
        String format = DateFormats.DATE_LMONTH_YEAR;
        switch (timeFlag) {
            case TYPE_TIME_DISABLE:
                format = DateFormats.DATE_LMONTH_YEAR;
                break;
            case TYPE_TIME_HH_MM:
                format = DateFormats.DATE_LMONTH_YEAR_TIME;
                break;
            case TYPE_TIME_HH_MM_A:
                format = DateFormats.DATE_LMONTH_YEAR_TIME_A;
                break;
        }

        Calendar dateCalender = getStringISODateToCalenderObject(dateValue);
        return new SimpleDateFormat(format).format(dateCalender.getTime());
    }

    private static String convertToDateMonthYear(String dateValue, @HumanizerTypeTime int timeFlag) {
        String format = DateFormats.DATE_MONTH_YEAR;
        switch (timeFlag) {
            case TYPE_TIME_DISABLE:
                format = DateFormats.DATE_MONTH_YEAR;
                break;
            case TYPE_TIME_HH_MM:
                format = DateFormats.DATE_MONTH_YEAR_TIME;
                break;
            case TYPE_TIME_HH_MM_A:
                format = DateFormats.DATE_MONTH_YEAR_TIME_A;
                break;
        }

        Calendar dateCalender = getStringISODateToCalenderObject(dateValue);
        return new SimpleDateFormat(format).format(dateCalender.getTime());
    }

    private static String convertToDateMonth(String dateValue, @HumanizerTypeTime int timeFlag) {
        String format = DateFormats.DATE_MONTH_ONLY;
        switch (timeFlag) {
            case TYPE_TIME_DISABLE:
                format = DateFormats.DATE_MONTH_ONLY;
                break;
            case TYPE_TIME_HH_MM:
                format = DateFormats.DATE_MONTH_ONLY_TIME;
                break;
            case TYPE_TIME_HH_MM_A:
                format = DateFormats.DATE_MONTH_ONLY_TIME_A;
                break;
        }

        Calendar dateCalender = getStringISODateToCalenderObject(dateValue);
        return new SimpleDateFormat(format).format(dateCalender.getTime());
    }

    private static String convertToDayDateMonthYear(String dateValue, @HumanizerTypeTime int timeFlag) {
        String format = DateFormats.DAY_DATE_MONTH_YEAR;
        switch (timeFlag) {
            case TYPE_TIME_DISABLE:
                format = DateFormats.DAY_DATE_MONTH_YEAR;
                break;
            case TYPE_TIME_HH_MM:
                format = DateFormats.DAY_DATE_MONTH_YEAR_TIME;
                break;
            case TYPE_TIME_HH_MM_A:
                format = DateFormats.DAY_DATE_MONTH_YEAR_TIME_A;
                break;
        }

        Calendar dateCalender = getStringISODateToCalenderObject(dateValue);
        return new SimpleDateFormat(format).format(dateCalender.getTime());
    }

    private static String convertToTimeOnly(String dateValue, @HumanizerTypeTime int timeFlag) {
        String format = DateFormats.DISABLE_DATE_TIME_A;
        switch (timeFlag) {
            case TYPE_TIME_DISABLE:
                throw new IllegalArgumentException("TYPE_TIME_DISABLE is not allowed when TYPE_DATE_DISABLE flag is set");
            case TYPE_TIME_HH_MM:
                format = DateFormats.DISABLE_DATE_TIME;
                break;
            case TYPE_TIME_HH_MM_A:
                format = DateFormats.DISABLE_DATE_TIME_A;
                break;
        }

        Calendar dateCalender = getStringISODateToCalenderObject(dateValue);
        return new SimpleDateFormat(format).format(dateCalender.getTime());
    }

    private static String convertToPrettyToday(String dateValue, @HumanizerTypeTime int timeFlag) {
        Calendar dateCalender = getStringISODateToCalenderObject(dateValue);

        Calendar now = Calendar.getInstance();

        if (dateCalender.get(Calendar.MONTH) == now.get(Calendar.MONTH) && dateCalender.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            int period = dateCalender.get(Calendar.DATE) - now.get(Calendar.DATE);

            switch (period) {
                case 0:
                    return "Today";
                case 1:
                    return "Tomorrow";
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    return "In " + period + " days";
                case -1:
                    return "Yesterday";
                case -2:
                case -3:
                case -4:
                case -5:
                case -6:
                case -7:
                    return (-period) + " days ago";
                default:
                    return convertToDateMonth(dateValue, timeFlag);
            }
        } else return convertToDateLMonthYear(dateValue, timeFlag);
    }

    private static String convertToPrettyEverything(String dateValue) {
        Calendar dateCalender = resetCalendarTime(getStringISODateToCalenderObject(dateValue));

        Calendar now = resetCalendarTime(Calendar.getInstance());

        long diffInMillis = dateCalender.getTimeInMillis() - now.getTimeInMillis();
        Long dayDiff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        switch (dayDiff.intValue()) {
            case 0:
                return "Today";
            case 1:
                return "Tomorrow";
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return "In " + dayDiff.intValue() + " days";
            case -1:
                return "Yesterday";
            case -2:
            case -3:
            case -4:
            case -5:
            case -6:
            case -7:
                return (-dayDiff.intValue()) + " days ago";
            default:
                return getRelativeDateString(dayDiff.intValue(), dateValue);
        }
    }

    private static String getRelativeDateString(Integer value, String dateValue) {
        int positiveValue = value < 0 ? -value : value;
        if (positiveValue > 7 && positiveValue <= 14) {
            if (value < 0) return "1 week ago";
            else return "In one week";
        } else if (positiveValue > 14 && positiveValue <= 21) {
            if (value < 0) return "2 weeks ago";
            else return "In 2 weeks";
        } else if (positiveValue > 21 && positiveValue <= 30) {
            if (value < 0) return "3 weeks ago";
            else return "In 3 weeks";
        } else if (positiveValue > 30 && positiveValue <= 60) {
            if (value < 0) return "Last month";
            else return "Next month";
        } else if (positiveValue > 60 && positiveValue <= 90) {
            if (value < 0) return "2 months ago";
            else return "In 2 months";
        } else if (positiveValue > 90 && positiveValue <= 120) {
            if (value < 0) return "3 months ago";
            else return "In 3 months";
            //3 month
        } else if (positiveValue > 120 && positiveValue <= 150) {
            if (value < 0) return "4 months ago";
            else return "In 4 months";
            //4 month
        } else if (positiveValue > 150 && positiveValue <= 180) {
            if (value < 0) return "5 months ago";
            else return "In 5 months";
            //5 month
        } else if (positiveValue > 180 && positiveValue <= 210) {
            if (value < 0) return "6 months ago";
            else return "In 6 months";
            //6 month
        } else if (positiveValue > 210 && positiveValue <= 240) {
            if (value < 0) return "7 months ago";
            else return "In 7 months";
            //7 month
        } else if (positiveValue > 240 && positiveValue <= 270) {
            if (value < 0) return "8 months ago";
            else return "In 8 months";
            //8 month
        } else if (positiveValue > 270 && positiveValue <= 300) {
            if (value < 0) return "9 months ago";
            else return "In 9 months";
            //9 month
        } else if (positiveValue > 300 && positiveValue <= 330) {
            if (value < 0) return "10 months ago";
            else return "In 10 months";
            //10 month
        } else if (positiveValue > 330 && positiveValue <= 360) {
            //10 month
            if (value < 0) return "11 months ago";
            else return "In 11 months";
        } else if (positiveValue > 360 && positiveValue <= 720) {
            if (value < 0) return "Last Year";
            else return "Next Year";
        } else if (positiveValue > 720) {
            if (value < 0) return "1+ years ago";
            else return "In 1+ years";
        } else {
            return convertToDateLMonthYear(dateValue, TYPE_TIME_DISABLE);
        }
    }

    private static Calendar resetCalendarTime(Calendar sourceCalendar) {
        sourceCalendar.set(Calendar.HOUR_OF_DAY, 0);
        sourceCalendar.set(Calendar.MINUTE, 0);
        sourceCalendar.set(Calendar.SECOND, 0);
        sourceCalendar.set(Calendar.MILLISECOND, 0);
        return sourceCalendar;
    }

    /**
     * Return dateValue to equivalent Calender Instance
     */
    private static Calendar getStringISODateToCalenderObject(String dateValue) {
        try {
            Calendar cal = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat(DateFormats.ISO_FORMAT);

            cal.setTime(df.parse(dateValue));
            return cal;
        } catch (ParseException e) {
            return Calendar.getInstance();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_PRETTY_EVERYTHING, TYPE_PRETTY_TODAY, TYPE_DD_MMM_YYYY, TYPE_DD_MM_YYYY, TYPE_DD_MMM, TYPE_DATE_DISABLE, TYPE_EEE_DD_MMM_YYYY})
    private @interface HumanizerTypeDate {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_TIME_HH_MM, TYPE_TIME_HH_MM_A, TYPE_TIME_DISABLE})
    private @interface HumanizerTypeTime {
    }
}