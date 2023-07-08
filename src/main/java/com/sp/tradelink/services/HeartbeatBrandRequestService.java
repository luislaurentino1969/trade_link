package com.sp.tradelink.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sp.tradelink.gateways.DeviceToServerGateway;
import com.sp.tradelink.models.QuantumHBRequest;
import com.sp.tradelink.models.QuantumHBResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class HeartbeatBrandRequestService {
    private static final Logger logger = LoggerFactory.getLogger(HeartbeatBrandRequestService.class);

    @Autowired
    private QuantumHeartbeatRequestService cloudGateway;

    @ServiceActivator(inputChannel = "heartbeat-in-channel", outputChannel = "heartbeat-out-channel")
    public Message<?> publishHeartbeatRequest(Message<?> heartbeat) {
        logger.info("Will start quantum heartbeat process.");
        return heartbeat;
    }

    @ServiceActivator(inputChannel = "heartbeat-out-channel", outputChannel = "heartbeat-reply-channel")
    public Message<?> processHeartbeatRequest(Message<?> heartbeat) throws JsonProcessingException {
        logger.info("Processing the quantum heartbeat process.");
        return cloudGateway.startHeartbeat(heartbeat);
    }

    @ServiceActivator(inputChannel = "heartbeat-reply-channel")
    public void sendHeartbeatResponse(Message<?> heartbeat) {
        logger.info("Will send the heartbeat response back to caller.");
        MessageChannel replyChannel = (MessageChannel) heartbeat.getHeaders().getReplyChannel();
        replyChannel.send(heartbeat);
    }
}
