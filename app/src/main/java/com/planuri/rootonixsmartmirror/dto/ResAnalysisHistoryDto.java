package com.planuri.rootonixsmartmirror.dto;

import com.planuri.rootonixsmartmirror.model.kioskuser.KioskAnalysisHistory;

import java.util.List;

public class ResAnalysisHistoryDto {
    public boolean success;
    public int code;
    public String msg;

    public List<KioskAnalysisHistory> list;
}
