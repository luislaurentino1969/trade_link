package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.sp.tradelink.utils.AppStringUtils;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class DefaultErrorResponse {
    @JsonProperty("ResultCode")
    private String resultCode;
    @JsonProperty("ResultTxt")
    private String resultTxt;
    @JsonProperty("ResultMsg")
    private String resultMsg;
    @JsonProperty("ExtData")
    private JsonNode extData;

    public DefaultErrorResponse setResultCode(String resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public DefaultErrorResponse setResultTxt(String resultTxt) {
        this.resultTxt = resultTxt;
        return this;
    }

    public DefaultErrorResponse setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
        return this;
    }

    public DefaultErrorResponse setExtData(JsonNode extData) {
        this.extData = extData;
        return this;
    }

    @Override
    public String toString() {
        return AppStringUtils.convertObject2Json(this);
    }
}
