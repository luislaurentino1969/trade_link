package com.sp.tradelink.config;

import com.sp.tradelink.common.RestTemplateLogInterceptor;
import jakarta.annotation.PreDestroy;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1000);
        threadPoolTaskScheduler.setThreadNamePrefix("TL_Thread");
        return threadPoolTaskScheduler;
    }

    @PreDestroy
    private void terminateThreads() {
        threadPoolTaskScheduler().shutdown();
    }

    @Bean
    public RestTemplate restTemplate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

//        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
//        javax.net.ssl.SSLContext sslContext = SSLContexts.custom()
//                .loadTrustMaterial(null, acceptingTrustStrategy).build();
//        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
//        CloseableHttpClient httpClient = HttpClients.custom().build();
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setHttpClient(httpClient);
//        requestFactory.setConnectionRequestTimeout(Duration.ofSeconds(94));
//        requestFactory.setConnectTimeout(Duration.ofSeconds(94));
//
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2 * 1000);
        requestFactory.setReadTimeout(95 * 1000);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
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
    public ConnectionFactory amqConnection() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setTargetConnectionFactory(connectionFactory());
//        connectionFactory.setSessionCacheSize(2000);
        connectionFactory.setCacheConsumers(false);
        connectionFactory.setCacheProducers(false);
        connectionFactory.setReconnectOnException(true);
        connectionFactory.setExceptionListener(e -> getLogger().error("Broker cached connection error.", e));
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(amqConnection());
    }
}
