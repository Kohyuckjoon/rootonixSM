package com.planuri.rootonixsmartmirror.model.kioskuser;

public class KioskAnalysisHistory {

    /** example: 142, 키오스크 사용자 분석 번호 **/
    public int kanalyzedIdx;

    /** example: 12, 각질 점수 */
    public int deadSkinPt;
    /** example: 13, 모낭 당 모발 수 점수*/
    public int hairNumPt;
    /** example: 15 모발 두께 점수 */
    public int hairThickPt;
    /** example: 11, 유분 점수 */
    public int sebumPt;
    /** example: 13, 민감도 점수 */
    public int sensitivityPt;

    /** example: 76.2, 종합 점수 */
    public int totalPt;

    /** example: 2022-09-01 13:42:33, 분석 날짜 **/
    public String analyzedDate;
//    public String analyzedDayWeek;
//    public String analyzedTime;
//    public int deadSkinPtP;
//    public int sensitivityPtP;
//    public int hairThickPtP;
//    public int sebumPtP;
//    public int hairNumPtP;
}
