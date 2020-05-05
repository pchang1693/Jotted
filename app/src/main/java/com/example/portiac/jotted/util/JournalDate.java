package com.example.portiac.jotted.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class JournalDate {

    private static String datePattern = "MMMM d, yyyy";
    private static String JSONStringPattern = "yyyy-MM-dd HH:mm:ss";

    public static Date currentDate() {
        return Calendar.getInstance().getTime();
    }

    public static String welcomeString(Date date) {
        String ret;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour >= 4 && hour < 12) {    // morning: 4:00 am - 11:59 am
            ret = "Good morning!";
        } else if (hour >= 12 && hour < 18) {    // afternoon: 12 pm - 5:59 pm
            ret = "Good afternoon!";
        } else {    // evening: 6 pm - 3:59 am
            ret = "Good evening!";
        }
        return ret;
    }

    public static String formatDateToString(Date date) {
        return new SimpleDateFormat(datePattern, Locale.CANADA).format(date);
    }

    public static String formatDateToJSONString(Date date) {
        return new SimpleDateFormat(JSONStringPattern).format(date);
    }

    public static Date JSONStringToDate(String js) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(JSONStringPattern);
        return sdf.parse(js);
    }

}
