package com.sp.tradelink.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class HeartbeatHttpOutboundGatewayConfig {
    @Value("${app.server.to.device.url}")
    private String SERVERTODEVICE;

    @Value("${app.device.to.server.url}")
    private String DEVICETOSERVER;

    @Bean public MessageChannel serverToDeviceChannel() {
        return new DirectChannel();
    }
    @Bean public MessageChannel serverToDeviceReplyChannel() {
        return new DirectChannel();
    }
    @Bean
    @ServiceActivator(inputChannel = "serverToDeviceChannel")
    public MessageHandler postToService() {
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler(SERVERTODEVICE);
        handler.setHttpMethod(HttpMethod.POST);
        handler.setExpectReply(true);
        handler.setExpectedResponseType(Object.class);
        handler.setOutputChannelName("serverToDeviceReplyChannel");
        return handler;
    }
}
