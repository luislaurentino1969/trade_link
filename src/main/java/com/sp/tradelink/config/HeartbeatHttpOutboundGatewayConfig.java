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
    private String serverToDevice;

    @Value("${app.device.to.server.url}")
    private String deviceToServer;

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
//        handler.setOutputChannelName("serverToDeviceReplyChannel");
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
//        handler.setOutputChannelName("deviceToServerReplyChannel");
        return handler;
    }
}
