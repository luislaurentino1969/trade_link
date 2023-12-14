package com.sp.tradelink.config;

import com.sp.tradelink.common.RestTemplateLogInterceptor;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.ExceptionListener;
import jakarta.jms.JMSException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {
    @Bean
    public Logger getLogger() {
        return LoggerFactory.getLogger("com.sp.tradelink");
    }

    @Value("${spring.activemq.broker-url}")
    private String messagingServer;
    @Value("${log.queue}")
    private String logQueue;
    @Bean("log-out-channel")
    public MessageChannel logOutChannel() {
        return new DirectChannel();
    }
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors
                = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new RestTemplateLogInterceptor().setLogChannel(logOutChannel()));
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
//
//    @Bean
//    public HttpComponentsClientHttpRequestFactory requestFactory() {
//        return new HttpComponentsClientHttpRequestFactory(httpClient());
//    }
//
//    @Bean
//    public CloseableHttpClient httpClient() {
//        return HttpClients.custom()
//                .setConnectionManager(poolingConnectionManager())
//                .build();
//    }
//
//    @Bean
//    public PoolingHttpClientConnectionManager poolingConnectionManager() {
//        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
//        poolingConnectionManager.setMaxTotal(poolSize);
//        poolingConnectionManager.setDefaultMaxPerRoute(poolSize);
//        return poolingConnectionManager;
//    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messagingServer);
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setTrustedPackages(new ArrayList<>(List.of("com.sp.tradelink.models")));
        connectionFactory.setWatchTopicAdvisories(true);
        return connectionFactory;
    }

    @Bean
    public ConnectionFactory amqConnection(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setTargetConnectionFactory(connectionFactory());
        connectionFactory.setSessionCacheSize(100);
        connectionFactory.setCacheConsumers(true);
        connectionFactory.setCacheProducers(true);
        connectionFactory.setReconnectOnException(true);
        connectionFactory.setExceptionListener(e -> getLogger().error("Broker cached connection error.", e));
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(amqConnection());
    }
}
