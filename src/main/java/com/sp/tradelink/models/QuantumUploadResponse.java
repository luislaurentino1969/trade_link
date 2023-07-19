package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.sp.tradelink.utils.AppStringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuantumUploadResponse {
    @JsonProperty("ResultCode")
    private String resultCode;
    @JsonProperty("ResultTxt")
    private String resultTxt;
    @JsonProperty("ResultMsg")
    private String resultMsg;
    @JsonProperty("IPaddress")
    private String ipAddress;
    @JsonProperty("PosID")
    private String posID;
    @JsonProperty("Port")
    private String port;
    @JsonProperty("MacAddress")
    private String macAddress;
    @JsonProperty("TerminalID")
    private String terminalID;
    @JsonProperty("Token")
    private String token;
    @JsonProperty("SerialNum")
    private String serialNum;
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("TraceNum")
    private String traceNum;
    @JsonProperty("CommandType")
    private String commandType;
    @JsonProperty("CommandRequest")
    private JsonNode commandRequest;
    @JsonProperty("ExtData")
    private JsonNode extData;

    @Override
    public String toString() {
        return AppStringUtils.convertObject2Json(this);
    }
}
