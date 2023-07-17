package com.sp.tradelink.controllers;

import com.sp.tradelink.models.QuantumUploadRequest;
import com.sp.tradelink.services.QuantumUploadDeviceResponseService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("device-to-server/v1")
@IntegrationComponentScan("com.sp.tradelink")
public class DeviceToServerController {
    private final Logger logger;

    @Autowired
    QuantumUploadDeviceResponseService service;

    public DeviceToServerController(Logger logger) {
        this.logger = logger;
    }

    @RequestMapping(value="/quantum", method= RequestMethod.POST, produces={"application/json"})
    public ResponseEntity<?> initiateMainLinkHeartbeat(@RequestBody QuantumUploadRequest info, @RequestHeader Map<String, String> headers) {
        return new ResponseEntity<>(service.startUpload(MessageBuilder.withPayload(info).build()).getPayload(), HttpStatus.OK);
    }
}
