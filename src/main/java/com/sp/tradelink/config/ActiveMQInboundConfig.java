package com.sp.tradelink.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.integration.jms.JmsMessageDrivenEndpoint;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.MessageChannel;

import javax.jms.ConnectionFactory;

@Configuration
@EnableIntegration
public class ActiveMQInboundConfig {
    @Value("${request.integration}")
    private String integrationDestination;

    @Value("${spring.activemq.broker-url}")
    private String messagingServer;

    @Bean("heartbeat-in-channel")
    public MessageChannel heartbeatInChannel() {
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
                                             ChannelPublishingJmsMessageListener channelListener) {
        JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(queueContainer, channelListener);
        endpoint.setOutputChannel(heartbeatInChannel());
        return endpoint;
    }

    @Bean("heartbeat-out-channel")
    public MessageChannel heartbeatOutChannel() {
        return new DirectChannel();
    }

    @Bean("heartbeat-reply-channel")
    public MessageChannel heartbeatReplyChannel() {
        return new DirectChannel();
    }

//    @Bean
//    @ServiceActivator(inputChannel = "outputChannel")
//    public MessageHandler jmsInMessageHandler(JmsTemplate jmsTemplate) {
//        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(jmsTemplate);
//        handler.setDestinationName(integrationDestination);
//        return handler;
//    }

}
