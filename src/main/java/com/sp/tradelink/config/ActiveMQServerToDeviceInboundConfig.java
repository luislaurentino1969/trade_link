package com.sp.tradelink.config;

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
public class ActiveMQServerToDeviceInboundConfig {
    @Value("${server.to.device.queue}")
    private String serverToDeviceQueue;

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
    public JmsInboundGateway hbInboundGateway(ConnectionFactory amqConnection) {
        JmsInboundGateway gateway = new JmsInboundGateway(
                serverToDeviceMessageListenerContainer(amqConnection),
                serverToDeviceChannelPublishingMessageListener());
        gateway.setRequestChannel(hbRequestInChannel());

        return gateway;
    }

    @Bean
    public SimpleMessageListenerContainer serverToDeviceMessageListenerContainer(
            ConnectionFactory amqConnection) {
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer();
        container.setConnectionFactory(amqConnection);
        container.setDestination(new ActiveMQQueue(serverToDeviceQueue));
//        container.setPubSubDomain(true);
//        container.setDestinationName(serverToDeviceQueue);
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
