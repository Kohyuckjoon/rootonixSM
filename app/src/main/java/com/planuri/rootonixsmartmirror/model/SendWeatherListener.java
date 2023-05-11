package com.planuri.rootonixsmartmirror.model;

import java.util.Map;

public interface SendWeatherListener {

    public void sendWeaterMessage(int temp, String code, Map<String, Integer> weatherMap);
}
