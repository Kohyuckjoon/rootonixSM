package com.planuri.rootonixsmartmirror.model;

public class Header {
    final String SUCCESS = "00";
    final String APPLICATION_ERROR = "01";
    final String DB_ERROR = "02";
    final String NO_DATA_ERROR = "03";
    final String HTTP_ERROR = "04";
    final String SERVICE_TIME_ERROR = "05";
    final String INVALID_REQUEST_PARAMETER_ERROR = "10";
    final String NO_MANDATORY_REQUEST_PARAMETERS_ERROR = "11";
    final String NO_OPEN_API_SERVICE_ERROR = "12";
    final String SERVICE_ACCESS_DENIED_ERROR = "20";
    final String TEMPORARILY_DISABLE_THE_SERVICE_KEY_ERROR = "21";
    final String LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR = "22";
    final String SERVICE_KEY_IS_NOT_REGISTERED_ERROR = "30";
    final String DEADLINE_HAS_EXPIRED_ERROR = "31";
    final String UNREGISTERED_IP_ERROR = "32";
    final String UNSIGNED_CALL_ERROR = "33";
    final String UNKNOWN_ERROR = "99";


    public String resultCode;
    public String resultMsg;



    /**
     * 헤더의 resultCode 상태 출력
     * @return 에러메세지
     */
    public void parseResultCode() {
        String msg = "";
        switch (this.resultCode) {
            case SUCCESS :
                msg = "정상";
                break;
            case APPLICATION_ERROR:
                msg = "어플리케이션 에러";
                break;
            case DB_ERROR:
                msg = "데이터베이스 에러";
                break;
            case NO_DATA_ERROR:
                msg = "데이터 없음 에러";
                break;
            case HTTP_ERROR:
                msg = "HTTP 에러";
                break;
            case SERVICE_TIME_ERROR:
                msg = "서비스 연결 실패 에러";
                break;
            case INVALID_REQUEST_PARAMETER_ERROR:
                msg = "잘못된 요청 파라미터 에러";
                break;
            case NO_MANDATORY_REQUEST_PARAMETERS_ERROR:
                msg = "필수 요청 파라미터가 없음";
                break;
            case NO_OPEN_API_SERVICE_ERROR:
                msg = "해당 오픈 API서비스가 없거나 폐기됨";
                break;
            case SERVICE_ACCESS_DENIED_ERROR:
                msg = "서비스 접근거부";
                break;
            case TEMPORARILY_DISABLE_THE_SERVICE_KEY_ERROR:
                msg = "일시적으로 사용할 수 없는 서비스 키";
                break;
            case LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR:
                msg = "서비스 요청 제한 횟수 초과 에러";
                break;
            case SERVICE_KEY_IS_NOT_REGISTERED_ERROR:
                msg = "등록되지 않은 서비스 키";
                break;
            case DEADLINE_HAS_EXPIRED_ERROR:
                msg = "기한만료된 서비스 키";
                break;
            case UNREGISTERED_IP_ERROR:
                msg = "등록되지 않은 IP";
                break;
            case UNSIGNED_CALL_ERROR:
                msg = "서명되지 않은 호출";
                break;
            case UNKNOWN_ERROR:
                msg = "기타 에러";
                break;
        }
        this.resultMsg = msg;
    }
}
