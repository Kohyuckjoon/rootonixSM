package com.planuri.rootonixsmartmirror.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.base.BaseFragment;
import com.planuri.rootonixsmartmirror.fragments.adapter.MeasurementDetailsAdapter;
import com.planuri.rootonixsmartmirror.model.HairSkinAnalyze;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeasurementDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MeasurementDetailsFragment extends BaseFragment {
    RecyclerView recyclerView;
//    MeasurementDetailsAdapter adapter = new MeasurementDetailsAdapter();

    public MeasurementDetailsFragment() {

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_measurement_details, container, false);
//
////        recyclerView = view.findViewById(R.id.measurement_list);
//        recyclerView = view.findViewById(R.id.search_name);
//        recyclerView.setAdapter(adapter);   // RecyclerView와 Adapter 연결
//
//        // 아래 블럭은 테스트 데이터임 추후 필히 삭제할 것
//        {
//            List<HairSkinAnalyze> testData = new ArrayList<>();
//            HairSkinAnalyze temp;
//            temp = new HairSkinAnalyze();
//            temp.이미지 = new ArrayList<>();
//            temp.이미지.add("테스트다 임마");
//            testData.add(temp);
//
//            temp = new HairSkinAnalyze();
//            temp.이미지 = new ArrayList<>();
//            temp.이미지.add("ㅋㅋㄹㅃㅃ");
//            testData.add(temp);
//
//            adapter.setData(testData);
//        }
//
//        return view;
//    }
}