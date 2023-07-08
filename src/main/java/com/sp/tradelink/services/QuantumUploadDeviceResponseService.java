package com.sp.tradelink.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.tradelink.gateways.HttpDeviceToServerGateway;
import com.sp.tradelink.models.QuantumHBResponse;
import com.sp.tradelink.models.QuantumUploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class QuantumUploadDeviceResponseService {
    public static Logger logger = LoggerFactory.getLogger(QuantumUploadDeviceResponseService.class);

    @Autowired
    private HttpDeviceToServerGateway httpDeviceToServerGateway;

    public Message<?> startUpload(Message<?> request) {
        logger.debug("Will upload device response to cloud.\n{}",request.toString());

        QuantumUploadResponse response = new QuantumUploadResponse();
        var hbResponse = httpDeviceToServerGateway.sendResponseToCloud(MessageBuilder.withPayload(request.getPayload())
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader(HttpHeaders.CONTENT_LENGTH, request.toString().length())
                .setHeader(HttpHeaders.HOST, "TradeLink")
                .build()).getPayload();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = objectMapper.convertValue(objectMapper.convertValue(hbResponse, JsonNode.class)
                    .get("d"),QuantumUploadResponse.class);
        } catch (Exception ex) {
            response.setResultCode(-1);
            response.setResultMsg("Error converting response format.");
        }

        return MessageBuilder.withPayload(response).build();
    }

}
