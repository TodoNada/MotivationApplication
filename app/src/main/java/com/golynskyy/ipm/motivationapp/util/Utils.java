package com.golynskyy.ipm.motivationapp.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

/**
 * Created by Dep5 on 07.08.2017.
 */

public class Utils {

    public static String toMonthString(int i) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};

        return " " +months[i];
    }

    public static String formatNowDateTimeToString() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMMM dd HH:mm:ss");
        return sdf.format(calendar.getTimeInMillis());
    }


    public static String formatDateTimeToString(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMMM dd HH:mm:ss");
        return sdf.format(timeStamp);
    }

    public static long formatStringToDateTime(long timeString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMMM dd HH:mm:ss");
        Calendar calendar = sdf.getCalendar();
        return calendar.getTimeInMillis();
    }

    public static boolean isDateToday(int timestamp)
    {
        Calendar todayBeginCalendar = Calendar.getInstance();
        todayBeginCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayBeginCalendar.set(Calendar.MINUTE, 0);
        todayBeginCalendar.set(Calendar.SECOND, 0);

        Calendar tomorrowBeginCalendar = (Calendar) todayBeginCalendar.clone();
        tomorrowBeginCalendar.add(Calendar.DATE, 1);

        int todayBeginTimestamp = (int)(todayBeginCalendar.getTimeInMillis()/1000);
        int tomorrowBeginTimestamp = (int)(tomorrowBeginCalendar.getTimeInMillis()/1000);

        if(timestamp >= todayBeginTimestamp && timestamp < tomorrowBeginTimestamp)
        {
            return true;
        }
        return false;
    }

    public static boolean isDateYesterday(int timestamp)
    {
        Calendar todayBeginCalendar = Calendar.getInstance();
        todayBeginCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayBeginCalendar.set(Calendar.MINUTE, 0);
        todayBeginCalendar.set(Calendar.SECOND, 0);

        Calendar yesterdayBeginCalendar = (Calendar) todayBeginCalendar.clone();
        yesterdayBeginCalendar. add(Calendar.DATE, -1);

        int todayBeginTimestamp = (int)(todayBeginCalendar.getTimeInMillis()/1000);
        int yesterdayBeginTimestamp = (int)(yesterdayBeginCalendar.getTimeInMillis()/1000);

        if(timestamp >= yesterdayBeginTimestamp && timestamp < todayBeginTimestamp)
        {
            return true;
        }
        return false;
    }

    public static boolean isDateTomorrow(int timestamp)
    {
        Calendar tomorrowBeginCalendar = Calendar.getInstance();
        tomorrowBeginCalendar.set(Calendar.HOUR_OF_DAY, 0);
        tomorrowBeginCalendar.set(Calendar.MINUTE, 0);
        tomorrowBeginCalendar.set(Calendar.SECOND, 0);
        tomorrowBeginCalendar.add(Calendar.DATE, 1);

        Calendar otherdayBeginCalendar = (Calendar) tomorrowBeginCalendar.clone();
        otherdayBeginCalendar.add(Calendar.DATE, 1);

        int tomorrowBeginTimestamp = (int)(tomorrowBeginCalendar.getTimeInMillis()/1000);
        int otherdayBeginTimestamp = (int)(otherdayBeginCalendar.getTimeInMillis()/1000);

        if(timestamp >= tomorrowBeginTimestamp && timestamp < otherdayBeginTimestamp)
        {
            return true;
        }
        return false;
    }
    public static boolean isNetworkAvailable(Context context)
    {
        try
        {
            ConnectivityManager con=(ConnectivityManager)context.getSystemService(Activity.CONNECTIVITY_SERVICE);
            try
            {
                return con.getActiveNetworkInfo().isConnectedOrConnecting();
            }
            catch(Exception e) {
                boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
                boolean mobile=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
                if(wifi || mobile) {
                    return true;
                }
                return false;
            }
        }
        catch(Exception e)
        {
            return false;
        }
    }


}
