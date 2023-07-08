package com.sp.tradelink.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class BrandUploadDeviceResponseService {
    private static final Logger logger = LoggerFactory.getLogger(BrandUploadDeviceResponseService.class);

    @Autowired
    private QuantumUploadDeviceResponseService cloudService;

    @ServiceActivator(inputChannel = "device-response-in-channel", outputChannel = "device-response-out-channel")
    public Message<?> publishDeviceToServerRequest(Message<?> message) {
        logger.info("Will start quantum upload process.");
        return message;
    }

    @ServiceActivator(inputChannel = "device-response-out-channel", outputChannel = "device-response-reply-channel")
    public Message<?> processDeviceToServerRequest(Message<?> message) throws JsonProcessingException {
        logger.info("Processing the quantum upload process.");
        return cloudService.startUpload(message);
    }

    @ServiceActivator(inputChannel = "device-response-reply-channel")
    public void sendDeviceToServerResponse(Message<?> message) {
        logger.info("Will send the upload response back to caller.");
        MessageChannel replyChannel = (MessageChannel) message.getHeaders().getReplyChannel();
        replyChannel.send(message);
    }
}
