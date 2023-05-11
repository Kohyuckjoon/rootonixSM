package com.planuri.rootonixsmartmirror.service;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.util.log.LoggerInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateService implements LoggerInterface {

    private TextView tvDate, tvDayOfWeek;
    private ImageView ivAM, ivPM, ivAMPM;

    public DateService(Activity activity){
        this.tvDate = activity.findViewById(R.id.tv_date);
        this.tvDayOfWeek = activity.findViewById(R.id.tv_day_of_week);
        this.ivAMPM = activity.findViewById(R.id.iv_ampm);
    }

    public void initializeDate(){
        //날짜
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        SimpleDateFormat dayOfWeek = new SimpleDateFormat("E요일", Locale.KOREA);
        tvDate.setText(sdf.format(date));

        /**
         * 요일 영어 약자로 변환
         */
        if(dayOfWeek.format(date).toString().equals("월요일")){
            this.tvDayOfWeek.setText("MON");
            System.out.println("dayOfWeek >>> " + dayOfWeek.format(date).toString().equals("월요일"));
        }else if (dayOfWeek.format(date).toString().equals("화요일")) {
            this.tvDayOfWeek.setText("TUE");
        }else if (dayOfWeek.format(date).toString().equals("수요일")){
            this.tvDayOfWeek.setText("WED");
        }else if (dayOfWeek.format(date).toString().equals("목요일")){
            this.tvDayOfWeek.setText("THU");
        }else if (dayOfWeek.format(date).toString().equals("금요일")){
            this.tvDayOfWeek.setText("FRI");
        }else if (dayOfWeek.format(date).toString().equals("토요일")){
            this.tvDayOfWeek.setText("SAT");
        }else if (dayOfWeek.format(date).toString().equals("일요일")){
            this.tvDayOfWeek.setText("SUN");
        }

        /**
         * 오전, 오후 출력
         */
        SimpleDateFormat ampm = new SimpleDateFormat("a");
        String ampmStr = ampm.format(date);
        if (ampmStr.equals("오전")){
            ivAMPM.setImageResource(R.drawable.am);
        } else {
            ivAMPM.setImageResource(R.drawable.icon_pm);
        }
    }
}
