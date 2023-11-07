package com.sp.tradelink.config.integration.amq;

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

@Configuration
@EnableIntegration
public class ActiveMQDeviceToServerInboundConfig {
    @Value("${device.to.server.queue}")
    private String deviceToServerQueue;

    //region InboundGateway
    @Bean("upload-request-in-channel")
    public MessageChannel uploadRequestInChannel() {
        return new DirectChannel();
    }

    @Bean
    public JmsMessageDrivenEndpoint jmsMessageDeviceToServerDrivenEndpoint(
            SimpleMessageListenerContainer deviceToServerMessageListenerContainer) {
        JmsMessageDrivenEndpoint endpoint = new JmsMessageDrivenEndpoint(
                deviceToServerMessageListenerContainer,
                deviceToServerChannelPublishingMessageListener());
        endpoint.setOutputChannel(uploadRequestInChannel());
        return endpoint;
    }

    @Bean
    public SimpleMessageListenerContainer deviceToServerMessageListenerContainer(ConnectionFactory amqConnection) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(amqConnection);
        container.setDestination(new ActiveMQQueue(deviceToServerQueue));
        container.setMessageSelector("Target = 'DeviceToServer'");
        return container;
    }

    @Bean
    public ChannelPublishingJmsMessageListener deviceToServerChannelPublishingMessageListener() {
        ChannelPublishingJmsMessageListener channelPublishingJmsMessageListener =
                new ChannelPublishingJmsMessageListener();
        channelPublishingJmsMessageListener.setExpectReply(false);
        return channelPublishingJmsMessageListener;
    }
    //endregion
}
