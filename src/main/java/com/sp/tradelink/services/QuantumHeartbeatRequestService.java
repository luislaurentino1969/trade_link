package com.sp.tradelink.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.tradelink.gateways.HttpServerToDeviceGateway;
import com.sp.tradelink.models.QuantumHBResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class QuantumHeartbeatRequestService {
    public final Logger logger;

    @Autowired
    private HttpServerToDeviceGateway httpServerToDeviceGateway;

    public QuantumHeartbeatRequestService(Logger logger) {
        this.logger = logger;
    }

    public Message<?> startHeartbeat(Message<?> request) {
        logger.debug("Will send the heartbeat request to cloud.\n{}",request.toString());

        QuantumHBResponse response = new QuantumHBResponse();
        var hbResponse = httpServerToDeviceGateway.startHeartbeat(MessageBuilder.withPayload(request.getPayload())
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader(HttpHeaders.CONTENT_LENGTH, request.toString().length())
                .setHeader(HttpHeaders.HOST, "TradeLink")
                .build()).getPayload();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = objectMapper.convertValue(objectMapper.convertValue(hbResponse, JsonNode.class)
                    .get("d"),QuantumHBResponse.class);
        } catch (Exception ex) {
            response.setResultCode(-1);
            response.setResultMsg("Error converting response format.");
        }
        logger.debug("Will return heartbeat response to device.\n{}",response.toString());

        MessageBuilder<?> responseMessage = MessageBuilder.withPayload(response.toString());
        if (request.getHeaders().containsKey("Brand")) {
            responseMessage.setHeader("Brand", request.getHeaders().get("Brand"));
        }
        if (request.getHeaders().containsKey("Target")) {
            responseMessage.setHeader("Target", request.getHeaders().get("Target"));
        }
        if (request.getHeaders().containsKey("Source")) {
            responseMessage.setHeader("Source", request.getHeaders().get("Source"));
        }
        if (request.getHeaders().containsKey("COMMAND_TYPE")) {
            responseMessage.setHeader("COMMAND_TYPE", request.getHeaders().get("COMMAND_TYPE"));
        }
        if (request.getHeaders().containsKey("TRACE_NUM")) {
            responseMessage.setHeader("TRACE_NUM", request.getHeaders().get("TRACE_NUM"));
        }
        return responseMessage.build();
    }

}
