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

    @ServiceActivator(inputChannel = "heartbeat-in-channel", outputChannel = "heartbeat-out-channel")
    public Message<?> publishHeartbeatRequest(Object heartbeat) {
        logger.info("Will start quantum heartbeat process.");
        return MessageBuilder.withPayload(heartbeat).build();
    }
    @ServiceActivator(inputChannel = "heartbeat-in-channel", outputChannel = "heartbeat-out-channel")
    public Message<?> publishHeartbeatRequest(Message<?> heartbeat) {
        logger.info("Will start quantum heartbeat process.");
        return heartbeat;
    }

    @ServiceActivator(inputChannel = "heartbeat-out-channel", outputChannel = "heartbeat-reply-channel")
    public Message<?> processHeartbeatRequest(Message<?> heartbeat) throws JsonProcessingException {
        logger.info("Processing the quantum heartbeat process.");
        return cloudService.startHeartbeat(heartbeat);
    }

    @ServiceActivator(inputChannel = "heartbeat-reply-channel")
    public void sendHeartbeatResponse(Message<?> heartbeat) {
        logger.info("Will send the heartbeat response back to caller.");
        MessageChannel replyChannel = (MessageChannel) heartbeat.getHeaders().getReplyChannel();
        if (replyChannel != null) {
            replyChannel.send(heartbeat);
        } else {
            logger.debug("reply channel not defined.");
        }
    }
}
