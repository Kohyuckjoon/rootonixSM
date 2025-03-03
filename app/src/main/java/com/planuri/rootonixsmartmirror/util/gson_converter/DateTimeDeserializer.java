package com.planuri.rootonixsmartmirror.util.gson_converter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Date;

public class DateTimeDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        /*String date = json.getAsString();
        return Date.valueOf(date);*/
        long time = json.getAsLong();
        return new Date(time);
    }
}
