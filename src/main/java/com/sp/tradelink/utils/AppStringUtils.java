package com.sp.tradelink.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class AppStringUtils {

    //convert map parameters into url parameters
    public static String convertMap2Str(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        return map.toString().replaceAll("[\\{\\}\\s]", "").replaceAll(",",
                "&");
    }

    public static String convertObject2Json(Object object) {
        return convertObject2Json(object, true);
    }

    public static String convertObject2Json(Object object, boolean includeNullFields) {
        if (object == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (includeNullFields)
                mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            else
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(object);
        } catch (Exception jex) {
            return null;
        }
    }

}
