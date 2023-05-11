package com.planuri.rootonixsmartmirror.dto;

public class KioskAnalysisDetail {
    /** example: 12, 각질 점수 */
    public int deadSkinPt;

    /** 평가 내역들 */
    public String diagnosisDesc;
    public String diagnosisName;
    public String expertSuggest;
    public String expertSuggestItem1;
    public String expertSuggestItem2;

    /** example: 13, 모낭 당 모발 수 점수*/
    public int hairNumPt;

    /** example: 15 모발 두께 점수 */
    public int hairThickPt;

    /** example: 142, 키오스크 사용자 분석 번호 **/
    public int kanalyzedIdx;

    /** example: 11, 유분 점수 */
    public int sebumPt;

    /** example: 13, 민감도 점수 */
    public int sensitivityPt;

    /** "탈모가 의심되고 예민한 두피로 판단됩니다. 모낭 당 모발 수..." */
    public String totalDesc;

    /** example: 76.2, 종합 점수 */
}
