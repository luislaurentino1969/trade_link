package com.sp.tradelink.config.integration.amq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.jms.ConnectionFactory;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ActiveMQDeviceToServerOutboundConfig {
    @Value("${device.to.server.queue}")
    private String deviceToServerQueue;
    @Value("${reply.queue}")
    private String replyQueue;

//    @Value("${spring.activemq.broker-url}")
//    private String messagingServer;
//
//    @Value("${spring.activemq.user-name}")
//    private String brokerUser;
//
//    @Value("${spring.activemq.user-pwd}")
//    private String brokerPwd;
//
//    @Bean
//    public ConnectionFactory dtsOutboundConnection() {
//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messagingServer);
//        connectionFactory.setUserName(brokerUser);
//        connectionFactory.setPassword(brokerPwd);
//        connectionFactory.setTrustedPackages(new ArrayList<>(List.of("com.sp.equinox.link")));
//
//        return connectionFactory;
//    }


    //region OutboundGateway
    @Bean("upload-request-out-channel")
    public MessageChannel uploadRequestOutChannel() {
        return new DirectChannel();
    }
    @Bean("upload-response-out-channel")
    public MessageChannel uploadResponseOutChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "upload-response-out-channel")
    public MessageHandler jmsMessageHandler(JmsTemplate jmsTemplate) {
        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(jmsTemplate);
        handler.setDestinationName(replyQueue);
        return handler;
    }
    //endregion
}
