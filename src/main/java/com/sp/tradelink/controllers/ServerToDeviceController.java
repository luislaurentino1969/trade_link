package com.sp.tradelink.controllers;

import com.sp.tradelink.models.DefaultErrorResponse;
import com.sp.tradelink.models.QuantumHBRequest;
import com.sp.tradelink.models.QuantumHBResponse;
import com.sp.tradelink.services.QuantumHeartbeatRequestService;
import com.sp.tradelink.utils.MsgHeaderConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("server-to-device/v1")
@IntegrationComponentScan("com.sp.tradelink")
public class ServerToDeviceController {
    private final Logger logger;

    @Autowired
    private QuantumHeartbeatRequestService service;

    public ServerToDeviceController(Logger logger) {
        this.logger = logger;
    }

    @Operation(summary = "Quantum ServerToDevice service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = { @Content(schema = @Schema(implementation = QuantumHBResponse.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(schema = @Schema(implementation = DefaultErrorResponse.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = { @Content(schema = @Schema(implementation = DefaultErrorResponse.class),
                            mediaType = "application/json") })})
    @RequestMapping(value="/quantum", method= RequestMethod.POST, produces={"application/json"})
    public ResponseEntity<?> initiateMainLinkHeartbeat(@RequestBody QuantumHBRequest info, @RequestHeader Map<String, String> headers) {
        logger.debug("Headers: {}", headers.values());
        logger.debug("Information: {}", info.toString());
        try {
            service.managePreviousHB(info.getSerialNum(), "stop",
                    headers.get(("X-" + MsgHeaderConstants.BRAND_HEADER).toLowerCase()));
            return new ResponseEntity<>(service.startHeartbeat(MessageBuilder.withPayload(info)
                    .setHeader(MsgHeaderConstants.SOURCE_HEADER, "myself")
                    .setHeader(MsgHeaderConstants.BRAND_HEADER, headers.get(("X-" + MsgHeaderConstants.BRAND_HEADER).toLowerCase()))
                    .build()).getPayload(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error processing ServerToDevice request.", ex);
            return new ResponseEntity<>(new DefaultErrorResponse().setResultCode("-00500")
                    .setResultMsg("Error processing request to cloud.")
                    .setResultTxt(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
