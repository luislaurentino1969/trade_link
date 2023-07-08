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
    private int _resultCode;
    @JsonProperty("ResultMsg")
    private String _resultMsg;
    @JsonProperty("IPaddress")
    private String _ipAddress;
    @JsonProperty("PosID")
    private String _posID;
    @JsonProperty("Port")
    private String _port;
    @JsonProperty("MacAddress")
    private String _macAddress;
    @JsonProperty("TerminalID")
    private String _terminalID;
    @JsonProperty("Token")
    private String _token;
    @JsonProperty("SerialNum")
    private String _serialNum;
    @JsonProperty("Amount")
    private String _amount;
    @JsonProperty("TraceNum")
    private String _traceNum;
    @JsonProperty("POSLinkRequest")
    private String _posLinkRequest;
    @JsonProperty("POSLinkResponse")
    private String _posLinkResponse;
    @JsonProperty("ExtData")
    private String _extData;

    public QuantumHBResponse() {
    }

    @Override
    public String toString() {
        return StringUtils.convertObject2Json(this);
    }
}
