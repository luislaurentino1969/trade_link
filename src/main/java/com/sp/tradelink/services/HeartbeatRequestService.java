package com.sp.tradelink.services;

import com.sp.tradelink.config.ActiveMQOutboundConfig;
import com.sp.tradelink.config.ActiveMQOutboundGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeartbeatRequestService {
    private static final Logger logger = LoggerFactory.getLogger(HeartbeatRequestService.class);

//    @Autowired
//    private ActiveMQOutboundGateway.AddressGateway gateway;
//    private ActiveMQOutboundConfig.HeartbeatRequestGateway gateway;

//    public HeartbeatRequestService(ActiveMQOutboundConfig.HeartbeatRequestGateway gateway) {
//        this.gateway = gateway;
//    }

    public void publishHeartbeatRequest(Object heartbeat) {
        logger.info("Will start quantum heartbeat process.");
//        gateway.publishHeartbeatRequest(heartbeat);
    }
}
