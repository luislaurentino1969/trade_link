package com.sp.tradelink.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.tradelink.gateways.HttpServerToDeviceGateway;
import com.sp.tradelink.models.QuantumHBResponse;
import com.sp.tradelink.utils.MsgHeaderConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class QuantumHeartbeatRequestService {
    public final Logger logger;

    @Qualifier("hb-response-out-channel")
    @Autowired
    private MessageChannel sendDataToBrandLink;

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
            response.setResultCode("-1");
            response.setResultMsg("Error converting response format.");
        }
        logger.debug("Will return heartbeat response to device.\n{}",response.toString());

        MessageBuilder<?> responseMessage = MessageBuilder.withPayload(response.toString());
        if (request.getHeaders().containsKey("ip_connectionId")) {
            responseMessage.setHeader("ip_connectionId", request.getHeaders().get("ip_connectionId"));
        }
        if (request.getHeaders().containsKey(MsgHeaderConstants.BRAND_HEADER)) {
            responseMessage.setHeader(MsgHeaderConstants.BRAND_HEADER, request.getHeaders().get(MsgHeaderConstants.BRAND_HEADER));
        }
        if (request.getHeaders().containsKey(MsgHeaderConstants.TARGET_HEADER)) {
            responseMessage.setHeader(MsgHeaderConstants.TARGET_HEADER, request.getHeaders().get(MsgHeaderConstants.TARGET_HEADER));
        }
        if (request.getHeaders().containsKey(MsgHeaderConstants.SOURCE_HEADER)) {
            responseMessage.setHeader(MsgHeaderConstants.SOURCE_HEADER, request.getHeaders().get(MsgHeaderConstants.SOURCE_HEADER));
        }
        if (request.getHeaders().containsKey(MsgHeaderConstants.REQUEST_TYPE)) {
            responseMessage.setHeader(MsgHeaderConstants.REQUEST_TYPE, request.getHeaders().get(MsgHeaderConstants.REQUEST_TYPE));
        }
        if (request.getHeaders().containsKey(MsgHeaderConstants.TRACE_NUM)) {
            responseMessage.setHeader(MsgHeaderConstants.TRACE_NUM, request.getHeaders().get(MsgHeaderConstants.TRACE_NUM));
        }

        if (request.getHeaders().containsKey("Source")) {
            sendDataToBrandLink.send(responseMessage.build());
        }
        return responseMessage.build();
    }

}
