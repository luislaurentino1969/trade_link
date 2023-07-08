package com.sp.tradelink.gateways;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(defaultRequestChannel = "serverToDeviceChannel", defaultReplyChannel = "serverToDeviceReplyChannel")
public interface HttpServerToDeviceGateway {
    Message<?> startHeartbeat(Message<?> request);
}
