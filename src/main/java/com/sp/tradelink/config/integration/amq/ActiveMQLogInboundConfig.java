package com.sp.tradelink.config.integration.amq;

import jakarta.jms.ConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.integration.jms.JmsMessageDrivenEndpoint;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class ActiveMQLogInboundConfig {
    @Value("${log.queue}")
    private String logQueue;
//
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
//    public ConnectionFactory logInboundConnection() {
//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messagingServer);
//        connectionFactory.setUserName(brokerUser);
//        connectionFactory.setPassword(brokerPwd);
//        connectionFactory.setTrustedPackages(new ArrayList<>(List.of("com.sp.equinox.link")));
//
//        return connectionFactory;
//    }

    //region InboundGateway
    @Bean("log-in-channel")
    public MessageChannel logInChannel() {
        return new DirectChannel();
    }
    @Bean
    public JmsMessageDrivenEndpoint jmsLogMessageDrivenEndpoint(SimpleMessageListenerContainer logMessageListenerContainer) {
        JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(
                logMessageListenerContainer,
                logPublishingJmsMessageListener());
        endpoint.setOutputChannel(logInChannel());
        return endpoint;
    }

    @Bean
    public SimpleMessageListenerContainer logMessageListenerContainer(ConnectionFactory amqConnection) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(amqConnection);
        container.setDestination(new ActiveMQQueue(logQueue));
        return container;
    }

    @Bean
    public ChannelPublishingJmsMessageListener logPublishingJmsMessageListener() {
        ChannelPublishingJmsMessageListener listener = new ChannelPublishingJmsMessageListener();
        listener.setExpectReply(false);
        listener.setReplyDeliveryPersistent(true);
        return listener;
    }
    //endregion
}
