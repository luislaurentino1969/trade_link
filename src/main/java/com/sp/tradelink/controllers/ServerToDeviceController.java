package com.sp.tradelink.controllers;

import com.sp.tradelink.models.QuantumHBRequest;
import com.sp.tradelink.services.QuantumHeartbeatRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("server-to-device/v1")
@IntegrationComponentScan("com.sp.tradelink")
public class ServerToDeviceController {
    private final Logger logger = LoggerFactory.getLogger(ServerToDeviceController.class);

    @Autowired
    private QuantumHeartbeatRequestService service;

    @RequestMapping(value="/quantum", method= RequestMethod.POST, produces={"application/json"})
    public ResponseEntity<?> initiateMainLinkHeartbeat(@RequestBody QuantumHBRequest info, @RequestHeader Map<String, String> headers) {
        return new ResponseEntity<>(service.startHeartbeat(MessageBuilder.withPayload(info).build()).getPayload(), HttpStatus.OK);
    }
}
