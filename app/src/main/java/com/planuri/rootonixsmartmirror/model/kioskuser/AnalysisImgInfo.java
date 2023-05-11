package com.planuri.rootonixsmartmirror.model.kioskuser;

public class AnalysisImgInfo {
    //두피 촬영 위치. 1=앞머리, 2=정수리, 3=좌측두부, 4=우측두부, 5=후두부
    public int headPosition;
    //두피 촬영 위치 별(앞머리, 정수리...등) 이미지 순서. 1,2,3...
    public int imgNum;
    //이미지 소스 구분. 1=원본, 2=분석본
    public int imgSource;
    //이미지 URL. example: /path/kuseridx_bh_2_yyyymmddhhmmss.jpg
    public String imgUrl;

    public AnalysisImgInfo() {
        this.headPosition = 0;
        this.imgNum = 0;
        this.imgSource = 0;
        this.imgUrl = null;
    }
}
