package com.sp.tradelink.config.integration.http;

import com.sp.tradelink.common.HttpResponseException;
import com.sp.tradelink.models.QuantumHBResponse;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.BeforeAdvice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.handler.advice.AbstractHandleMessageAdvice;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.AbstractList;
import java.util.Collections;

@Configuration
public class HeartbeatHttpOutboundGatewayConfig {
    @Value("${app.server.to.device.url}")
    private String serverToDevice;

    @Value("${app.device.to.server.url}")
    private String deviceToServer;

    @Value("${app.upload.raw.message.url}")
    private String uploadBrandRawMessage;

    @Bean public MessageChannel serverToDeviceChannel() {
        return new DirectChannel();
    }
    @Bean public MessageChannel serverToDeviceReplyChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "serverToDeviceChannel")
    public MessageHandler postServerToDeviceService() {
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler(serverToDevice);
        handler.setHttpMethod(HttpMethod.POST);
        handler.setExpectReply(true);
        handler.setExpectedResponseType(Object.class);
        handler.setAsync(true);
        handler.onError(new HttpResponseException());
        handler.setExpectedResponseType(QuantumHBResponse.class);
        handler.setLoggingEnabled(true);
        return handler;
    }

    @Bean public MessageChannel deviceToServerChannel() {
        return new DirectChannel();
    }
    @Bean public MessageChannel deviceToServerReplyChannel() {
        return new DirectChannel();
    }
    @Bean
    @ServiceActivator(inputChannel = "deviceToServerChannel")
    public MessageHandler postDeviceToServerService() {
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler(deviceToServer);
        handler.setHttpMethod(HttpMethod.POST);
        handler.setExpectReply(true);
        handler.setExpectedResponseType(Object.class);
        handler.setLoggingEnabled(true);
        return handler;
    }
    @Bean public MessageChannel uploadRawMessageChannel() {
        return new DirectChannel();
    }
    @Bean public MessageChannel uploadRawMessageReplyChannel() {
        return new DirectChannel();
    }
    @Bean
    @ServiceActivator(inputChannel = "uploadRawMessageChannel")
    public MessageHandler postBrandRawMessageService() {
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler(uploadBrandRawMessage);
        handler.setHttpMethod(HttpMethod.POST);
        handler.setExpectReply(true);
        handler.setExpectedResponseType(Object.class);
        handler.setAsync(true);
        handler.setLoggingEnabled(true);
        return handler;
    }
}