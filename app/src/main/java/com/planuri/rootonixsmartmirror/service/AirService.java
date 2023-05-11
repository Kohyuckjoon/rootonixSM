package com.planuri.rootonixsmartmirror.service;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.model.air.AirData;
import com.planuri.rootonixsmartmirror.util.AppTrace;
import com.planuri.rootonixsmartmirror.util.log.LoggerInterface;
import com.planuri.rootonixsmartmirror.util.rest.AirRestUtil;
import com.planuri.rootonixsmartmirror.util.retrofit.NewsRetroService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirService implements LoggerInterface {

    private TextView tvAirPollution;
    private ImageView ivAirPollution;
    private AirData airData = new AirData();

    public AirService(Activity activity){
//        this.tvAirPollution = activity.findViewById(R.id.tv_air_pollution);
//        this.ivAirPollution = activity.findViewById(R.id.iv_air_pollution);
    }

    public void getAir(){
        NewsRetroService restService = AirRestUtil.getInstance().getDeviceService();
        restService = AirRestUtil.getInstance().getDeviceService();
        Call<JsonObject> call = restService.getAirPollution();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    int pollutionVal = 0;
                    String pollutionStr = "";
                    JSONObject airResponse = new JSONObject(response.body().toString());
                    Gson gson = new Gson();
                    airData = gson.fromJson(String.valueOf(airResponse), AirData.class);

                    for (int i=0; i<airData.response.body.items.size();i++){
                        if (airData.response.body.items.get(i).getStationName().equals("연산동")){
                            pollutionVal = Integer.parseInt(airData.response.body.items.get(i).getPm10Value());
                        }
                    }

                    if (pollutionVal>=0 && pollutionVal<=30){
                        pollutionStr = "매우좋음";
//                        ivAirPollution.setImageResource(R.drawable.icon_air_good);
                    } else if (pollutionVal>=31 && pollutionVal<=80){
                        pollutionStr = "보통";
//                        ivAirPollution.setImageResource(R.drawable.icon_air_normal);
                    } else if (pollutionVal>=81 && pollutionVal<=150){
                        pollutionStr = "나쁨";
//                        ivAirPollution.setImageResource(R.drawable.icon_air_bad);
                    } else if (pollutionVal>=151){
                        pollutionStr = "매우나쁨";
//                        ivAirPollution.setImageResource(R.drawable.icon_air_very);
                    }

                    Log.e(">>>>>>", pollutionStr + " : " + pollutionVal);
                    tvAirPollution.setText(pollutionStr);
                } catch (Exception e) {
                    e.printStackTrace();
                    AppTrace.e(e);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
