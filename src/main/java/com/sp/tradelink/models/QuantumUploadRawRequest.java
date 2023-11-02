package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.sp.tradelink.utils.AppStringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuantumUploadRawRequest {
    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("SerialNum")
    private String serialNumber;
    @JsonProperty("TraceNum")
    private String traceNum;
    @JsonProperty("RawResponse")
    private JsonNode rawResponse;

    @Override
    public String toString() {
        return AppStringUtils.convertObject2Json(this);
    }
}
