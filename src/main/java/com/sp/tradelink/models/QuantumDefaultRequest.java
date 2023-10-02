package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class QuantumDefaultRequest extends QuantumDeviceCredentials {
    @JsonProperty("UserName")
    String userName;
    @JsonProperty("Password")
    String password;
    @JsonProperty("TimeOut")
    String timeout;
    @JsonProperty("TraceNum")
    String traceNum;
}
