package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sp.tradelink.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class QuantumHBRequest {
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
    @JsonProperty("ExtData")
    private String extData;

    public QuantumHBRequest() {
    }

    @Override
    public String toString() {
        return StringUtils.convertObject2Json(this);
    }

    @JsonIgnore(true)
    public boolean isValid() {
        boolean result;
        result = this.userName == null || this.userName.isEmpty() || this.userName.isBlank() ||
                this.password == null || this.password.isEmpty() || this.password.isBlank() ||
                this.posID == null || this.posID.isEmpty() || this.posID.isBlank() ||
                this.serialNum == null || this.serialNum.isEmpty() || this.serialNum.isBlank() ||
                this.terminalID == null || this.terminalID.isEmpty() || this.terminalID.isBlank() ||
                this.ipAddress == null || this.ipAddress.isEmpty() || this.ipAddress.isBlank() ||
                this.timeout < 90;
        return !result;
    }
}
