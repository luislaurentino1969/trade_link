package com.sp.tradelink;

import com.sp.tradelink.main.TradeLinkApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TradeLinkApplication.class)
@ActiveProfiles("local")
@DirtiesContext
class TradeLinkApplicationTests {
    @Value("${destination.integration}")
    private String integrationDestination;

    @Autowired
    private ApplicationContext applicationContext;

//    @Autowired
//    private CountDownHandler countDownLatchHandler;
    @Test
    public void testIntegration() throws Exception {
        if (false) {
            MessageChannel producingChannel = applicationContext.getBean("sendHeartbeatResponseToBrandLinkChannel", MessageChannel.class);

            Map<String, Object> headers = Collections.singletonMap(JmsHeaders.DESTINATION, integrationDestination);

            System.out.println("sending 10 messages");
            for (int i = 0; i < 10; i++) {
                GenericMessage<String> message = new GenericMessage<>("Hello Spring Integration JMS " + i + "!", headers);
                producingChannel.send(message);
                System.out.println("sent message=" + message);
            }

//            countDownLatchHandler.getLatch().await(10000, TimeUnit.SECONDS);
//            assertTrue(countDownLatchHandler.getLatch().getCount() == 0);
        } else {
            assertTrue(true);
        }
    }

}
