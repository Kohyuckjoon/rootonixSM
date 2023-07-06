package com.planuri.rootonixsmartmirror.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    static public String getFormattedDate(@NonNull String format, Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(date);
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void threadSleep(int i) {
        try { Thread.sleep(i); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public static boolean isDeviceOnLine(Context cxt){
        boolean res = false;
        ConnectivityManager cm = (ConnectivityManager)cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni!= null && ni.isConnected();
    }

    public static String calculateAge(String birthdateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthdate = null;
        try {
            birthdate = sdf.parse(birthdateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (birthdate == null) {
            return "NULL";
        }

        Calendar birthdateCalendar = Calendar.getInstance();
        birthdateCalendar.setTime(birthdate);
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - birthdateCalendar.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < birthdateCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return String.valueOf(age);
    }


}
