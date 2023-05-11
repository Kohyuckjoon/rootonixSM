package com.planuri.rootonixsmartmirror.dto;

import com.planuri.rootonixsmartmirror.model.kioskuser.AnalysisImgInfo;

import java.util.ArrayList;
import java.util.List;

public class ReqSaveAnalysisDto {

    public int deadSkinPt;      //각질점수
    public int hairNumPt;       //모낭당 모발수
    public int hairThickPt;     //모발 두께 점수
    public int sebumPt;         //유분 점수
    public int sensitivityPt;   //민감도 점수

    public String diagnosisName;    //진단 타이틀, 진단명
    public String diagnosisDesc;    //진단 설명
    public String totalDesc;        //종합평가
    public String expertSuggest;    //전문가 제안
    public String expertSuggestItem1;    //전문가 제안 항목 1
    public String expertSuggestItem2;    //전문가 제안 항목 2

    public List<AnalysisImgInfo> analysisImgInfo = new ArrayList<>();

    public ReqSaveAnalysisDto() {}
}
