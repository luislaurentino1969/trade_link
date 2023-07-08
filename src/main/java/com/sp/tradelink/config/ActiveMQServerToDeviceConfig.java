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
import javax.jms.JMSException;

@Configuration
@EnableIntegration
public class ActiveMQServerToDeviceConfig {
    @Value("${request.integration}")
    private String integrationDestination;

    @Value("${spring.activemq.broker-url}")
    private String messagingServer;

    @Bean("heartbeat-in-channel")
    public MessageChannel heartbeatInChannel() {
        return new DirectChannel();
    }

    @Bean("heartbeat-out-channel")
    public MessageChannel heartbeatOutChannel() {
        return new DirectChannel();
    }

    @Bean("heartbeat-reply-channel")
    public MessageChannel heartbeatReplyChannel() {
        return new DirectChannel();
    }

    @Bean(name = "connectionFactory")
    public ConnectionFactory serverConnection() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messagingServer);
        return connectionFactory;
    }

    @Bean
    public SimpleMessageListenerContainer queueHeartbeatContainer(@Qualifier("connectionFactory") ConnectionFactory cf) {
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
    public JmsMessageDrivenEndpoint heartbeatEndpoint(SimpleMessageListenerContainer queueHeartbeatContainer,
                                                      ChannelPublishingJmsMessageListener channelListener) {
        JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(queueHeartbeatContainer, channelListener);
        endpoint.setOutputChannel(heartbeatInChannel());
        return endpoint;
    }
}
