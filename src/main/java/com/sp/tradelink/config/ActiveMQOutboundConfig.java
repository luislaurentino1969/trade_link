package com.sp.tradelink.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

//@Configuration
public class ActiveMQOutboundConfig {
//    @Value("${destination.integration}")
//    private String integrationDestination;
//
//    @Bean
//    public MessageChannel sendHeartbeatToTradeLinkChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public MessageChannel receiveHeartbeatFromTradeLinkChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    @Transformer(inputChannel = "receiveHeartbeatFromTradeLinkChannel", outputChannel = "sendHeartbeatToTradeLinkChannel")
//    public ObjectToJsonTransformer objectToJsonTransformer() {
//        return new ObjectToJsonTransformer();
//    }
//
//    @Bean(name="sendHeartbeatRequest")
//    @ServiceActivator(inputChannel = "sendHeartbeatToTradeLinkChannel")
//    public MessageHandler jmsMessageHandler(JmsTemplate jmsTemplate) {
//        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(jmsTemplate);
//        handler.setDestinationName(integrationDestination);
//        return handler;
//    }

//    @MessagingGateway(defaultRequestChannel = "sendHeartbeatToTradeLinkChannel")
//    public interface HeartbeatRequestGateway {
//        @Gateway
//        void publishHeartbeatRequest(Object heartbeatResponse);
//    }
}
