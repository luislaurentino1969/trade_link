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

    @ServiceActivator(inputChannel = "hb-request-in-channel")
    public Message<?> processHeartbeatRequest(Message<?> message) {
        logger.info("Processing the quantum upload process.");
        return cloudService.startUpload(message);
    }
}
