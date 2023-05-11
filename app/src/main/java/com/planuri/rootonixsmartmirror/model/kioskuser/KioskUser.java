package com.planuri.rootonixsmartmirror.model.kioskuser;

/** 스마트미러(키오스크) 사용자 객체 */
public class KioskUser {

    public long kuserIdx;            // 키오스크 사용자 번호
    public String kuserName;        // 키오스크 사용자 이름
    public String kuserTel;         // 키오스크 사용자 전화번호
    public String kuserBirthDate;   // 키오스크 사용자 생년월일
    public char kuserSex;           // 키오스크 사용자 성별
//    public String kuserSex;
    public String createDate;       // 키오스크 사용자 등록일

    public KioskUser() {
        kuserIdx = 0;
        kuserName = null;
        kuserTel = null;
        kuserBirthDate = null;
        kuserSex = 0;
        createDate = null;
    }

    public void clear() {
        kuserIdx = 0;
        kuserName = null;
        kuserTel = null;
        kuserBirthDate = null;
        kuserSex = 0;
        createDate = null;
    }
}
