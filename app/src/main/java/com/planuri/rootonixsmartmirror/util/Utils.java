package com.planuri.rootonixsmartmirror.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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


}
