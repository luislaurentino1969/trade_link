package com.sp.tradelink.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

@Service
public class BrandHeartbeatRequestService {
    private static final Logger logger = LoggerFactory.getLogger(BrandHeartbeatRequestService.class);

    @Autowired
    private QuantumHeartbeatRequestService cloudService;

    @ServiceActivator(inputChannel = "hb-request-in-channel")
    public Message<?> processHeartbeatRequest(Message<?> heartbeat) {
        logger.info("Processing the quantum heartbeat process.");
        return cloudService.startHeartbeat(heartbeat);
    }
}
