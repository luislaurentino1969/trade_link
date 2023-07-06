package com.sp.tradelink.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
public class TestListener {
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @ServiceActivator(inputChannel = "publishingChannel")
    public void handleMessage(String message) {
        logger.info("received message {}", message);
    }
}
