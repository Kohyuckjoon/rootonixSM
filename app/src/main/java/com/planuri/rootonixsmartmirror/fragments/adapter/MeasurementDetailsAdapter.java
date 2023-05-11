package com.planuri.rootonixsmartmirror.fragments.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.dto.RegKioskUserDto;
import com.planuri.rootonixsmartmirror.model.HairSkinAnalyze;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskUser;
import com.planuri.rootonixsmartmirror.model.kioskuser.TelSearchUserItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MeasurementDetailsAdapter extends RecyclerView.Adapter<MeasurementDetailsAdapter.ViewHolder> {
    Context mContext;
//    private List<HairSkinAnalyze> list = new ArrayList<>();
//    private List<KioskUser> items = new ArrayList<>();

    ArrayList<TelSearchUserItem> items = new ArrayList<>();

    /** ViewHolder Click 이벤트를 위한 인터페이스 및 리스너 등록 */

    // 액티비티나 프래그먼트에서 이 인터페이스를 구현. ViewHolder 생성자에서 클릭이벤트 처리하면서 액티비티 및 프래그먼트 에등록된 onGridItemClicked 로 보낸다.
    public interface OnGridItemClickListener {
        void onGridItemClicked(View v, int position, TelSearchUserItem items);
    }

    private OnGridItemClickListener mListener = null;

    // 리스너 연결. 액티비티 혹은 프래그먼트에서 이 adapter 객체를 생성한 후 이 매서드로 리스너 등록
    public void setOnGridItemClickListener(OnGridItemClickListener listener) {
        this.mListener = listener;
    }

    /** ViewHolder Click 이벤트를 위한 인터페이스 및 리스너 등록  끝 */

    public MeasurementDetailsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /** CareMode >> 검색할때 리스트 보여주기*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.fragment_measurement_details, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementDetailsAdapter.ViewHolder holder, int position) {
        TelSearchUserItem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

//    public void addItem(KioskUser item){
//        items.add(item);
//    }

    public void addItem(TelSearchUserItem item) {
        items.add(item);
    }

    public void clearItem(){ items.clear();     }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView kuser_name;     // 이름
        TextView kuser_tel;      // 전화번호
        TextView kuser_birth_date; // 생년월일
        TextView kuser_sex;      // 성별

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kuser_name = itemView.findViewById(R.id.kuser_name);
            kuser_tel = itemView.findViewById(R.id.kuser_tel);
            kuser_birth_date = itemView.findViewById(R.id.kuser_birth_date);
            kuser_sex = itemView.findViewById(R.id.kuser_sex);

            /** ViewHolder item 클릭 이벤트 */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if(mListener != null) {

                            TelSearchUserItem clickedItem = new TelSearchUserItem();

                            clickedItem.kuserName = ((TextView)v.findViewById(R.id.kuser_name)).getText().toString();
                            clickedItem.kuserTel = ((TextView)v.findViewById(R.id.kuser_tel)).getText().toString();
                            clickedItem.kuserSex = ((TextView)v.findViewById(R.id.kuser_sex)).getText().charAt(0);
                            clickedItem.kuserBirthDate = ((TextView)v.findViewById(R.id.kuser_birth_date)).getText().toString();

                            mListener.onGridItemClicked(v, pos, clickedItem);   //(액티비티나 프래그먼트)등록된 리스너 호출 
                        }
                    }
                }
            });

        }

        public void setItem(TelSearchUserItem item) {
            kuser_name.setText(item.kuserName + "");
            kuser_tel.setText(item.kuserTel + "");
            kuser_birth_date.setText(item.kuserBirthDate + "");
            kuser_sex.setText(item.kuserSex == 'M' ? "남성" : "여성");
        }
    }
}