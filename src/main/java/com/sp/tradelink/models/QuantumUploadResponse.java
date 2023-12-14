package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.sp.tradelink.utils.AppStringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuantumUploadResponse extends QuantumDefaultResponse {
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("TraceNum")
    private String traceNum;
    @JsonProperty("ServiceOperation")
    private String serviceOperation;
    @JsonProperty("SPRequest")
    private JsonNode spRequest;
    @JsonProperty("ExtData")
    private JsonNode extData;

    @Override
    public String toString() {
        return AppStringUtils.convertObject2Json(this);
    }
}
