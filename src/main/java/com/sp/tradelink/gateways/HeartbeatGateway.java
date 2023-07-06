package com.sp.tradelink.gateways;

import com.sp.tradelink.models.QuantumHBRequest;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway
public interface HeartbeatGateway {

    @Gateway(requestChannel = "heartbeat-in-channel")
    Message<?> startHeartbeat(QuantumHBRequest request);
}
