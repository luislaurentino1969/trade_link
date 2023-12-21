package com.sp.tradelink.services.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.tradelink.models.QuantumUploadRawRequest;
import com.sp.tradelink.models.QuantumUploadRequest;
import org.slf4j.Logger;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.Executors;

@Service
public class BrandUploadDeviceResponseService {
    private final Logger logger;
    private final QuantumUploadDeviceResponseService cloudService;
    private final QuantumUploadRawMessageService uploadRawMessageService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public BrandUploadDeviceResponseService(Logger logger, QuantumUploadDeviceResponseService cloudService, QuantumUploadRawMessageService uploadRawMessageService, ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.logger = logger;
        this.cloudService = cloudService;
        this.uploadRawMessageService = uploadRawMessageService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    @ServiceActivator(inputChannel = "upload-request-in-channel")
    public void processUploadResponse(Message<?> message) {
        logger.info("Processing the quantum upload process.");
        Executors.newWorkStealingPool().execute(() -> {

//        threadPoolTaskScheduler.schedule(() -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                QuantumUploadRawRequest uploadRawRequest = mapper.readValue(message.getPayload().toString(), QuantumUploadRawRequest.class);
                //&& Integer.parseInt(uploadRequest.getTraceNum()) > 0
                if (uploadRawRequest.getTraceNum() != null && uploadRawRequest.getRawResponse() != null) {
                    var responseRawUpload = uploadRawMessageService.startUpload(MessageBuilder.createMessage(uploadRawRequest.toString(), message.getHeaders()));
                }
                QuantumUploadRequest uploadRequest = mapper.readValue(message.getPayload().toString(), QuantumUploadRequest.class);
                var responseMsg = cloudService.startUpload(MessageBuilder.createMessage(uploadRequest.toString(), message.getHeaders()));

            } catch (Exception ex) {
                logger.error("Error parsing message to UpLoadRaw service.", ex);
                var responseMsg = cloudService.startUpload(message);
            }
//        }, Instant.now().plusMillis(300));
        });
    }
}
