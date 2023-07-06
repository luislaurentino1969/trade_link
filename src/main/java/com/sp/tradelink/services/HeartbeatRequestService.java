package com.sp.tradelink.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sp.tradelink.models.QuantumHBRequest;
import com.sp.tradelink.models.QuantumHBResponse;
import com.sp.tradelink.utils.HttpUtils;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.http.HttpClient;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class HeartbeatRequestService {
    private static final Logger logger = LoggerFactory.getLogger(HeartbeatRequestService.class);
    @Value("${app.server.url}")
    private String SERVER;

    @Value("${app.server.endpoint}")
    private String ENDPOINT;

    @ServiceActivator(inputChannel = "heartbeat-in-channel", outputChannel = "heartbeat-out-channel")
    public QuantumHBRequest publishHeartbeatRequest(QuantumHBRequest heartbeat) {
        logger.info("Will start quantum heartbeat process.");
        return heartbeat;
    }

    @ServiceActivator(inputChannel = "heartbeat-out-channel", outputChannel = "heartbeat-reply-channel")
    public QuantumHBResponse processHeartbeatRequest(QuantumHBRequest heartbeat) throws JsonProcessingException {
        logger.info("Processing the quantum heartbeat process.");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestNode = objectMapper.createObjectNode();
        requestNode.put("d",objectMapper.readTree(heartbeat.toString()));
//        var apiResponse = HttpUtils.callAPIUsingWebClient(heartbeat, SERVER, ENDPOINT);
        var apiResponse = callAPIUsingWebClient(heartbeat, SERVER, ENDPOINT);
        QuantumHBResponse response = objectMapper.readValue(apiResponse,
                QuantumHBResponse.class);
        return response;
    }

    @ServiceActivator(inputChannel = "heartbeat-reply-channel")
    public void sendHeartbeatResponse(Message<QuantumHBResponse> heartbeat) throws JsonProcessingException {
        logger.info("Will send the heartbeat response back to caller.");
        MessageChannel replyChannel = (MessageChannel) heartbeat.getHeaders().getReplyChannel();
        replyChannel.send(heartbeat);
    }

    private String callAPIUsingWebClient(QuantumHBRequest info, String server, String endpoint) {
        try {

            WebClient webClient = WebClient.create(server);

            Mono<Object> createdEmployee = webClient.post()
                    .uri(endpoint)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(info), QuantumHBRequest.class)
                    .exchangeToMono(response -> {

                        if (response.statusCode().equals(HttpStatus.OK)) {
                            return response.bodyToMono(QuantumHBResponse.class);
                        } else if (response.statusCode().is4xxClientError()) {
                            return Mono.just("Error response");
                        } else {
                            return response.createException().flatMap(Mono::error);
                        }
                    });
//
//            SslContext sslContext = SslContextBuilder
//                    .forClient()
//                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
//                    .build();
//            HttpClient httpClient = HttpClient.newHttpClient();
//            WebClient client = WebClient.builder()
//                    .baseUrl(server).build();
//
////            WebClient client = WebClient.builder().exchangeStrategies(ExchangeStrategies.builder().codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build())
////                    .baseUrl(server).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
//
//            final WebClient.RequestHeadersSpec<?> spec = client.post().uri(endpoint)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .body(Mono.just(info), QuantumHBRequest.class);
//
//            return Objects.requireNonNull(spec.retrieve().toEntity(String.class).block()).getBody();
            createdEmployee.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Objects.requireNonNull(Mono.empty().block()).toString();
        }
        return new QuantumHBResponse().toString();
    }

    private String callAPIUsingTemplate(QuantumHBRequest info, String server, String endpoint) {
        RestTemplate quantum = new RestTemplate();
        String serverEndpoint = server + endpoint.substring(1);

        HttpHeaders headers = new HttpHeaders();
        List<MediaType> accept =
                Collections.singletonList(MediaType.asMediaType(MediaType.APPLICATION_JSON));
        headers.setAccept(accept);
        headers.setContentType(MediaType.asMediaType(MimeType.valueOf(MediaType.APPLICATION_JSON_VALUE)));
        HttpEntity<String> entity = new HttpEntity<>(info.toString(), headers);
        return quantum.exchange(serverEndpoint, HttpMethod.POST, entity, String.class).getBody();
    }
}
