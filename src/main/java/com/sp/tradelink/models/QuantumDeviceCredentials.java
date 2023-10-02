package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class QuantumDeviceCredentials extends BaseModel {
    @JsonProperty("IPaddress")
    String ipAddress;
    @JsonProperty("PosID")
    String posID;
    @JsonProperty("Port")
    String port;
    @JsonProperty("MacAddress")
    String macAddress;
    @JsonProperty("TerminalID")
    String terminalID;
    @JsonProperty("Token")
    String token;
    @JsonProperty("SerialNum")
    String serialNum;
}
