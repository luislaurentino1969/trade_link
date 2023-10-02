package com.sp.tradelink.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sp.tradelink.models.QuantumUploadRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BrandHttpService {
    @Value("${spring.profiles.active}")
    private String profile;
    public QuantumUploadRequest sendHttpToBrandLink(String body, String brand) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        ResponseEntity<QuantumUploadRequest> response = restTemplate.exchange(
                (profile.equalsIgnoreCase("local") ? "http://localhost:8084" :
                        "http://108.215.108.45:5080") + "/" + brand + "-link/pos_request", HttpMethod.POST,
                new HttpEntity<Object>(body, headers),
                QuantumUploadRequest.class);
        return response.getBody();
    }
    public ObjectNode managePreviousHB(String serialNumber, String action, String brand) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        ResponseEntity<ObjectNode> response = restTemplate.exchange(
                (profile.equalsIgnoreCase("local") ? "http://localhost:8084" :
                        "http://108.215.108.45:5080") + "/" + brand + "-link/cancel_heartbeat", HttpMethod.POST,
                new HttpEntity<Object>(new ObjectMapper().createObjectNode().put("serialNumber",serialNumber)
                        .put("action",action), headers),
                ObjectNode.class);
        return response.getBody();
    }
}
