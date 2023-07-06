package com.sp.tradelink.utils;

import com.sp.tradelink.models.QuantumHBRequest;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

public class HttpUtils {
    public static String callAPIUsingWebClient(Object info, String server, String endpoint) {
        try {
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
            WebClient client = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient))
                    .baseUrl(server)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            final WebClient.RequestHeadersSpec<?> spec = client.post().uri(endpoint)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(info, QuantumHBRequest.class);

            return spec.retrieve().toEntity(String.class).block().getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
