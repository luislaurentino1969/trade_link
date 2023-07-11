package com.sp.tradelink.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.jms.ChannelPublishingJmsMessageListener;
import org.springframework.integration.jms.JmsInboundGateway;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.MessageChannel;

import javax.jms.ConnectionFactory;

@Configuration
@EnableIntegration
public class ActiveMQDeviceToServerInboundConfig {
    @Value("${device.to.server.queue}")
    private String deviceToServerQueue;

    @Value("${spring.activemq.broker-url}")
    private String messagingServer;

    //region InboundGateway
    @Bean("upload-request-in-channel")
    public MessageChannel uploadRequestInChannel() {
        return new DirectChannel();
    }
    @Bean("upload-response-in-channel")
    public MessageChannel uploadResponseInChannel() {
        return new DirectChannel();
    }
    @Bean
    public ConnectionFactory uploadInboundConnection() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messagingServer);
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        return connectionFactory;
    }
    @Bean
    public JmsInboundGateway uploadInboundGateway() {
        JmsInboundGateway gateway = new JmsInboundGateway(
                deviceToServerMessageListenerContainer(uploadInboundConnection()),
                deviceToServerChannelPublishingMessageListener());
        gateway.setRequestChannel(uploadRequestInChannel());

        return gateway;
    }

    @Bean
    public SimpleMessageListenerContainer deviceToServerMessageListenerContainer(
            ConnectionFactory uploadInboundConnection) {
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer();
        container.setConnectionFactory(uploadInboundConnection);
        container.setDestinationName(deviceToServerQueue);
        return container;
    }

    @Bean
    public ChannelPublishingJmsMessageListener deviceToServerChannelPublishingMessageListener() {
        ChannelPublishingJmsMessageListener channelPublishingJmsMessageListener =
                new ChannelPublishingJmsMessageListener();
        channelPublishingJmsMessageListener.setExpectReply(true);

        return channelPublishingJmsMessageListener;
    }
    //endregion
}
