package com.sp.tradelink.services.messaging;

import org.slf4j.Logger;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.Executors;

@Service
public class BrandHeartbeatRequestService {
    private final Logger logger;

    private final QuantumHeartbeatRequestService cloudService;

    public BrandHeartbeatRequestService(Logger logger, QuantumHeartbeatRequestService cloudService) {
        this.logger = logger;
        this.cloudService = cloudService;
    }

    @ServiceActivator(inputChannel = "hb-request-in-channel")
    public void processHeartbeatRequest(Message<?> heartbeat) {
        if (heartbeat.getHeaders().containsKey("jms_timestamp") && heartbeat.getHeaders().get("jms_timestamp", Long.class) != null &&
                Instant.ofEpochMilli(heartbeat.getHeaders().get("jms_timestamp", Long.class)).plusSeconds(90).compareTo(Instant.now()) > 0) {
            logger.debug("Processing the quantum heartbeat process.");
            Executors.newSingleThreadExecutor().execute(() -> {
                var responseMsg = cloudService.startHeartbeat(heartbeat);
            });
        }
    }
}
