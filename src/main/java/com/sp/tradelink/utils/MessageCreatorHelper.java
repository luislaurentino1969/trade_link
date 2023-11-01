package com.sp.tradelink.utils;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MessageCreatorHelper {
    public static MessageBuilder<?> createMessageWithMergedHeaders(Object payload, MessageHeaders headers) {
        var responseMessage = MessageBuilder.withPayload(payload);
        headers.forEach((headerKey, headerValue) -> {
            if (!headerKey.equals("id") && !headerKey.equals("timestamp")) {
                responseMessage.setHeaderIfAbsent(headerKey, headerValue);
            }
        });
        return responseMessage;
    }

    public static Map<String, Object> addMissingMessageHeaders(MessageHeaders headers) {
        Map<String, Object> result = new HashMap<>();
        if (headers.containsKey("ip_connectionId")) {
            result.put("ip_connectionId", headers.get("ip_connectionId"));
        }
        if (headers.containsKey(MsgHeaderConstants.BRAND_HEADER)) {
            result.put(MsgHeaderConstants.BRAND_HEADER, headers.get(MsgHeaderConstants.BRAND_HEADER));
        }
        if (headers.containsKey(MsgHeaderConstants.TARGET_HEADER)) {
            result.put(MsgHeaderConstants.TARGET_HEADER, headers.get(MsgHeaderConstants.TARGET_HEADER));
        }
        if (headers.containsKey(MsgHeaderConstants.SOURCE_HEADER)) {
            result.put(MsgHeaderConstants.SOURCE_HEADER, headers.get(MsgHeaderConstants.SOURCE_HEADER));
        }
        if (headers.containsKey(MsgHeaderConstants.REQUEST_TYPE)) {
            result.put(MsgHeaderConstants.REQUEST_TYPE, headers.get(MsgHeaderConstants.REQUEST_TYPE));
        }
        if (headers.containsKey(MsgHeaderConstants.TRACE_NUM)) {
            result.put(MsgHeaderConstants.TRACE_NUM, headers.get(MsgHeaderConstants.TRACE_NUM));
        }
        return result;
    }
}
