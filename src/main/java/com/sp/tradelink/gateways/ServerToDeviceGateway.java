package com.sp.tradelink.gateways;

import com.sp.tradelink.models.QuantumHBRequest;
import com.sp.tradelink.models.QuantumHBResponse;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(defaultRequestChannel = "serverToDeviceChannel", defaultReplyChannel = "serverToDeviceReplyChannel")
public interface ServerToDeviceGateway {
    Message<?> startHeartbeat(Message<?> request);
}
