package com.sp.tradelink.gateways;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(defaultRequestChannel = "uploadRawMessageChannel", defaultReplyChannel = "uploadRawMessageReplyChannel")
public interface HttpUploadRawMessageGateway {
    Message<?> sendRawMessageToCloud(Message<?> request);
}
