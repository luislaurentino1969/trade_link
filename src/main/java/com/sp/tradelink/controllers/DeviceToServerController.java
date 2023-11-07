package com.sp.tradelink.controllers;

import com.sp.tradelink.models.DefaultErrorResponse;
import com.sp.tradelink.models.QuantumUploadRequest;
import com.sp.tradelink.models.QuantumUploadResponse;
import com.sp.tradelink.services.messaging.QuantumUploadDeviceResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("device-to-server/v1")
@IntegrationComponentScan("com.sp.tradelink")
public class DeviceToServerController {
    private final Logger logger;

    private final QuantumUploadDeviceResponseService service;

    public DeviceToServerController(Logger logger, QuantumUploadDeviceResponseService service) {
        this.logger = logger;
        this.service = service;
    }

    @Operation(summary = "Quantum DeviceToServer service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = { @Content(schema = @Schema(implementation = QuantumUploadResponse.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = { @Content(schema = @Schema(implementation = DefaultErrorResponse.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "Server Error",
                    content = { @Content(schema = @Schema(implementation = DefaultErrorResponse.class),
                            mediaType = "application/json") })})
    @RequestMapping(value = "/quantum", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<?> initiateMainLinkHeartbeat(@RequestBody QuantumUploadRequest info, @RequestHeader Map<String, String> headers) {
        logger.debug("Headers: {}", headers.values());
        logger.debug("Information: {}", info.toString());
        try {
            return new ResponseEntity<>(service.startUpload(MessageBuilder.withPayload(info).build()).getPayload(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error processing DeviceToServer request.", ex);
            return new ResponseEntity<>(new DefaultErrorResponse().setResultCode("-00500")
                    .setResultMsg("Error processing request to cloud.")
                    .setResultTxt(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
