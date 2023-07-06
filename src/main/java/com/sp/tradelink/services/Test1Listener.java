package com.sp.tradelink.services;

import com.sp.tradelink.config.ActiveMQOutboundGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
public class Test1Listener {
    private static final Logger logger = LoggerFactory.getLogger(Test1Listener.class);

    @Autowired
    private ActiveMQOutboundGateway.BarReservationGateway gateway;

    public void publishReservation(String message) {
        logger.info("publishing message {}", message);
        gateway.publishReservation(message);
    }
}
