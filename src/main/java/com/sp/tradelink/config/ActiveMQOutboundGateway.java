package com.sp.tradelink.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.jms.JmsOutboundGateway;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.jms.ConnectionFactory;

@Configuration
@EnableIntegration
public class ActiveMQOutboundGateway {
//    @Value("${request.integration}")
//    private String requestQueueName;
//    @Value("${request.integration}")
//    private String responseQueueName;
//    @Value("${request.integration}")
//    private String replyQueueName;

    private String integrationDestination = "request.q";

    @Value("${spring.activemq.broker-url}")
    private String messagingServer;

    @Bean
    public MessageChannel reservationChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel publishReservationToActiveMQ() {
        return new DirectChannel();
    }

//    @Bean
//    @Transformer(inputChannel = "reservationChannel", outputChannel = "publishReservationToActiveMQ")
//    public ObjectToJsonTransformer objectToJsonTransformer() {
//        return new ObjectToJsonTransformer();
//    }

    @Bean
    @ServiceActivator(inputChannel = "publishReservationToActiveMQ")
    public MessageHandler jmsOutMessageHandler(JmsTemplate jmsTemplate) {
        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(jmsTemplate);
        handler.setDestinationName(integrationDestination);
        return handler;
    }

    @MessagingGateway(defaultRequestChannel = "reservationChannel")
    public interface BarReservationGateway {

        void publishReservation(String userId);
    }
}
