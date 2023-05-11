package com.planuri.rootonixsmartmirror.dto;

public class RegKioskUserDto {

    public String kuserBirthDate;   //키오스크 사용자 생년월일
    public String kuserName;        //키오스크 사용자 이름
    public char kuserSex;           //키오스크 사용자 성별
    public String kuserTel;         //키오스크 사용자 전화번호
    public int regKioskIdx;         //등록된 키오스크 번호(첫 기기이므로 1로 보내면 됨)

    /*public RegKioskUserDto(String kuserName, String kuserTel, char kuserSex, String kuserBirthDate, int regKioskIdx) {
        this.kuserBirthDate = kuserBirthDate;
        this.kuserName = kuserName;
        this.kuserSex = kuserSex;
        this.kuserTel = kuserTel;
        this.regKioskIdx = regKioskIdx;
    }*/

    public RegKioskUserDto(String kuserBirthDate, String kuserName, char kuserSex, String kuserTel, int regKioskIdx) {
        this.kuserBirthDate = kuserBirthDate;
        this.kuserName = kuserName;
        this.kuserSex = kuserSex;
        this.kuserTel = kuserTel;
        this.regKioskIdx = regKioskIdx;
    }

    @Override
    public String toString() {
        return "RegKioskUserDto{" +
                "kuserBirthDate='" + kuserBirthDate + '\'' +
                ", kuserName='" + kuserName + '\'' +
                ", kuserSex=" + kuserSex +
                ", kuserTel='" + kuserTel + '\'' +
                ", regKioskIdx=" + regKioskIdx +
                '}';
    }
}
