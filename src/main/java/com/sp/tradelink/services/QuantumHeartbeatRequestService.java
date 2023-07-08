package com.sp.tradelink.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sp.tradelink.config.HeartbeatHttpOutboundGatewayConfig;
import com.sp.tradelink.gateways.DeviceToServerGateway;
import com.sp.tradelink.gateways.ServerToDeviceGateway;
import com.sp.tradelink.models.QuantumHBRequest;
import com.sp.tradelink.models.QuantumHBResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class QuantumHeartbeatRequestService {
    public static Logger logger = LoggerFactory.getLogger(QuantumHeartbeatRequestService.class);

    @Autowired
    private ServerToDeviceGateway serverToDeviceGateway;

//    @Autowired
//    private DeviceToServerGateway deviceToServerGateway;

    public Message<?> startHeartbeat(Message<?> request) {
        logger.debug("Will send the heartbeat request to cloud.\n{}",request.toString());

        QuantumHBResponse response = new QuantumHBResponse();
        var hbResponse = serverToDeviceGateway.startHeartbeat(MessageBuilder.withPayload(request.getPayload())
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader(HttpHeaders.CONTENT_LENGTH, request.toString().length())
                .setHeader(HttpHeaders.HOST, "TradeLink")
                .build()).getPayload();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = objectMapper.convertValue(objectMapper.convertValue(hbResponse, JsonNode.class)
                    .get("d"),QuantumHBResponse.class);
        } catch (Exception ex) {
            response.set_resultCode(-1);
            response.set_resultMsg("Error converting response format.");
        }

        return MessageBuilder.withPayload(response).build();
    }

}
