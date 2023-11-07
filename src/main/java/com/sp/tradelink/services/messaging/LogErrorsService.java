package com.sp.tradelink.services.messaging;

import org.slf4j.Logger;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class LogErrorsService {
    private final Logger logger;

    public LogErrorsService(Logger logger) {
        this.logger = logger;
    }

    @ServiceActivator(inputChannel = "log-in-channel")
    public void processMessageReceived(Message<?> message) {
        if (message.getPayload() instanceof Throwable)
            logger.error("Logging error received.", (Throwable) message.getPayload());
    }
}
