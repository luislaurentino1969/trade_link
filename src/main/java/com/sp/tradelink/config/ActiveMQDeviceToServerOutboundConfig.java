package com.sp.tradelink.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.jms.JmsOutboundGateway;
import org.springframework.messaging.MessageChannel;

import javax.jms.ConnectionFactory;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ActiveMQDeviceToServerOutboundConfig {
    @Value("${device.to.server.queue}")
    private String deviceToServerQueue;
    @Value("${reply.integration}")
    private String replyQueue;

    @Value("${spring.activemq.broker-url}")
    private String messagingServer;

    //region OutboundGateway
    @Bean("upload-request-out-channel")
    public MessageChannel uploadRequestOutChannel() {
        return new DirectChannel();
    }
    @Bean("upload-response-out-channel")
    public MessageChannel uploadResponseOutChannel() {
        return new QueueChannel();
    }

    @Bean
    public ConnectionFactory uploadOutboundConnection() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messagingServer);
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setTrustedPackages(new ArrayList<>(List.of("com.sp.tradelink.models")));
        return connectionFactory;
    }

    @Bean
    @ServiceActivator(inputChannel = "upload-request-out-channel")
    public JmsOutboundGateway uploadOutboundGateway() {
        JmsOutboundGateway gateway = new JmsOutboundGateway();
        gateway.setConnectionFactory(uploadOutboundConnection());
        gateway.setRequestDestinationName(deviceToServerQueue);
        gateway.setReplyDestinationName(replyQueue);
        gateway.setReplyChannel( uploadResponseOutChannel());

        return gateway;
    }
    //endregion
}
