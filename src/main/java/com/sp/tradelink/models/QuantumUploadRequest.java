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
public class QuantumUploadRequest extends QuantumDefaultRequest {
    @JsonProperty("ServiceOperation")
    private String serviceOperation;
    @JsonProperty("SPResponse")
    private JsonNode spResponse;
    @JsonProperty("ExtData")
    private JsonNode extData;

    @Override
    public String toString() {
        return AppStringUtils.convertObject2Json(this);
    }
}
