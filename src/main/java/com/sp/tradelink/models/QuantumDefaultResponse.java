package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class QuantumDefaultResponse extends QuantumDeviceCredentials {
    @JsonProperty("ResultCode")
    String resultCode;
    @JsonProperty("ResultTxt")
    String resultTxt;
    @JsonProperty("ResultMsg")
    String resultMsg;

}
