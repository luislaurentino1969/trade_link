package com.sp.tradelink.services.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.tradelink.gateways.HttpUploadRawMessageGateway;
import com.sp.tradelink.models.DefaultErrorResponse;
import com.sp.tradelink.utils.MessageCreatorHelper;
import com.sp.tradelink.utils.MsgHeaderConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

@Service
public class QuantumUploadRawMessageService {
    public final Logger logger;

    private final HttpUploadRawMessageGateway httpUploadRawMessageGateway;

    private final MessageChannel sendDataToBrandLink;
    public QuantumUploadRawMessageService(Logger logger, HttpUploadRawMessageGateway httpUploadRawMessageGateway, @Qualifier("hb-response-out-channel") MessageChannel sendDataToBrandLink) {
        this.logger = logger;
        this.httpUploadRawMessageGateway = httpUploadRawMessageGateway;
        this.sendDataToBrandLink = sendDataToBrandLink;
    }

    public Message<?> startUpload(Message<?> request) {
        logger.debug("Will upload device raw message to cloud.\n{}",request.toString());

        DefaultErrorResponse response = new DefaultErrorResponse();
        MessageHeaders headers = request.getHeaders();
        var hbResponse = httpUploadRawMessageGateway.sendRawMessageToCloud(MessageBuilder.withPayload(request.getPayload())
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader(HttpHeaders.CONTENT_LENGTH, request.getPayload().toString().length())
                .setHeader(HttpHeaders.HOST, "TradeLink")
                .setHeader(HttpHeaders.CONNECTION, "close")
                .build()).getPayload();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = objectMapper.convertValue(objectMapper.convertValue(hbResponse, JsonNode.class),
                    DefaultErrorResponse.class);
        } catch (Exception ex) {
            response.setResultCode("-1");
            response.setResultMsg("Incorrect response format from cloud.");
            response.setResultTxt("Response received ===> " + hbResponse);
        }
        MessageBuilder<?> uploadResponse = MessageCreatorHelper.createMessageWithMergedHeaders(response.toString(),
                headers).setHeader(MsgHeaderConstants.TARGET_HEADER, "UploadRawResponse");
        return uploadResponse.build();
    }

}
