package com.planuri.rootonixsmartmirror.model.kioskuser;

import com.planuri.rootonixsmartmirror.dto.CommonResponse;

public class RecentAnalysis extends CommonResponse {

    public RecentAnalysisData data;

    public class RecentAnalysisData {
        /**
         * example: 12, 각질 점수
         */
        public int deadSkinPt = 0;
        /**
         * example: 13, 모낭 당 모발 수 점수
         */
        public int hairNumPt = 0;
        /**
         * example: 15 모발 두께 점수
         */
        public int hairThickPt = 0;
        /**
         * example: 11, 유분 점수
         */
        public int sebumPt = 0;
        /**
         * example: 13, 민감도 점수
         */
        public int sensitivityPt = 0;
    }
}
