package com.planuri.rootonixsmartmirror.fragments.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskAnalysisHistory;
import com.planuri.rootonixsmartmirror.util.graphic.RadarChartDrawer;
import com.planuri.rootonixsmartmirror.util.graphic.RadarChartImageView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MeasurementUserAdapter extends RecyclerView.Adapter<MeasurementUserAdapter.ViewHolder> {
    Context mContext;

    ArrayList<KioskAnalysisHistory> items = new ArrayList<>();

    /** ViewHolder Click 이벤트를 위한 인터페이스 및 리스너 등록 */

    // 액티비티나 프래그먼트에서 이 인터페이스를 구현. ViewHolder 생성자에서 클릭이벤트 처리하면서 액티비티 및 프래그먼트 에등록된 onGridItemClicked 로 보낸다.
    public interface OnGridItemClickListener {
        void onGridItemClicked(View v, int position, KioskAnalysisHistory items);
    }

    private OnGridItemClickListener mListener = null;

    // 리스너 연결. 액티비티 혹은 프래그먼트에서 이 adapter 객체를 생성한 후 이 매서드로 리스너 등록
    public void setOnGridItemClickListener(OnGridItemClickListener listener) {
        this.mListener = listener;
    }

    /** ViewHolder Click 이벤트를 위한 인터페이스 및 리스너 등록  끝 */
    /** 측정내역보기 리스트 결과 보여주기*/
    public MeasurementUserAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.fragment_measurement_user_details_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        KioskAnalysisHistory item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

//    public void addItem(KioskUser item){
//        items.add(item);
//    }

//    public void addItem(TelSearchUserItem item) {
//        items.add(item);
//    }

    public void addItem(KioskAnalysisHistory item) {
        items.add(item);
    }

    public void clearItem(){ items.clear();     }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RadarChartImageView ivRadarChart;

        /** 지난 날짜 데이터들 뿌려줘야함
         * 요일, 시간 별도로 작업해줘야함
         * analyzedDayWeek
         * analyzedTime
         * */
        private TextView analyzedDate;      //일시
        private TextView analyzedDayWeek;   // 요일
        private TextView analyzedTime;      //시간
        private TextView kanalyzedIdx; //리스트 인덱스 번호
        private TextView tv_diagnosis_name;     //진단명

        KioskAnalysisHistory currentItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            analyzedDate = itemView.findViewById(R.id.analyzed_date);           //일시
            analyzedDayWeek = itemView.findViewById(R.id.analyzed_day_week);    //요일
            analyzedTime = itemView.findViewById(R.id.analyzed_time);           //시간
            kanalyzedIdx = itemView.findViewById(R.id.kanalyzed_idx);           //리스트 인덱스 번호
            tv_diagnosis_name = itemView.findViewById(R.id.tv_diagnosis_name);  //진단명
            ivRadarChart = (RadarChartImageView)itemView.findViewById(R.id.iv_radar_chart);          //레이더 차트

            ivRadarChart = itemView.findViewById(R.id.iv_radar_chart);
            float diameter = 113.0f;
            float offsetX = 2f;
            float offsetY = 5.0f;
            ivRadarChart.setRadarAttribute(new RadarChartDrawer.RadarAttribute(diameter, 0f, Color.parseColor("#5E72E9"))); // 한 번만 설정
            ivRadarChart.setOffsetPoint(new RadarChartDrawer.OffsetPoint(offsetX, offsetY)); // 한 번만 설정

            /** ViewHolder item 클릭 이벤트 */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if(mListener != null) {
                            KioskAnalysisHistory clickedItem = new KioskAnalysisHistory();

                            clickedItem.kanalyzedIdx = Integer.parseInt(((TextView)v.findViewById(R.id.kanalyzed_idx)).getText().toString()); //분석 인덱스 번호
                            mListener.onGridItemClicked(v, pos, clickedItem);   //(액티비티나 프래그먼트)등록된 리스너 호출
                        }
                    }
                }
            });
        }

        public void setItem(KioskAnalysisHistory item) {
            this.currentItem = item;

            analyzedDate.setText(item.analyzedDate.substring(0,10));        //일시

            String inputDate = item.analyzedDate; //
            System.out.println("item.analyzedDate >>> " + item.analyzedDate);
            System.out.println("Calendar.DAY_OF_WEEK >>> " + Calendar.DAY_OF_WEEK);
            System.out.println();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);

            Date date = null;
            try {
                date = sdf.parse(inputDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat dayOfWeek = new SimpleDateFormat("E요일", Locale.KOREA);

            analyzedDayWeek.setText(dayOfWeek.format(date).toString());     //측정요일
            System.out.println("dayOfWeek.format(date).toString() >>> "+ dayOfWeek.format(date).toString());
            analyzedTime.setText(item.analyzedDate.substring(11,16));       //시간
            kanalyzedIdx.setText("" + item.kanalyzedIdx);
            tv_diagnosis_name.setText(item.diagnosisName);

            ArrayList<Integer> scores = new ArrayList<>();
            scores.add(currentItem.deadSkinPt);    //각질
            scores.add(currentItem.sebumPt);       //유분
            scores.add(currentItem.hairThickPt);   //모발두께
            scores.add(currentItem.hairNumPt);     //모낭당 모발 수
            scores.add(currentItem.sensitivityPt); //민감도
            /*int s1=20, s2=12, s3=20, s4=20, s5=20;
            scores.add(s1);
            scores.add(s2);
            scores.add(s3);
            scores.add(s4);
            scores.add(s5);*/

            ivRadarChart.setScores(scores); // 각 아이템이 뷰홀더에 바인딩될 때마다 변경
        }
    }
}