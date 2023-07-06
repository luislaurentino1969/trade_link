package com.sp.tradelink.controllers;

import com.sp.tradelink.config.ActiveMQInboundConfig;
import com.sp.tradelink.models.QuantumHBRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("heartbeat/v1")
@IntegrationComponentScan("com.sp.tradelink")
public class HeartbeatController {
    private final Logger logger = LoggerFactory.getLogger(HeartbeatController.class);

    @Value("${app.server.url}")
    private String SERVER;

    @Value("${app.server.endpoint}")
    private String ENDPOINT;

    @Autowired
    private ActiveMQInboundConfig.HeartbeatResponseGateway gateway;

    @RequestMapping(value="/quantum", method= RequestMethod.POST, produces={"application/json"})
    public ResponseEntity<?> initiateMainLinkHeartbeat(@RequestBody QuantumHBRequest info, @RequestHeader Map<String, String> headers) {

//        return new ResponseEntity<>(callAPIUsingRestTemplate(info), HttpStatus.OK);
        gateway.publishHeartbeatResponse(info);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
