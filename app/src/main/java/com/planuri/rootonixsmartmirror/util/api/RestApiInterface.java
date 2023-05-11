package com.planuri.rootonixsmartmirror.util.api;

import com.planuri.rootonixsmartmirror.dto.CommonResponse;
import com.planuri.rootonixsmartmirror.dto.CorrectionDto;
import com.planuri.rootonixsmartmirror.dto.RegKioskUserDto;
import com.planuri.rootonixsmartmirror.dto.ResAnalysisHistoryDto;
import com.planuri.rootonixsmartmirror.dto.ResLoginDto;
import com.planuri.rootonixsmartmirror.dto.ReqSaveAnalysisDto;
import com.planuri.rootonixsmartmirror.dto.ResUsersDto;
import com.planuri.rootonixsmartmirror.dto.TestDto;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskAnalysisHistory;
import com.planuri.rootonixsmartmirror.model.kioskuser.KioskAnalysisHistoryDetail;
import com.planuri.rootonixsmartmirror.model.kioskuser.RecentAnalysis;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/** HTTP Method ( POST / GET / PUT / DELETE ) 들을 정의한 인터페이스
 *  Retrofit 에서 이 인터페이스를 사용하여 REST 요청을 함
 *  Request 파라미터 와 Response 는 DTO 객체를 이용하면 JSON 으로 Converting 됨 */
public interface RestApiInterface {

    /** 에코 테스트 */
    @GET("test/echo")
    Call<TestDto> getTest(@Query("echoStr") String echoStr);

    /** 전화번호로 검색
     * @param telNum*/
    @GET("kiosk/search/{telNum}")
    Call<ResUsersDto> getUsersByTelNum(@Path("telNum") String telNum);

    /** 성명, 전화번호로 로그인 */
    @GET("kiosk/login")
    Call<ResLoginDto> RequestLogin(@Query("kuserName") String kuserName, @Query("kuserTel") String kuserTel);

    /** 사용자 등록 //*/
    @POST("kiosk/register")
    Call<CommonResponse> RegisterUser(@Body RegKioskUserDto regKioskUserDto);

    /** 스마트미러 측정 내역 yearMonth = 2022-11 형식 */
    @GET("kiosk/kuser/{kuserIdx}/analysis-history")
    Call<ResAnalysisHistoryDto> getAnalysisHistory(@Path("kuserIdx") String kuserIdx, @Query("yearMonth") String yearMonth);

    /** 분석결과 저장 요청 */
    @POST("kiosk/kuser/{kuserIdx}/analysis")
    Call<CommonResponse> saveAnalysisData(@Path("kuserIdx") long kuserIdx, @Body ReqSaveAnalysisDto reqSaveAnalysisDto);

    /** 이전 평가 요청 */
    @GET("kiosk/kuser/{kuserIdx}/recent-analysis")
    Call<RecentAnalysis> getRecentAnalysis(@Path("kuserIdx") long kuserIdx);

    /** 회원정보 수정*/
    @PUT("kiosk/user/{kuserIdx}")
    Call<CommonResponse> getCorrection(@Path("kuserIdx") long kuserIdx, @Body CorrectionDto correctionDto);
    //Call<CommonResponse> getCorrection(@Path("kuserIdx") long kuserIdx, @Body {내가 만든 DTO} {regKioskUserDto});

    /** 회원 탈퇴*/
    @DELETE("kiosk/user/{kuserIdx}")
    Call<CommonResponse> getWithdrawal(@Path("kuserIdx") long kuserIdx);

    /** 스마트미러 상세 측정 내역 */
    @GET("/kiosk/kuser/{kuserIdx}/analysis-detail/{kanalyzedIdx}")
    Call<KioskAnalysisHistoryDetail> getAnalysisDetail(@Path("kuserIdx") long kuserIdx, @Path("kanalyzedIdx") long kanalyzedIdx);
}
