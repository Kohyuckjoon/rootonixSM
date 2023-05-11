package com.planuri.rootonixsmartmirror.util.gson_converter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * 가맹점 설정 / 영수증 속성값 / 최근 현금 반환 내역 / 기타 액션.
 * SharedPreferences로 기기 내부에 설정값 저장 및 불러오기.
 */
public class PreferenceUtils {
    static public final String TAG = PreferenceUtils.class.getSimpleName();

    private static final long TOKEN_DURATION = (1000 * 60 * 60) * 24 * 7;
    private static final long TOKEN_REFRESH_DURATION = (1000 * 60 * 60) * 24 * 21;
    public static final String TOKEN = "TOKEN";
    public static final String TOKEN_TIME = "TOKEN_TIME";

    static public SharedPreferences getPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /** 마운팅된 경로*/
    static public class  MountPath{
        public static final String m_key = "maount_path";
        static public String get(Context context){
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            return pref.getString(m_key, null);
        }

        static public void set(Context context, String value){
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putString(m_key, value);
            editor.commit();
        }

        static public boolean notNull(Context context){
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            //true : not null, false = null
            return pref.getString(m_key, null) != null;
        }
    }

    /** 가장 최근에 조회한 강수확률 */
    static public class RecentPrecipitation{
        public static final String m_key = "recent_precipitation";
        static public String get(Context context){
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            return pref.getString(m_key, null);
        }

        static public void set(Context context, String value){
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putString(m_key, value);
            editor.commit();
        }

        static public boolean notNull(Context context){
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            //true : not null, false = null
            return pref.getString(m_key, null) != null;
        }
    }
}
