package com.sp.tradelink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.tradelink.main.TradeLinkApplication;
import com.sp.tradelink.models.QuantumHBRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TradeLinkApplication.class)
@DirtiesContext
@ActiveProfiles("local")
class TradeLinkApplicationTests {
    //    @Value("${destination.integration}")
//    private String integrationDestination;
//
//    @Autowired
//    private ApplicationContext applicationContext;
//
////    @Autowired
////    private CountDownHandler countDownLatchHandler;
//    @Test
//    public void testIntegration() throws Exception {
//        if (false) {
//            MessageChannel producingChannel = applicationContext.getBean("sendHeartbeatResponseToBrandLinkChannel", MessageChannel.class);
//
//            Map<String, Object> headers = Collections.singletonMap(JmsHeaders.DESTINATION, integrationDestination);
//
//            System.out.println("sending 10 messages");
//            for (int i = 0; i < 10; i++) {
//                GenericMessage<String> message = new GenericMessage<>("Hello Spring Integration JMS " + i + "!", headers);
//                producingChannel.send(message);
//                System.out.println("sent message=" + message);
//            }
//
////            countDownLatchHandler.getLatch().await(10000, TimeUnit.SECONDS);
////            assertTrue(countDownLatchHandler.getLatch().getCount() == 0);
//        } else {
//            assertTrue(true);
//        }
//    }
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testServerToDevice() throws JsonProcessingException {
        MessageChannel outboundOrderRequestChannel =
                applicationContext.getBean("hb-request-out-channel",
                        MessageChannel.class);
        QueueChannel outboundOrderResponseChannel = applicationContext
                .getBean("hb-response-out-channel", QueueChannel.class);
        ObjectMapper mapper = new ObjectMapper();
        QuantumHBRequest hbRequest = mapper.readValue("{\"UserName\":\"A1906003008935\",\"Password\":\"A1906003008935\",\"Token\":\"A1906003008935\",\"TerminalID\":\"A1906003008935\",\"PosID\":\"LuisMultDev\",\"Timeout\":90,\"SerialNum\":\"A1906003008935\"}",
                QuantumHBRequest.class);

        outboundOrderRequestChannel
                .send(MessageBuilder.withPayload(hbRequest.toString()).build());
        Message<?> response = outboundOrderResponseChannel.receive(90000);

        assertThat(response.getPayload())
                .isNotNull();
        ;
    }

    @Test
    public void testDeviceToServer() throws JsonProcessingException {
        MessageChannel outboundOrderRequestChannel =
                applicationContext.getBean("hb-request-out-channel",
                        MessageChannel.class);
        QueueChannel outboundOrderResponseChannel = applicationContext
                .getBean("hb-response-out-channel", QueueChannel.class);
        ObjectMapper mapper = new ObjectMapper();
        QuantumHBRequest hbRequest = mapper.readValue("{\"UserName\":\"A1906003008935\",\"Password\":\"A1906003008935\",\"Token\":\"A1906003008935\",\"TerminalID\":\"A1906003008935\",\"PosID\":\"LuisMultDev\",\"Timeout\":90,\"SerialNum\":\"A1906003008935\"}",
                QuantumHBRequest.class);

        outboundOrderRequestChannel
                .send(MessageBuilder.withPayload(hbRequest.toString()).build());
        Message<?> response = outboundOrderResponseChannel.receive(90000);

        assertThat(response.getPayload())
                .isNotNull();
        ;
    }

}
