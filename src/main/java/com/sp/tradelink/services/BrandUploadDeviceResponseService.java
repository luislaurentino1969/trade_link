package com.sp.tradelink.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class BrandUploadDeviceResponseService {
    private final Logger logger;

    @Autowired
    private QuantumUploadDeviceResponseService cloudService;

    public BrandUploadDeviceResponseService(Logger logger) {
        this.logger = logger;
    }

    @ServiceActivator(inputChannel = "upload-request-in-channel")
    public Message<?> processHeartbeatRequest(Message<?> message) {
        logger.info("Processing the quantum upload process.");
        return cloudService.startUpload(message);
    }
}
