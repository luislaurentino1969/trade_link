package com.sp.tradelink.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.jms.JmsOutboundGateway;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.jms.ConnectionFactory;

@Configuration
public class ActiveMQDeviceToServerOutboundConfig {
    @Value("${device.to.server.queue}")
    private String deviceToServerQueue;
    @Value("${reply.queue}")
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
//
//    @Bean
//    @ServiceActivator(inputChannel = "upload-request-out-channel")
//    public JmsOutboundGateway uploadOutboundGateway(ConnectionFactory amqConnection) {
//        JmsOutboundGateway gateway = new JmsOutboundGateway();
//        gateway.setConnectionFactory(amqConnection);
//        gateway.setRequestDestinationName(deviceToServerQueue);
//        gateway.setReplyDestinationName(replyQueue);
//        gateway.setReplyChannel( uploadResponseOutChannel());
//
//        return gateway;
//    }

    @Bean
    @ServiceActivator(inputChannel = "upload-request-out-channel")
    public MessageHandler jmsMessageHandler(JmsTemplate jmsTemplate) {
//        jmsTemplate.setConnectionFactory(amqConnection);
        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(jmsTemplate);
        handler.setDestinationName(replyQueue);
        return handler;
    }
    //endregion
}
