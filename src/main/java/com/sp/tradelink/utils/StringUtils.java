package com.sp.tradelink.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class StringUtils {

    //convert map parameters into url parameters
    public static String convertMap2Str(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        return map.toString().replaceAll("[\\{\\}\\s]", "").replaceAll(",",
                "&");
    }

    public static String convertObject2Json(Object object) {
        if (object == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception jex) {
            return null;
        }
    }

}
