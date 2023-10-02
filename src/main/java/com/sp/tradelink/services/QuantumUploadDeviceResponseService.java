package com.sp.tradelink.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.tradelink.gateways.HttpDeviceToServerGateway;
import com.sp.tradelink.models.QuantumUploadResponse;
import com.sp.tradelink.utils.MessageCreatorHelper;
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
public class QuantumUploadDeviceResponseService {
    public final Logger logger;

    @Autowired
    private HttpDeviceToServerGateway httpDeviceToServerGateway;

    @Qualifier("hb-response-out-channel")
    @Autowired
    private MessageChannel sendDataToBrandLink;
    public QuantumUploadDeviceResponseService(Logger logger) {
        this.logger = logger;
    }

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
            response.setResultCode("-1");
            response.setResultMsg("Error converting response format.");
        }

        logger.debug("Will return upload response to device.\n{}",response.toString());

        MessageBuilder<?> uploadResponse = MessageCreatorHelper.createMessageWithMergedHeaders(response.toString(),
                request.getHeaders());
        if (request.getHeaders().containsKey(MsgHeaderConstants.SOURCE_HEADER)) {
            sendDataToBrandLink.send(uploadResponse.build());
        }
        return uploadResponse.build();
    }

}
