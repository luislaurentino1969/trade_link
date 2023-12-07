package com.sp.tradelink.main;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Luis Laurentino
 * @version %I%, %G%
 * <p>
 * Application properties listener class
 * to set up the properties values
 * <p>
 * add the properties into the source of the application.properties file, check the profile to add the values dynamically
 * <p>
 * Note: do not use to set the logging.config property (Log4j), this property must be set using each
 * application-profile.properties file
 */
@Configuration
public class AppPropertiesListener extends PropertySourcesPlaceholderConfigurer {
    @Override
    public PropertySources getAppliedPropertySources() throws IllegalStateException {

        MutablePropertySources appliedPropertySources = (MutablePropertySources) super.getAppliedPropertySources();

        PropertySource<?> environmentPropertySource = appliedPropertySources.get("environmentProperties");

        if (Objects.nonNull(environmentPropertySource)) {
            addCustomPropertySourceToIgnoreRefresh(environmentPropertySource);
        }
        return appliedPropertySources;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        super.postProcessBeanFactory(beanFactory);
    }

    private void addCustomPropertySourceToIgnoreRefresh(PropertySource<?> environmentPropertySource) {
        Map<String, Object> customPropertyMap = new HashMap<>();

        ConfigurableEnvironment configurableEnvironment =
                (ConfigurableEnvironment) environmentPropertySource.getSource();

        customPropertyMap.put("server.servlet.context-path", "/trade-link");

        List<String> profiles = Arrays.asList(configurableEnvironment.getActiveProfiles());

        String amqServer = "108.215.108.45";
        String dbServer = "108.215.108.45";
        String quantumInstance = "qa";
        String dbName = quantumInstance;
        String quantumCloudURL = "";

        if (profiles.contains("local") || profiles.contains("dev")) {
            amqServer="localhost";
            customPropertyMap.put("server.port", "8081");
            dbName = "dev";
            quantumInstance = "qa";
            quantumCloudURL = "https://" + quantumInstance + ".spweblink.com/Service/SPTerminal/";
        } else if (profiles.contains("qa")) {
            customPropertyMap.put("server.port", "0");
            quantumCloudURL = "https://" + quantumInstance + ".spweblink.com/Service/SPTerminal/";
        } else if (profiles.contains("demo")) {
            customPropertyMap.put("server.port", "0");
            quantumInstance = "demo";
            quantumCloudURL = "https://" + quantumInstance + ".spweblink.com/Service/SPTerminalService.asmx/";
        } else if (profiles.contains("prod")) {
            customPropertyMap.put("server.port", "0");
            quantumInstance = "secure";
            quantumCloudURL = "https://" + quantumInstance + ".spweblink.com/Service/SPTerminalService.asmx/";
        } else if (profiles.contains("server")) {
            customPropertyMap.put("server.port", "0");
            amqServer = "108.215.108.45";
            dbServer = "108.215.108.45";
            quantumCloudURL = "https://" + quantumInstance + ".spweblink.com/Service/SPTerminal/";
        }


        customPropertyMap.put("spring.activemq.broker-url", "tcp://"+ amqServer + ":61616");
        customPropertyMap.put("app.server.to.device.url", quantumCloudURL + "NewServerToDevice");
        customPropertyMap.put("app.device.to.server.url", quantumCloudURL + "NewDeviceToServer");
        customPropertyMap.put("app.upload.raw.message.url", quantumCloudURL + "UploadRawResponse");

        customPropertyMap.put("spring.datasource.url", "jdbc:postgresql://" + dbServer + ":8088/trade_link");
        customPropertyMap.put("spring.datasource.username", "trade_link_dev");
        customPropertyMap.put("spring.datasource.password", "$2a$16$WOCTaXGVtJRnbKsmwRtB7.GY6S2DK3P89GVrVEhn6UanHW1ZDFeGO");
        customPropertyMap.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
        customPropertyMap.put("spring.jpa.hibernate.ddl-auto", "none");

        customPropertyMap.put("app.brand.url","http://" + amqServer + "/{{brand_name}}/test-equinox");

        customPropertyMap.put("spring.task.execution.pool.core-size", "7");
        customPropertyMap.put("spring.task.execution.pool.max-size", "100");
        customPropertyMap.put("spring.task.execution.pool.queue-capacity", "11");
        customPropertyMap.put("spring.task.execution.thread-name-prefix", "TradeLinkThread-");
        customPropertyMap.put("spring.task.scheduling.thread-name-prefix", "TradeLinkThread-");

        customPropertyMap.put("spring.activemq.user-name", "admin");
        customPropertyMap.put("spring.activemq.user-pwd", "admin");
        customPropertyMap.put("server.to.device.queue", "ServerToDevice.q");
        customPropertyMap.put("device.to.server.queue", "DeviceToServer.q");
        customPropertyMap.put("reply.queue", "BrandLink.q");
        customPropertyMap.put("log.queue", "TradeLinkLog.q");
        customPropertyMap.put("spring.activemq.packages.trust-all", "true");

        customPropertyMap.put("server.http2.enabled", "true");

        // post fetching the configurable environment, adding my hash map as custom map property source
        configurableEnvironment.getPropertySources()
                .addFirst(new MapPropertySource("customPropertySource", customPropertyMap));
    }

}
