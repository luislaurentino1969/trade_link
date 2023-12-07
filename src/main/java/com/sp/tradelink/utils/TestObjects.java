package com.sp.tradelink.utils;

import com.sp.tradelink.models.QuantumDefaultRequest;
import com.sp.tradelink.models.QuantumDefaultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public abstract class TestObjects<Z extends QuantumDefaultRequest,R extends QuantumDefaultResponse, S extends DefaultService> {
    private S service;
    private Z requestObject;
    private R responseObject;

    public TestObjects<Z, R, S> setService(S service) {
        this.service = service;
        return this;
    }

    @SuppressWarnings("unchecked")
    public ResponseEntity<R> getListJsonFormat(@RequestBody Z info, @RequestHeader Map<String, String> headers) {
        requestObject = info;
        //implement validation
        responseObject = (R) service.getDefaultErrorResponse(requestObject);
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }
    @SuppressWarnings("unchecked")
    public ResponseEntity<R> getListXmlFormat(@RequestBody Z info, @RequestHeader Map<String, String> headers) {
        requestObject = info;

        //implement validation
        responseObject = (R) service.getDefaultErrorResponse(requestObject);
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }
}
