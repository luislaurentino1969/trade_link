package com.sp.tradelink.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class BrandHeartbeatRequestService {
    private final Logger logger;

    @Autowired
    private QuantumHeartbeatRequestService cloudService;

    public BrandHeartbeatRequestService(Logger logger) {
        this.logger = logger;
    }

    @ServiceActivator(inputChannel = "hb-request-in-channel")
    public Message<?> processHeartbeatRequest(Message<?> heartbeat) {
        logger.info("Processing the quantum heartbeat process.");
        return cloudService.startHeartbeat(heartbeat);
    }
}
