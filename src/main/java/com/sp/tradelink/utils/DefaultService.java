package com.sp.tradelink.utils;

import org.springframework.stereotype.Service;

@Service
public abstract class DefaultService {
    public NotImplementedResponse getDefaultErrorResponse(Object request) {
        return new NotImplementedResponse();
    }
}
