package com.planuri.rootonixsmartmirror.util.gson_converter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Time;

public class TimeDeserializer implements JsonDeserializer<Time> {

    @Override
    public Time deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String time = json.getAsString();
        return Time.valueOf(time);
    }

}
