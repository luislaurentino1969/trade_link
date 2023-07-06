package com.sp.tradelink.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HeartbeatResponseService {
    private static final Logger logger = LoggerFactory.getLogger(HeartbeatResponseService.class);

//    @Autowired
//    private ActiveMQInboundConfig.HeartbeatResponseGateway gateway;

    public void publishHeartbeatResponse(Object heartbeat) {
        logger.info("Will send quantum heartbeat response to brand link.");
//        gateway.publishHeartbeatResponse(heartbeat);
    }
}
