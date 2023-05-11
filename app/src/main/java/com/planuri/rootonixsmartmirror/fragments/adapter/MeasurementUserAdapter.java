package com.planuri.rootonixsmartmirror.fragments.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskAnalysisHistory;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MeasurementUserAdapter extends RecyclerView.Adapter<MeasurementUserAdapter.ViewHolder> {
    Context mContext;

    //    ArrayList<TelSearchUserItem> items = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        TelSearchUserItem item = items.get(position);
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

        /** 지난 날짜 데이터들 뿌려줘야함
         * 요일, 시간 별도로 작업해줘야함
         * analyzedDayWeek
         * analyzedTime
         * */
        TextView analyzedDate;      //일시
        TextView analyzedDayWeek;   // 요일
        TextView analyzedTime;      //시간

        /** 데이터 수치로 뿌려줘야할 목록들*/
        ProgressBar deadSkinPt; //각질
        ProgressBar sensitivityPt; //민감도
        ProgressBar hairThickPt; //모발 두께
        ProgressBar sebumPt; //유분
        ProgressBar hairNumPt; //모낭당 모발수
        TextView deadSkinPtP; //각질 점수
        TextView sensitivityPtP; //민감도 점수
        TextView hairThickPtP;  //모발 두께 점수
        TextView sebumPtP;  //유분 점수
        TextView hairNumPtP; //모낭당 모발수
        TextView totalPt;   //전체 총합
        TextView kanalyzedIdx; //리스트 인덱스 번호

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            analyzedDate = itemView.findViewById(R.id.analyzed_date);       //일시
            analyzedDayWeek = itemView.findViewById(R.id.analyzed_day_week);//요일
            analyzedTime = itemView.findViewById(R.id.analyzed_time);       //시간
            deadSkinPt = itemView.findViewById(R.id.dead_skin_pt);          //각질 점수
            deadSkinPtP = itemView.findViewById(R.id.dead_skin_pt_p);       //각질 점수 표기
            sensitivityPt = itemView.findViewById(R.id.sensitivity_pt);     //민감도 점수
            sensitivityPtP = itemView.findViewById(R.id.sensitivity_pt_p);  //민감도 점수 표기
            hairThickPt = itemView.findViewById(R.id.hair_thick_pt);        //모발 두께 점수
            hairThickPtP = itemView.findViewById(R.id.hair_thick_pt_p);     //모발 두께 점수 표기
            sebumPt = itemView.findViewById(R.id.sebum_pt);                 //유분 점수
            sebumPtP = itemView.findViewById(R.id.sebum_pt_p);              //유분 점수 표기
            hairNumPt = itemView.findViewById(R.id.hair_num_pt);            //모낭당 모발수
            hairNumPtP = itemView.findViewById(R.id.hair_num_pt_p);         //모낭당 모발수 점수 표기
            totalPt = itemView.findViewById(R.id.total_pt);                 //전체 총합
            kanalyzedIdx = itemView.findViewById(R.id.kanalyzed_idx);        //리스트 인덱스 번호

            /** ViewHolder item 클릭 이벤트 */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if(mListener != null) {

//                            TelSearchUserItem clickedItem = new TelSearchUserItem();
                            KioskAnalysisHistory clickedItem = new KioskAnalysisHistory();

                            clickedItem.kanalyzedIdx = Integer.parseInt(((TextView)v.findViewById(R.id.kanalyzed_idx)).getText().toString()); //kanalyzed_idx
                            clickedItem.analyzedDate = ((TextView)v.findViewById(R.id.analyzed_date)).getText().toString(); //일시
//                            clickedItem.analyzedDayWeek = ((TextView)v.findViewById(R.id.analyzed_day_week)).getText().toString(); //요일
//                            clickedItem.analyzedTime = ((TextView)v.findViewById(R.id.analyzed_time)).getText().toString(); //시간
                            clickedItem.deadSkinPt = Integer.parseInt(((TextView)v.findViewById(R.id.dead_skin_pt_p)).getText().toString()); //각질 점수 표기
                            clickedItem.sensitivityPt = Integer.parseInt(((TextView)v.findViewById(R.id.sensitivity_pt_p)).getText().toString()); //민감도 점수 표기
                            clickedItem.hairThickPt = Integer.parseInt(((TextView)v.findViewById(R.id.hair_thick_pt_p)).getText().toString()); //모발두께 점수 표기
                            clickedItem.sebumPt = Integer.parseInt(((TextView)v.findViewById(R.id.sebum_pt_p)).getText().toString()); //유분 점수 표기
                            clickedItem.hairNumPt = Integer.parseInt(((TextView)v.findViewById(R.id.hair_num_pt_p)).getText().toString()); //유분 점수 표기

//                            clickedItem.deadSkinPt = Integer.parseInt(((TextView)v.findViewById(R.id.dead_skin_sc_pt)).getText().toString());
//                            clickedItem.hairNumPt = Integer.parseInt(((TextView)v.findViewById(R.id.hair_thick_sc_pt)).getText().toString());
//                            clickedItem.hairThickPt = Integer.parseInt(((TextView)v.findViewById(R.id.kanalyzed_idx)).getText().toString());
//                            clickedItem.sebumPt = Integer.parseInt(((TextView)v.findViewById(R.id.kanalyzed_idx)).getText().toString());
//                            clickedItem.sensitivityPt = Integer.parseInt(((TextView)v.findViewById(R.id.kanalyzed_idx)).getText().toString());
//                            clickedItem.totalPt = Integer.parseInt(((TextView)v.findViewById(R.id.kanalyzed_idx)).getText().toString());
//                            clickedItem.analyzedDate = ((TextView)v.findViewById(R.id.kanalyzed_idx)).getText().toString();


//                            clickedItem.deadSkinPt = ((TextView)v.findViewById(R.id.dead_skin_sc_pt)).getText().toString();
//                            clickedItem.deadSkinPt = ((TextView)v.findViewById(R.id.dead_skin_pt_p)).getText().toString();
//                            Log.e("kanalyzedIdx Test","kanalyzedIdx >>>" + kanalyzedIdx);

                            mListener.onGridItemClicked(v, pos, clickedItem);   //(액티비티나 프래그먼트)등록된 리스너 호출
                        }
                    }
                }
            });

        }

        public void setItem(KioskAnalysisHistory item) {

            analyzedDate.setText(item.analyzedDate.substring(0,10));        //일시
//            analyzedDate.setText(item.analyzedDate);        //일시
//            System.out.println("item.analyzedDate >>> "+ item.analyzedDate);
//            analyzedDayWeek.setText(item.analyzedDate);                   //요일
//            analyzedDayWeek.setText("" + inputDate);

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
            deadSkinPt.setProgress(item.deadSkinPt);                        //각질 점수
            deadSkinPtP.setText("" + item.deadSkinPt);                      //각질 점수 표기
            sensitivityPt.setProgress(item.sensitivityPt);                  //민감도 점수
            sensitivityPtP.setText("" + item.sensitivityPt);                //민감도 점수 표기
            hairThickPt.setProgress(item.hairThickPt);                      //모발 두께 점수
            hairThickPtP.setText("" + item.hairThickPt);                    //모발 두께 점수 표기
            sebumPt.setProgress(item.sebumPt);                              //유분 점수
            sebumPtP.setText("" + item.sebumPt);                            //유분 점수 표기
            hairNumPt.setProgress(item.hairNumPt);                          //모낭당 모발수
            hairNumPtP.setText("" + item.hairNumPt);                        //모낭당 모발수 점수 표기
            kanalyzedIdx.setText("" + item.kanalyzedIdx);

//            String total = item.deadSkinPt

            totalPt.setText("" + item.totalPt); // 전체 총합
        }
    }
}