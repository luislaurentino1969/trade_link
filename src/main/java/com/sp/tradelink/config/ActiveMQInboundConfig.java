package com.sp.tradelink.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.integration.jms.JmsInboundGateway;
import org.springframework.integration.jms.JmsMessageDrivenEndpoint;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.jms.ConnectionFactory;

@Configuration
@EnableIntegration
public class ActiveMQInboundConfig {
//    @Value("${destination.integration}")
    private String integrationDestination = "request.q";

//    @Value("${spring.activemq.broker-url}")
    private String messagingServer;

    @Bean
    public MessageChannel publishingChannel() {
        return new DirectChannel();
    }

    @Bean(name = "connectionFactory")
    public ConnectionFactory serverConnection() {
        return new ActiveMQConnectionFactory(messagingServer);
    }

    @Bean
    public SimpleMessageListenerContainer queueContainer(@Qualifier("connectionFactory") ConnectionFactory cf) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(cf);
        container.setDestinationName(integrationDestination);
        return container;
    }

    @Bean
    public ChannelPublishingJmsMessageListener channelListener() {
        return new ChannelPublishingJmsMessageListener();
    }

    @Bean
    public JmsMessageDrivenEndpoint endpoint(SimpleMessageListenerContainer queueContainer,
                                             ChannelPublishingJmsMessageListener channelListener,
                                             MessageChannel publishingChannel) {
        JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(queueContainer, channelListener);
        endpoint.setOutputChannel(publishingChannel);
        return endpoint;
    }

    @Bean
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "outputChannel")
    public MessageHandler jmsInMessageHandler(JmsTemplate jmsTemplate) {
        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(jmsTemplate);
        handler.setDestinationName(integrationDestination);
        return handler;
    }

    @MessagingGateway(defaultReplyChannel = "outputChannel")
    public interface HeartbeatResponseGateway {
        void publishHeartbeatResponse(Object heartbeatResponse);
    }
}
