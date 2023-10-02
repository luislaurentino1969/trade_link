package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.sp.tradelink.utils.AppStringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuantumUploadRequest extends QuantumDefaultRequest {
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
