package com.sp.tradelink.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sp.tradelink.gateways.HttpServerToDeviceGateway;
import com.sp.tradelink.models.QuantumHBResponse;
import com.sp.tradelink.models.QuantumUploadRequest;
import com.sp.tradelink.utils.MessageCreatorHelper;
import com.sp.tradelink.utils.MsgHeaderConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class QuantumHeartbeatRequestService {
    public final Logger logger;

    private final MessageChannel sendDataToBrandLink;

    private final HttpServerToDeviceGateway httpServerToDeviceGateway;
    private final BrandHttpService httpBrandService;
    private final QuantumUploadDeviceResponseService service;

    public QuantumHeartbeatRequestService(Logger logger, @Qualifier("hb-response-out-channel") MessageChannel sendDataToBrandLink,
                                          HttpServerToDeviceGateway httpServerToDeviceGateway, BrandHttpService httpBrandService, QuantumUploadDeviceResponseService service) {
        this.logger = logger;
        this.sendDataToBrandLink = sendDataToBrandLink;
        this.httpServerToDeviceGateway = httpServerToDeviceGateway;
        this.httpBrandService = httpBrandService;
        this.service = service;
    }

    public ObjectNode managePreviousHB(String serialNumber, String action, String brand) {
        return httpBrandService.managePreviousHB(serialNumber, action, brand);
    }

    public Message<?> startHeartbeat(Message<?> request) {
        logger.debug("Will send the heartbeat request to cloud.\n{}", request.toString());

        QuantumHBResponse response = new QuantumHBResponse();
        var hbResponse = httpServerToDeviceGateway.startHeartbeat(MessageBuilder.withPayload(request.getPayload())
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader(HttpHeaders.CONTENT_LENGTH, request.toString().length())
                .setHeader(HttpHeaders.HOST, "TradeLink")
                .build()).getPayload();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
//            response = objectMapper.convertValue(objectMapper.convertValue(hbResponse, JsonNode.class)
//                    .get("d"), QuantumHBResponse.class);
            response = objectMapper.convertValue(objectMapper.convertValue(hbResponse, JsonNode.class), QuantumHBResponse.class);
        } catch (Exception ex) {
            response.setResultCode("-1");
            response.setResultMsg("Error converting response format.");
        }
        logger.debug("Will return heartbeat response to device.\n{}", response.toString());

        var responseMessage = MessageCreatorHelper.createMessageWithMergedHeaders(response.toString(),
                request.getHeaders());
        try {

            if (request.getHeaders().containsKey(MsgHeaderConstants.SOURCE_HEADER)) {
                if (request.getHeaders().get(MsgHeaderConstants.SOURCE_HEADER).equals("myself") &&
                        request.getHeaders().containsKey(MsgHeaderConstants.BRAND_HEADER) &&
                        response.getResultCode().equals("0")) {
                    QuantumUploadRequest deviceResponse = httpBrandService.sendHttpToBrandLink(response.toString(),
                            request.getHeaders().get(MsgHeaderConstants.BRAND_HEADER).toString());
                    ((ObjectNode) response.getExtData()).put("DeviceToServerResponse", objectMapper
                            .readTree((String) service.startUpload(MessageBuilder.withPayload(deviceResponse).build())
                                    .getPayload()));
                    responseMessage = MessageCreatorHelper.createMessageWithMergedHeaders(response.toString(),
                            request.getHeaders());
                } else if (!request.getHeaders().get(MsgHeaderConstants.SOURCE_HEADER).equals("myself")) {
                    sendDataToBrandLink.send(responseMessage.build());
                }
            }
        } catch (Exception ex) {
            response.setResultTxt("Error parsing response data.");
            responseMessage = MessageCreatorHelper.createMessageWithMergedHeaders(response.toString(),
                    request.getHeaders());
        }
        return responseMessage.build();
    }

}
