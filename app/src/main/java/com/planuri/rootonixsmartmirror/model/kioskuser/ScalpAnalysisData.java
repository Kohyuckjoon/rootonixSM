package com.planuri.rootonixsmartmirror.model.kioskuser;

import java.util.ArrayList;
import java.util.List;

/** 두피분석 데이터 from AI SERVER */
public class ScalpAnalysisData {

    public AnalysisData total = new AnalysisData();

    public class AnalysisData {
        public int deadskin;
        public int sebum;
        public int sensitive;
        public int num_hairs;
        public int thickness;
        public List<String> images_origin;
        public List<String> images_result;
        public String title;
        public String short_explain;
        public String total_explain;
        public String expert_suggest;
        public List<String> expert_list = new ArrayList<String>();
    }
}
