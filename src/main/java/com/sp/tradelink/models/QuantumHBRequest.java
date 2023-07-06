package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sp.tradelink.utils.StringUtils;

public class QuantumHBRequest {
    String _userName;
    String _password;
    String _token;
    String _terminalID;
    String _posID;
    int _timeout;
    String _serialNum;
    String _ipAddress;
    String _macAddress;
    String _traceNum;
    String _port;

    public QuantumHBRequest() {
    }

    public QuantumHBRequest(String userName, String password, String token, String terminalID, String posID,
                            int timeout, String serialNum, String ipAddress, String macAddress, String traceNum,
                            String port) {
        this._userName = userName;
        this._password = password;
        this._token = token;
        this._terminalID = terminalID;
        this._posID = posID;
        this._timeout = timeout;
        this._serialNum = serialNum;
        this._ipAddress = ipAddress;
        this._macAddress = macAddress;
        this._traceNum = traceNum;
        this._port = port;
    }

    @JsonProperty("UserName")
    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        this._userName = userName;
    }

    @JsonProperty("Password")
    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    @JsonProperty("Token")
    public String getToken() {
        return _token;
    }

    public void setToken(String token) {
        this._token = token;
    }

    @JsonProperty("TerminalID")
    public String getTerminalID() {
        return _terminalID;
    }

    public void setTerminalID(String terminalID) {
        this._terminalID = terminalID;
    }

    @JsonProperty("PosID")
    public String getPosID() {
        return _posID;
    }

    public void setPosID(String posID) {
        this._posID = posID;
    }

    @JsonProperty("Timeout")
    @JsonAlias("TimeOut")
    public int getTimeout() {
        return _timeout;
    }

    public void setTimeout(int timeout) {
        this._timeout = timeout;
    }

    @JsonProperty("SerialNum")
    public String getSerialNum() {
        return _serialNum;
    }

    public void setSerialNum(String serialNum) {
        this._serialNum = serialNum;
    }

    @JsonProperty("IPaddress")
    public String getIPAddress() {
        return _ipAddress;
    }

    public void setIPAddress(String ipAddress) {
        this._ipAddress = ipAddress;
    }

    @JsonProperty("MacAddress")
    public String getMacAddress() {
        return _macAddress;
    }

    public void setMacAddress(String macAddress) {
        this._macAddress = macAddress;
    }

    @JsonProperty("TraceNum")
    public String getTraceNum() {
        return _traceNum;
    }

    public void setTraceNum(String traceNum) {
        this._traceNum = traceNum;
    }

    @JsonProperty("Port")
    public String getPort() {
        return _port;
    }

    public void setPort(String port) {
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
                !(this._traceNum == null || this._traceNum.isEmpty() || this._traceNum.isBlank()) ||
                this._timeout < 90;
        return !result;
    }
}
