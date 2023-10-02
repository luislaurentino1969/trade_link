package com.sp.tradelink.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.jms.ConnectionFactory;

@Configuration
public class ActiveMQServerToDeviceOutboundConfig {
    @Value("${server.to.device.queue}")
    private String serverToDeviceQueue;
    @Value("${reply.queue}")
    private String replyQueue;

    //region OutboundGateway
    @Bean("hb-request-out-channel")
    public MessageChannel hbRequestOutChannel() {
        return new DirectChannel();
    }

    @Bean("hb-response-out-channel")
    public MessageChannel hbResponseOutChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "hb-response-out-channel")
    public MessageHandler jmsMessageHandler(JmsTemplate jmsTemplate) {
        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(jmsTemplate);
        handler.setDestinationName(replyQueue);
        return handler;
    }
    //endregion
}
