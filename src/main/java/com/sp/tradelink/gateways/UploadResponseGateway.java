package com.sp.tradelink.gateways;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(defaultRequestChannel = "device-response-in-channel")
public interface UploadResponseGateway {
    Message<?> uploadDeviceResponse(Message<?> request);
}
