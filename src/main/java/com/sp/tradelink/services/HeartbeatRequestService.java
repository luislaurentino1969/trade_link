package com.sp.tradelink.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.tradelink.models.QuantumHBRequest;
import com.sp.tradelink.models.QuantumHBResponse;
import com.sp.tradelink.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class HeartbeatRequestService {
    private static final Logger logger = LoggerFactory.getLogger(HeartbeatRequestService.class);
    @Value("${app.server.url}")
    private String SERVER;

    @Value("${app.server.endpoint}")
    private String ENDPOINT;

    @ServiceActivator(inputChannel = "heartbeat-in-channel", outputChannel = "heartbeat-out-channel")
    public QuantumHBRequest publishHeartbeatRequest(QuantumHBRequest heartbeat) {
        logger.info("Will start quantum heartbeat process.");
        return heartbeat;
    }

    @ServiceActivator(inputChannel = "heartbeat-out-channel", outputChannel = "heartbeat-reply-channel")
    public QuantumHBResponse processHeartbeatRequest(QuantumHBRequest heartbeat) throws JsonProcessingException {
        logger.info("Processing the quantum heartbeat process.");
        ObjectMapper objectMapper = new ObjectMapper();
        QuantumHBResponse response = objectMapper.readValue(HttpUtils.callAPIUsingWebClient(heartbeat,
                        SERVER, ENDPOINT),
                QuantumHBResponse.class);
        return response;
    }

    @ServiceActivator(inputChannel = "heartbeat-reply-channel")
    public void sendHeartbeatResponse(Message<QuantumHBResponse> heartbeat) throws JsonProcessingException {
        logger.info("Will send the heartbeat response back to caller.");
        MessageChannel replyChannel = (MessageChannel) heartbeat.getHeaders().getReplyChannel();
        replyChannel.send(heartbeat);
    }
}
