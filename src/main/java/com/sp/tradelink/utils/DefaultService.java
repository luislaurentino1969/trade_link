package com.sp.tradelink.utils;

import com.sp.tradelink.models.DefaultErrorResponse;
import com.sp.tradelink.models.QuantumDefaultResponse;
import org.springframework.stereotype.Service;

@Service
public abstract class DefaultService {
    public NotImplementedResponse getDefaultErrorResponse(Object request) {
        return new NotImplementedResponse();
    }
}
