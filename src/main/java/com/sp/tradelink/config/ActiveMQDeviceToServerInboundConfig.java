package com.sp.tradelink.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
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
    public JmsInboundGateway uploadInboundGateway(ConnectionFactory amqConnection) {
        JmsInboundGateway gateway = new JmsInboundGateway(
                deviceToServerMessageListenerContainer(amqConnection),
                deviceToServerChannelPublishingMessageListener());
        gateway.setRequestChannel(uploadRequestInChannel());

        return gateway;
    }

    @Bean
    public SimpleMessageListenerContainer deviceToServerMessageListenerContainer(
            ConnectionFactory amqConnection) {
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer();
        container.setConnectionFactory(amqConnection);
//        container.setPubSubDomain(true);
//        container.setDestinationName(deviceToServerQueue);
        container.setDestination(new ActiveMQQueue(deviceToServerQueue));
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
