package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sp.tradelink.utils.AppStringUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class QuantumHBRequest extends QuantumDefaultRequest implements Serializable {
    @JsonProperty("ExtData")
    private String extData;

    public QuantumHBRequest() {
        super();
    }

    @Override
    public String toString() {
        return AppStringUtils.convertObject2Json(this);
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
                Integer.parseInt(this.timeout) < 90;
        return !result;
    }
}
