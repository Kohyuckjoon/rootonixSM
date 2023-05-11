package com.planuri.rootonixsmartmirror.model.kioskuser;

import com.planuri.rootonixsmartmirror.dto.KioskAnalysisDetail;
import com.planuri.rootonixsmartmirror.dto.KioskAnalysisDetailDto;
import com.planuri.rootonixsmartmirror.util.CustomRestClient;

public class LoginKioskUser {

    private static final String TAG = CustomRestClient.class.getSimpleName();

    private static volatile LoginKioskUser instance; //singleton 객체. main memory에서 직접 접근 할 수 있게(thread 안전을 위해)

    private boolean isLogin = false; //로그인 여부
    private KioskUser kioskUser; // 로그인한 키오스크 사용자 정보
    private ScalpAnalysisData analysisData; //분석 데이터
    private KioskAnalysisDetailDto kioskAnalysisDetailDto;
    public KioskAnalysisHistory kioskAnalysisHistory;
    public KioskAnalysisDetail kioskAnalysisDetail;

    public boolean isHistoryCall = false;

    /** METHOD */
    private LoginKioskUser() {} //외부에서 생성 할 수 없게 생서자를 private 로

    /** Thread 안전한 getInstance method. 단일 thread 환경으로 생각되지만 혹시 하는 마음으로... */
    public static LoginKioskUser getInstance() {
        if (instance == null) {
            synchronized (LoginKioskUser.class) {
                if (instance == null) {
                    instance = new LoginKioskUser();
                }
            }
        }
        return instance;
    }

    /********************** 접근 가능한 Method 들 *********************/
    /** 로그인 상태 여부 */
    public boolean isLogin() {
        return this.isLogin;
    }

    /** 로그인 상태 변경 */
    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    /** KioskUser 반환 **/
    public KioskUser getKioskUser() {
        return this.kioskUser;
    }

    /** KioskUser 변경 */
    public void setAnalysisHistory(KioskAnalysisHistory kioskAnalysisHistory) {
        this.kioskAnalysisHistory = kioskAnalysisHistory;
    }

    public KioskAnalysisHistory getKioskAnalysisHistory(){
        return this.kioskAnalysisHistory;
    }

    public KioskAnalysisDetail getKioskAnalysisDetail(){
        return this.kioskAnalysisDetail;
    }

    public KioskAnalysisDetailDto getKioskAnalysisDetailDto(){
        return this.kioskAnalysisDetailDto;
    }

    /** KioskUser 변경 */
    public void setKioskUser(KioskUser kioskUser) {
        this.kioskUser = kioskUser;
    }

    public void setKioskUser(long kuserIdx, String kuserName, String kuserTel, String kuserBirthDate, char kuserSex, String createDate) {
        this.kioskUser.kuserIdx = kuserIdx;
        this.kioskUser.kuserName = kuserName;
        this.kioskUser.kuserTel = kuserTel;
        this.kioskUser.kuserBirthDate = kuserBirthDate;
        this.kioskUser.kuserSex = kuserSex;
        this.kioskUser.createDate = createDate;
    }

    /** 분석 데이터 정보 */
    public ScalpAnalysisData getAnalysisData() {
        return this.analysisData;
    }

    public void setAnalysisData(ScalpAnalysisData analysisData) {
        this.analysisData = analysisData;
    }

    public int getAnalysisTotal() {
        return (analysisData.total.deadskin + analysisData.total.num_hairs + analysisData.total.sebum +
                analysisData.total.sensitive + analysisData.total.thickness);
    }
}
