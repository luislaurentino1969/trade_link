package com.sp.tradelink.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.util.concurrent.CountDownLatch;

public class CountDownHandler implements MessageHandler {
        private static final Logger LOGGER = LoggerFactory.getLogger(CountDownHandler.class);

        private CountDownLatch latch = new CountDownLatch(10);

        public CountDownLatch getLatch() {
            return latch;
        }

        @Override
        public void handleMessage(Message<?> message) {
            LOGGER.info("received message='{}'", message);
            latch.countDown();
        }
}
