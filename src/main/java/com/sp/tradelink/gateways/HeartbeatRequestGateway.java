package com.sp.tradelink.gateways;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(defaultRequestChannel = "heartbeat-in-channel")
public interface HeartbeatRequestGateway {
    Message<?> startHeartbeat(Message<?> request);
}
