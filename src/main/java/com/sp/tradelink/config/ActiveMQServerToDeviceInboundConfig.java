package com.sp.tradelink.config;

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
public class ActiveMQServerToDeviceInboundConfig {
    @Value("${server.to.device.queue}")
    private String serverToDeviceQueue;

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
