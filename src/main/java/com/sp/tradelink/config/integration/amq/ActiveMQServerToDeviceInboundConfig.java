package com.sp.tradelink.config.integration.amq;

import org.apache.activemq.ActiveMQConnectionFactory;
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

import javax.jms.ConnectionFactory;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableIntegration
public class ActiveMQServerToDeviceInboundConfig {
    @Value("${server.to.device.queue}")
    private String serverToDeviceQueue;

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
//    public ConnectionFactory stdInboundConnection() {
//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messagingServer);
//        connectionFactory.setUserName(brokerUser);
//        connectionFactory.setPassword(brokerPwd);
//        connectionFactory.setTrustedPackages(new ArrayList<>(List.of("com.sp.equinox.link")));
//
//        return connectionFactory;
//    }

    //region InboundGateway
    @Bean("hb-request-in-channel")
    public MessageChannel hbRequestInChannel() {
        return new DirectChannel();
    }

    @Bean
    public JmsMessageDrivenEndpoint jmsMessageServerToDeviceDrivenEndpoint(
            SimpleMessageListenerContainer serverToDeviceMessageListenerContainer) {
        JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(
                serverToDeviceMessageListenerContainer,
                serverToDeviceChannelPublishingMessageListener());
        endpoint.setOutputChannel(hbRequestInChannel());
        return endpoint;
    }

    @Bean
    public SimpleMessageListenerContainer serverToDeviceMessageListenerContainer(ConnectionFactory amqConnection) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(amqConnection);
        container.setDestination(new ActiveMQQueue(serverToDeviceQueue));
        container.setMessageSelector("Target = 'ServerToDevice'");
        return container;
    }

    @Bean
    public ChannelPublishingJmsMessageListener serverToDeviceChannelPublishingMessageListener() {
        ChannelPublishingJmsMessageListener channelPublishingJmsMessageListener =
                new ChannelPublishingJmsMessageListener();
        channelPublishingJmsMessageListener.setExpectReply(false);
        return channelPublishingJmsMessageListener;
    }
    //endregion
}
