package com.planuri.rootonixsmartmirror.service;

import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.planuri.rootonixsmartmirror.R;
import com.planuri.rootonixsmartmirror.model.water.AuthRequire;
import com.planuri.rootonixsmartmirror.model.water.WaterData;
import com.planuri.rootonixsmartmirror.util.AppTrace;
import com.planuri.rootonixsmartmirror.util.Const;
import com.planuri.rootonixsmartmirror.util.log.LoggerInterface;
import com.planuri.rootonixsmartmirror.util.rest.WaterRestUtil;
import com.planuri.rootonixsmartmirror.util.retrofit.NewsRetroService;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaterService implements LoggerInterface {

    private TextView tvPH, tvRC, tvNTU;
    private TextView tvPHStat, tvRCStat, tvNTUStat;
    private TextView tvMascot;
    private ImageView ivMascot;
    private ProgressBar pbNTU, pbRC, pbPH;

    private WaterData waterData = new WaterData();
    private AuthRequire authRequire = new AuthRequire();

    String mentArr[] = {"스마트정수장에서 갓 생산한\n고품질의 수돗물입니다", "깨끗하고 건강한 물,\n안심하고 이용하세요"};
    int currentMentArr = 0;
    CountDownTimer changeTxt = null;
    boolean ntuAdequate = true, rcAdequate = true, phAdequate = true;

    public WaterService(Activity activity){
//        this.tvPH = activity.findViewById(R.id.tv_ph);
//        this.tvRC = activity.findViewById(R.id.tv_rc);
//        this.tvNTU = activity.findViewById(R.id.tv_ntu);
//
//        this.pbPH = activity.findViewById(R.id.pb_ph);
//        this.pbRC = activity.findViewById(R.id.pb_rc);
//        this.pbNTU = activity.findViewById(R.id.pb_ntu);
//
//        this.tvPHStat = activity.findViewById(R.id.tv_ph_stat);
//        this.tvRCStat = activity.findViewById(R.id.tv_rc_stat);
//        this.tvNTUStat = activity.findViewById(R.id.tv_ntu_stat);
//
//        this.tvMascot = activity.findViewById(R.id.tv_mascot);
//        this.ivMascot = activity.findViewById(R.id.iv_mascot);
    }

    public void getWaterSensor(){
        authRequire.setUserId(Const.ID);
        authRequire.setUserPassword(encodePw(Const.PW));
        Log.e(">>>>sendData", authRequire.toString());

        NewsRetroService restService = WaterRestUtil.getInstance().getDeviceService();
        Call<JsonObject> call = restService.auth(authRequire);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    Log.e(">>>>>JSONData", response.body().toString());
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    String siteId = jsonObject.getString("customerId");
                    String authToken = jsonObject.getString("authToken");
                    String timeForm = "";

                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    timeForm = sdf.format(date);

                    NewsRetroService water = WaterRestUtil.getInstance().getDeviceService();
                    Call<JsonObject> getWater = water.getWaterStatus("UL "+authToken, siteId, timeForm);
                    getWater.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            try {
                                float ph = 0, ntu = 0, rc = 0;
                                JSONObject waterResponse = new JSONObject(response.body().toString());
                                Gson gson = new Gson();
                                waterData = gson.fromJson(String.valueOf(waterResponse), WaterData.class);
                                Log.e(">>>>>", waterData.toString());

                                for (int i=0; i<waterData.attrs.size();i++){
                                    switch (waterData.attrs.get(i).attrKey){
                                        case "MEAS_PH":
                                            tvPH.setText(waterData.attrs.get(i).attrValues.get(0).attrValue);
                                            ph = Float.parseFloat(waterData.attrs.get(i).attrValues.get(0).attrValue) * 10;
                                            break;
                                        case "MEAS_TUR":
                                            tvNTU.setText(waterData.attrs.get(i).attrValues.get(0).attrValue);
                                            ntu = Float.parseFloat(waterData.attrs.get(i).attrValues.get(0).attrValue) * 10;
                                            break;
                                        case "MEAS_RC":
                                            tvRC.setText(waterData.attrs.get(i).attrValues.get(0).attrValue);
                                            rc = Float.parseFloat(waterData.attrs.get(i).attrValues.get(0).attrValue) * 10;
                                            break;
                                    }
                                }

//                                ntu = 2;
//                                rc = 10;
//                                ph = 60;

                                pbNTU.setProgress((int)ntu);
                                tvNTU.setText(String.valueOf(ntu/10));

                                pbRC.setProgress((int)rc);
                                tvRC.setText(String.valueOf(rc/10));

                                pbPH.setProgress((int)ph);
                                tvPH.setText(String.valueOf(ph/10));

                                //탁도
                                if (ntu>=0 && ntu<=5){
                                    tvNTUStat.setText("적정");
                                    ntuAdequate = true;
                                } else if (ntu>=6){
                                    tvNTUStat.setText("점검중");
                                    ntuAdequate = false;
                                }

                                //잔류염소
                                if (rc>=0 && rc<1){
                                    tvRCStat.setText("점검중");
                                    rcAdequate = false;
                                } else if (rc>=1 && rc<=40){
                                    tvRCStat.setText("적정");
                                    rcAdequate = true;
                                } else if (rc>=41){
                                    tvRCStat.setText("점검중");
                                    rcAdequate = false;
                                }

                                //PH 수소이온
                                if (ph>=0 && ph<=57){
                                    tvPHStat.setText("점검중");
                                    phAdequate = false;
                                } else if (ph>=58 && ph<=85){
                                    tvPHStat.setText("적정");
                                    phAdequate = true;
                                } else if (ph>=86){
                                    tvPHStat.setText("점검중");
                                    phAdequate = false;
                                }

                                setMascot(ntuAdequate, rcAdequate, phAdequate);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            AppTrace.e(t);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    AppTrace.e(e);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(">>>>>>>>", "Connection Fail");
                AppTrace.e(t);
            }
        });
    }

    public String encodePw(String pw){
        String encoded = "";
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(pw.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for (int i=0; i<byteData.length; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            encoded = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encoded;
    }

    public void setMascot(boolean ntu, boolean rc, boolean ph){
//        ivMascot.setImageResource(R.drawable.bangul_good);
        tvMascot.setText(mentArr[0]);

        if (ntu && rc && ph) {
//            ivMascot.setImageResource(R.drawable.bangul_good);
            changeTxt = new CountDownTimer(5000, 5000) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    if (currentMentArr == 0) {
                        currentMentArr = 1;
                        tvMascot.setText(mentArr[1]);
                    } else {
                        currentMentArr = 0;
                        tvMascot.setText(mentArr[0]);
                    }
                    changeTxt.start();
                }
            };
            changeTxt.start();
        } else {
//            ivMascot.setImageResource(R.drawable.bangul_inspection);
            tvMascot.setText("더 나은 서비스를 위해\n설비를 점검중에 있어요.");
        }
    }
}
