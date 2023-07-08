package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sp.tradelink.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class QuantumHBResponse {

    @JsonProperty("ResultCode")
    private int resultCode;
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
    private String commandRequest;
    @JsonProperty("ExtData")
    private String extData;

    @Override
    public String toString() {
        return StringUtils.convertObject2Json(this);
    }
}
