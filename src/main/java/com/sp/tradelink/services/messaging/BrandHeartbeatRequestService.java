package com.sp.tradelink.services.messaging;

import org.slf4j.Logger;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.Executors;

@Service
public class BrandHeartbeatRequestService {
    private final Logger logger;

    private final QuantumHeartbeatRequestService cloudService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public BrandHeartbeatRequestService(Logger logger, QuantumHeartbeatRequestService cloudService, ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.logger = logger;
        this.cloudService = cloudService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    @ServiceActivator(inputChannel = "hb-request-in-channel")
    public void processHeartbeatRequest(Message<?> heartbeat) {
        if (heartbeat.getHeaders().containsKey("jms_timestamp") && heartbeat.getHeaders().get("jms_timestamp", Long.class) != null &&
                Instant.ofEpochMilli(heartbeat.getHeaders().get("jms_timestamp", Long.class)).plusSeconds(10).compareTo(Instant.now()) > 0) {
//            logger.debug("Processing the quantum heartbeat process. {} active threads.", threadPoolTaskScheduler.getActiveCount());
            Executors.newWorkStealingPool().execute(() -> {
                Thread.currentThread().setName(String.format("tl_Thread-%s_%s",heartbeat.getHeaders().get("Source"),Thread.currentThread().getName()));
                var responseMsg = cloudService.startHeartbeat(heartbeat);
            });
        } else {
            logger.warn("HB request for device {} has expired.", heartbeat.getPayload().toString().substring(
                    heartbeat.getPayload().toString().indexOf("{\"UserName\":\"")+13,
                    heartbeat.getPayload().toString().indexOf("\"",
                            heartbeat.getPayload().toString().indexOf("{\"UserName\":\"")+13)));
        }
    }
}
