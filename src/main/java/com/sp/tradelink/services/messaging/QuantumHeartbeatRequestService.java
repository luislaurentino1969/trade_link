package com.sp.tradelink.services.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sp.tradelink.gateways.HttpServerToDeviceGateway;
import com.sp.tradelink.models.QuantumHBResponse;
import com.sp.tradelink.models.QuantumUploadRequest;
import com.sp.tradelink.utils.MessageCreatorHelper;
import com.sp.tradelink.utils.MsgHeaderConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class QuantumHeartbeatRequestService {
    public final Logger logger;

    @Value("${app.server.to.device.url}")
    private String serverToDevice;
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
        MessageBuilder<?> responseMessage = null;
        QuantumHBResponse response = new QuantumHBResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        try {

//                                .baseUrl(apiConfig.getBaseURL())

//            HttpClient httpClient = HttpClient.create()
//                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
//                    .responseTimeout(Duration.ofMillis(5000))
//                    .doOnConnected(conn ->
//                            conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
//                                    .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
//
//            WebClient client = WebClient.builder()
//                    .clientConnector(new ReactorClientHttpConnector(httpClient))
//                    .baseUrl("https://qa.spweblink.com")
//                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                    .defaultHeader(HttpHeaders.HOST, "TradeLink")
//                    .defaultHeader(HttpHeaders.CONNECTION, "Close")
//                    .build();
//            final WebClient.RequestHeadersSpec<?> spec = client.post().uri("/Service/SPTerminal/NewServerToDevice")
//                    .body(Mono.just(request.getPayload().toString()), QuantumHBResponse.class);
//
//            response = spec.retrieve().toEntity(QuantumHBResponse.class).block().getBody();

//            RestClient API example
//            RestClient restClient = RestClient.create();
//            response = restClient.post()
//                    .uri(serverToDevice)
//                    .body(request.getPayload())
//                    .accept(MediaType.APPLICATION_JSON)
//                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(request.getPayload().toString().length()))
//                    .header(HttpHeaders.HOST, "TradeLink")
//                    .header(HttpHeaders.CONNECTION, "Close")
//                    .retrieve()
//                    .body(QuantumHBResponse.class);

//              using http integration
//            var hbResponse = httpServerToDeviceGateway.startHeartbeat(MessageBuilder.withPayload(request.getPayload())
//                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
//                    .setHeader(HttpHeaders.CONTENT_LENGTH, request.getPayload().toString().length())
//                    .setHeader(HttpHeaders.HOST, "TradeLink")
//                    .setHeader(HttpHeaders.CONNECTION, "Close")
//                    .build()).getPayload();

//        ObjectMapper objectMapper = new ObjectMapper();
//            response = objectMapper.convertValue(objectMapper.convertValue(hbResponse, JsonNode.class)
//                    .get("d"), QuantumHBResponse.class);
//            response = objectMapper.convertValue(hbResponse, QuantumHBResponse.class);

            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            clientHttpRequestFactory.setConnectTimeout(5000);

            RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setConnection("Close");

            HttpEntity<Object> request2 = new HttpEntity<>(request.getPayload(), headers);
            response = restTemplate.exchange(serverToDevice, HttpMethod.POST, request2, QuantumHBResponse.class).getBody();

            responseMessage = MessageCreatorHelper.createMessageWithMergedHeaders(response.toString(),
                    request.getHeaders());
            if (request.getHeaders().containsKey(MsgHeaderConstants.SOURCE_HEADER)) {
                if (request.getHeaders().get(MsgHeaderConstants.SOURCE_HEADER).equals("myself") &&
                        request.getHeaders().containsKey(MsgHeaderConstants.BRAND_HEADER) &&
                        response.getResultCode().equals("0")) {
                    logger.debug("Will return heartbeat response to device HTTP DEST.\n{}", response.toString());

                    QuantumUploadRequest deviceResponse = httpBrandService.sendHttpToBrandLink(response.toString(),
                            request.getHeaders().get(MsgHeaderConstants.BRAND_HEADER).toString());
                    ((ObjectNode) response.getExtData()).set("DeviceToServerResponse", objectMapper
                            .readTree((String) service.startUpload(MessageBuilder.withPayload(deviceResponse).build())
                                    .getPayload()));
                    responseMessage = MessageCreatorHelper.createMessageWithMergedHeaders(response.toString(),
                            request.getHeaders());
                }
            }
        } catch (IllegalArgumentException | HttpServerErrorException ex) {
            response.setResultCode("000500");
            response.setResultTxt("Invalid response format.");
            response.setResultMsg(ex.getMessage());
            logger.error("Incorrect message format.", ex);
        } catch (Exception ex) {
            response.setResultCode("-1");
            response.setResultTxt(ex.getMessage());
            response.setResultMsg("Error converting response format.");
            logger.error("Internal error processing heart beat.", ex);
        } finally {
            if (responseMessage == null) {
                responseMessage = MessageCreatorHelper.createMessageWithMergedHeaders(response.toString(),
                        request.getHeaders());
            }
            if (!request.getHeaders().get(MsgHeaderConstants.SOURCE_HEADER).equals("myself")) {
                logger.debug("Will return heartbeat response to device DEST Queue.\n{}", response.toString());
                sendDataToBrandLink.send(responseMessage.build());
            }
        }
        return responseMessage == null ? null : responseMessage.build();
    }

}
