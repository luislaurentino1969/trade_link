package com.sp.tradelink.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
public class BrandUploadDeviceResponseService {
    private final Logger logger;

    @Autowired
    private QuantumUploadDeviceResponseService cloudService;
    @Autowired
    private QuantumUploadRawMessageService uploadRawMessageService;

    public BrandUploadDeviceResponseService(Logger logger) {
        this.logger = logger;
    }

    @ServiceActivator(inputChannel = "upload-request-in-channel")
    public void processUploadResponse(Message<?> message) {
        logger.info("Processing the quantum upload process.");
        Executors.newSingleThreadExecutor().execute(() -> {
            var responseRawUpload = uploadRawMessageService.startUpload(message);
            var responseMsg = cloudService.startUpload(message);
        });
    }
}
