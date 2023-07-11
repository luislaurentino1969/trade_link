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
public class ActiveMQServerToDeviceInboundConfig {
    @Value("${server.to.device.queue}")
    private String serverToDeviceQueue;
    @Value("${reply.integration}")
    private String replyQueue;

    @Value("${spring.activemq.broker-url}")
    private String messagingServer;

    //region InboundGateway
    @Bean("hb-request-in-channel")
    public MessageChannel hbRequestInChannel() {
        return new DirectChannel();
    }
    @Bean("hb-response-in-channel")
    public MessageChannel hbResponseInChannel() {
        return new DirectChannel();
    }
    @Bean
    public ConnectionFactory hbInboundConnection() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messagingServer);
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        return connectionFactory;
    }
    @Bean
    public JmsInboundGateway hbInboundGateway() {
        JmsInboundGateway gateway = new JmsInboundGateway(
                serverToDeviceMessageListenerContainer(hbInboundConnection()),
                serverToDeviceChannelPublishingMessageListener());
        gateway.setRequestChannel(hbRequestInChannel());

        return gateway;
    }

    @Bean
    public SimpleMessageListenerContainer serverToDeviceMessageListenerContainer(
            ConnectionFactory hbInboundConnection) {
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer();
        container.setConnectionFactory(hbInboundConnection);
        container.setDestinationName(serverToDeviceQueue);
        return container;
    }

    @Bean
    public ChannelPublishingJmsMessageListener serverToDeviceChannelPublishingMessageListener() {
        ChannelPublishingJmsMessageListener channelPublishingJmsMessageListener =
                new ChannelPublishingJmsMessageListener();
        channelPublishingJmsMessageListener.setExpectReply(true);

        return channelPublishingJmsMessageListener;
    }
    //endregion
}
