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
public class ActiveMQServerToDeviceOutboundConfig {
    @Value("${server.to.device.queue}")
    private String serverToDeviceQueue;
    @Value("${reply.integration}")
    private String replyQueue;

    @Value("${spring.activemq.broker-url}")
    private String messagingServer;

    //region OutboundGateway
    @Bean("hb-request-out-channel")
    public MessageChannel hbRequestOutChannel() {
        return new DirectChannel();
    }
    @Bean("hb-response-out-channel")
    public MessageChannel hbResponseOutChannel() {
        return new QueueChannel();
    }

    @Bean
    public ConnectionFactory hbOutboundConnection() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messagingServer);
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setTrustedPackages(new ArrayList<>(List.of("com.sp.tradelink.models")));
        return connectionFactory;
    }

    @Bean
    @ServiceActivator(inputChannel = "hb-request-out-channel")
    public JmsOutboundGateway hbOutboundGateway() {
        JmsOutboundGateway gateway = new JmsOutboundGateway();
        gateway.setConnectionFactory(hbOutboundConnection());
        gateway.setRequestDestinationName(serverToDeviceQueue);
        gateway.setReplyDestinationName(replyQueue);
        gateway.setReplyChannel( hbResponseOutChannel());

        return gateway;
    }
    //endregion
}
