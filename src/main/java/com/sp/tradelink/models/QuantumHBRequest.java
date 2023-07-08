package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sp.tradelink.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class QuantumHBRequest {
    @JsonProperty("UserName")
    String _userName;
    @JsonProperty("Password")
    String _password;
    @JsonProperty("Token")
    String _token;
    @JsonProperty("TerminalID")
    String _terminalID;
    @JsonProperty("PosID")
    String _posID;
    @JsonProperty("TimeOut")
    int _timeout;
    @JsonProperty("SerialNum")
    String _serialNum;
    @JsonProperty("IPaddress")
    String _ipAddress;
    @JsonProperty("MacAddress")
    String _macAddress;
    @JsonProperty("Port")
    String _port;
    @JsonProperty("TraceNum")
    String _traceNum;
    @JsonProperty("ExtData")
    String _extData;

    public QuantumHBRequest() {
    }

    public QuantumHBRequest(String userName, String password, String token, String terminalID, String posID,
                            int timeout, String serialNum, String ipAddress, String macAddress, String port) {
        this._userName = userName;
        this._password = password;
        this._token = token;
        this._terminalID = terminalID;
        this._posID = posID;
        this._timeout = timeout;
        this._serialNum = serialNum;
        this._ipAddress = ipAddress;
        this._macAddress = macAddress;
        this._port = port;
    }

    @Override
    public String toString() {
        return StringUtils.convertObject2Json(this);
    }

    @JsonIgnore(true)
    public boolean isValid() {
        boolean result;
        result = this._userName == null || this._userName.isEmpty() || this._userName.isBlank() ||
                this._password == null || this._password.isEmpty() || this._password.isBlank() ||
                this._posID == null || this._posID.isEmpty() || this._posID.isBlank() ||
                this._serialNum == null || this._serialNum.isEmpty() || this._serialNum.isBlank() ||
                this._terminalID == null || this._terminalID.isEmpty() || this._terminalID.isBlank() ||
                this._ipAddress == null || this._ipAddress.isEmpty() || this._ipAddress.isBlank() ||
                this._timeout < 90;
        return !result;
    }
}
