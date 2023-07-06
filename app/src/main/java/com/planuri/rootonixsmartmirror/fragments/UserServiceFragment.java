package com.planuri.rootonixsmartmirror.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.activity.MainActivity;
import com.planuri.rootonixsmartmirror.base.BaseFragment;
import com.planuri.rootonixsmartmirror.dto.ResLoginDto;
import com.planuri.rootonixsmartmirror.dto.ResUsersDto;
import com.planuri.rootonixsmartmirror.fragments.adapter.MeasurementDetailsAdapter;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskUser;
import com.planuri.rootonixsmartmirror.model.kioskuser.LoginKioskUser;
import com.planuri.rootonixsmartmirror.model.kioskuser.TelSearchUserItem;
import com.planuri.rootonixsmartmirror.util.CustomRestClient;
import com.planuri.rootonixsmartmirror.util.Utils;
import com.planuri.rootonixsmartmirror.util.api.RestApiInterface;

import org.w3c.dom.Text;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserServiceFragment extends BaseFragment {
    //    MeasurementDetailsAdapter adapter = new MeasurementDetailsAdapter(getActivity().getApplicationContext());
    TextView kuserName;
    RecyclerView recyclerViewUserList; //RecyclerView를 사용
    MeasurementDetailsAdapter adapter; //Adapter 사용

    private final RestApiInterface CallApi = CustomRestClient.getInstance().getApiInterface();  // Rest Api Interface

    String kuser_info;
    public KioskUser kioskUser;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public UserServiceFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    public void userInfo(KioskUser items){
        TelSearchUserItem telSearchUserItem = new TelSearchUserItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_service, container, false);

        TextView kuserName = view.findViewById(R.id.kuser_name);
        TextView kuserTel = view.findViewById(R.id.kuser_tel);
        TextView kuserBirthDate = view.findViewById(R.id.kuser_birth_date);
        ImageView measure = view.findViewById(R.id.measure);
        ImageView measurement_details = view.findViewById(R.id.measurement_details);
        ImageView changing_information_icon = view.findViewById(R.id.changing_information_icon); //사용자 정보 수정

        TextView kuserSex = view.findViewById(R.id.kuser_sex);
//        String s = String.valueOf(kuserSex);

        /**
         * 사용자 정보 수정
         */
        changing_information_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(16);
            }
        });

        /**
         * userService logout Btn
         */
        ImageView service_logout = view.findViewById(R.id.service_logout);
        service_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(3);
                Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 왼쪽 상단 유저 기본 정보 표시해주기
         * */
        if (LoginKioskUser.getInstance().getKioskUser().kuserName.length() >= 3) {
            kuserName.setText(LoginKioskUser.getInstance().getKioskUser().kuserName.substring(0, 1)
                    + "*" + LoginKioskUser.getInstance().getKioskUser().kuserName.substring(2, 3) + " 님");
        } else if (LoginKioskUser.getInstance().getKioskUser().kuserName.length() < 3) {
            kuserName.setText(LoginKioskUser.getInstance().getKioskUser().kuserName.substring(0, 1) + "*" + " 님");
        }

        /** 전화번호 및 나이 */
        kuserTel.setText(LoginKioskUser.getInstance().getKioskUser().kuserTel.substring(0,3) + "-" + "****" + "-" +
                LoginKioskUser.getInstance().getKioskUser().kuserTel.substring(7,11));
        kuserBirthDate.setText(Utils.calculateAge(LoginKioskUser.getInstance().getKioskUser().kuserBirthDate));

        /**
         * 서비스 선택 화면
         * 왼쪽 상단 로그인한 사용자 정보 뿌려주기(성별)
         * */
        char kuserSexStatus = LoginKioskUser.getInstance().getKioskUser().kuserSex;
        if (kuserSexStatus=='M'){
            kuserSex.setText("남성");
        } else if (kuserSexStatus=='F'){
            kuserSex.setText("여성");
        }

        /**서비스 선택 > 측정하기(촬영 화면) 이동*/
        measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(12);
            }
        });


//        recyclerViewUserList = (RecyclerView) view.findViewById(R.id.user_details_list);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
//        recyclerViewUserList.setLayoutManager(layoutManager);

        /**서비스 선택 > 측정 내역 보기 클릭 시*/
        measurement_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fragmentView(13);

                adapter = new MeasurementDetailsAdapter(getActivity().getApplicationContext());

                CallApi.getUsersByTelNum(LoginKioskUser.getInstance().getKioskUser().kuserTel.substring(7,11)).enqueue(new Callback<ResUsersDto>() {
                    @Override
                    public void onResponse(Call<ResUsersDto> call, Response<ResUsersDto> response) {
                        if (response.isSuccessful() && response.body() != null){
                            ResUsersDto usersDto = response.body();

//                            recyclerViewUserList.setAdapter(adapter);

                            adapter.clearItem();

                            for (int i = 0; i<usersDto.list.size(); i++){
                                TelSearchUserItem searchItem = new TelSearchUserItem();
                                searchItem.kuserBirthDate = usersDto.list.get(i).kuserBirthDate;

                                adapter.addItem(searchItem);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResUsersDto> call, Throwable t) {

                    }
                });
            }
        });




        return view;
    }
}