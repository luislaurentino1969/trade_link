package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.sp.tradelink.utils.AppStringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuantumUploadRequest {
    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("Token")
    private String token;
    @JsonProperty("TerminalID")
    private String terminalID;
    @JsonProperty("PosID")
    private String posID;
    @JsonProperty("TimeOut")
    private int timeout;
    @JsonProperty("SerialNum")
    private String serialNum;
    @JsonProperty("IPaddress")
    private String ipAddress;
    @JsonProperty("MacAddress")
    private String macAddress;
    @JsonProperty("Port")
    private String port;
    @JsonProperty("TraceNum")
    private String traceNum;
    @JsonProperty("CommandType")
    private String commandType;
    @JsonProperty("CommandResponse")
    private JsonNode commandResponse;
    @JsonProperty("ExtData")
    private JsonNode extData;

    @Override
    public String toString() {
        return AppStringUtils.convertObject2Json(this);
    }
}
