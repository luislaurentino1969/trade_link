package com.sp.tradelink.gateways;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(defaultRequestChannel = "deviceToServerChannel", defaultReplyChannel = "deviceToServerReplyChannel")
public interface HttpDeviceToServerGateway {
    Message<?> sendResponseToCloud(Message<?> response);
}
