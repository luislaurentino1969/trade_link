package com.sp.tradelink.controllers;

import com.sp.tradelink.gateways.HeartbeatGateway;
import com.sp.tradelink.models.QuantumHBRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("heartbeat/v1")
@IntegrationComponentScan("com.sp.tradelink")
public class HeartbeatController {
    private final Logger logger = LoggerFactory.getLogger(HeartbeatController.class);

    @Autowired
    private HeartbeatGateway gateway;

    @RequestMapping(value="/quantum", method= RequestMethod.POST, produces={"application/json"})
    public ResponseEntity<?> initiateMainLinkHeartbeat(@RequestBody QuantumHBRequest info, @RequestHeader Map<String, String> headers) {

//        return new ResponseEntity<>(callAPIUsingRestTemplate(info), HttpStatus.OK);
        return new ResponseEntity<>(gateway.startHeartbeat(MessageBuilder.withPayload(info).build()).getPayload(), HttpStatus.OK);
    }

}
