package com.planuri.rootonixsmartmirror.dto;

public class CorrectionDto extends CommonResponse{

    public String kuserBirthDate;   //키오스크 사용자 생년월일
    public String kuserName;        //키오스크 사용자 이름
    public char kuserSex;           //키오스크 사용자 성별
    public String kuserTel;         //키오스크 사용자 전화번호

    public CorrectionDto(String kuserBirthDate, String kuserName, char kuserSex, String kuserTel) {
        this.kuserBirthDate = kuserBirthDate;
        this.kuserName = kuserName;
        this.kuserSex = kuserSex;
        this.kuserTel = kuserTel;
    }

    @Override
    public String toString() {
        return "RegKioskUserDto{" +
                "kuserBirthDate='" + kuserBirthDate + '\'' +
                ", kuserName='" + kuserName + '\'' +
                ", kuserSex=" + kuserSex +
                ", kuserTel='" + kuserTel + '\'' +
                '}';
    }
}
