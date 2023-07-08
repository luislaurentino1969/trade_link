package com.sp.tradelink.config;

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
public class ActiveMQDeviceToServerConfig {
    @Value("${response.integration}")
    private String integrationDestination;

    @Value("${spring.activemq.broker-url}")
    private String messagingServer;

    @Bean("device-response-in-channel")
    public MessageChannel uploadResponseInChannel() {
        return new DirectChannel();
    }

    @Bean("device-response-out-channel")
    public MessageChannel uploadResponseOutChannel() {
        return new DirectChannel();
    }

    @Bean("device-response-reply-channel")
    public MessageChannel uploadResponseReplyChannel() {
        return new DirectChannel();
    }

    @Bean
    public SimpleMessageListenerContainer queueDeviceResponseContainer(@Qualifier("connectionFactory") ConnectionFactory cf) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(cf);
        container.setDestinationName(integrationDestination);
        return container;
    }

    @Bean
    public JmsMessageDrivenEndpoint uploadDeviceResponseEndpoint(SimpleMessageListenerContainer queueDeviceResponseContainer,
                                                                 ChannelPublishingJmsMessageListener channelListener) {
        JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(queueDeviceResponseContainer, channelListener);
        endpoint.setOutputChannel(uploadResponseInChannel());
        return endpoint;
    }
}
