package com.planuri.rootonixsmartmirror.util.api;

import com.planuri.rootonixsmartmirror.dto.ResScalpAnalysisDto;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestAIServer {

    @Multipart
    @POST("/analyze")
    Call<ResScalpAnalysisDto> reqScalpAnalysis(@Part List<MultipartBody.Part> uploadFiles);

}
