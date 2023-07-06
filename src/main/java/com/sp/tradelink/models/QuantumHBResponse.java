package com.sp.tradelink.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sp.tradelink.utils.StringUtils;

public class QuantumHBResponse {

    private int _resultCode;
    private String _resultMsg;
    private String _ipAddress;
    private String _posID;
    private String _port;
    private String _macAddress;
    private String _terminalID;
    private String _token;
    private String _serialNum;
    private String _amount;
    private String _traceNum;
    private String _posLinkRequest;
    private String _posLinkResponse;
    private String _extData;

    public QuantumHBResponse() {
    }

    @JsonProperty("ResultCode")
    public int getResultCode() {
        return this._resultCode;
    }

    public void setResultCode(int resultCode) {
        this._resultCode = resultCode;
    }

    @JsonProperty("ResultMsg")
    public String getResultMsg() {
        return this._resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this._resultMsg = resultMsg;
    }

    @JsonProperty("IPaddress")
    public String getIPAddress() {
        return this._ipAddress;
    }

    public void setIPAddress(String ipAddress) {
        this._ipAddress = ipAddress;
    }

    @JsonProperty("PosID")
    public String getPosID() {
        return this._posID;
    }

    public void setPosID(String posID) {
        this._posID = posID;
    }

    @JsonProperty("Port")
    public String getPort() {
        return this._port;
    }

    public void setPort(String port) {
        this._port = port;
    }

    @JsonProperty("MacAddress")
    public String getMacAddress() {
        return this._macAddress;
    }

    public void setMacAddress(String macAddress) {
        this._macAddress = macAddress;
    }

    @JsonProperty("TerminalID")
    public String getTerminalID() {
        return this._terminalID;
    }

    public void setTerminalID(String terminalID) {
        this._terminalID = terminalID;
    }

    @JsonProperty("Token")
    public String getToken() {
        return this._token;
    }

    public void setToken(String token) {
        this._token = token;
    }

    @JsonProperty("SerialNum")
    public String getSerialNum() {
        return this._serialNum;
    }

    public void setSerialNum(String serialNum) {
        this._serialNum = serialNum;
    }

    @JsonProperty("Amount")
    public String getAmount() {
        return this._amount;
    }

    public void setAmount(String amount) {
        this._amount = amount;
    }

    @JsonProperty("TraceNum")
    public String getTraceNum() {
        return this._traceNum;
    }

    public void setTraceNum(String traceNum) {
        this._traceNum = traceNum;
    }

    @JsonProperty("POSLinkRequest")
    public String getPOSLinkRequest() {
        return this._posLinkRequest;
    }

    public void setPOSLinkRequest(String posLinkRequest) {
        this._posLinkRequest = posLinkRequest;
    }

    @JsonProperty("POSLinkResponse")
    public String getPOSLinkResponse() {
        return this._posLinkResponse;
    }

    public void setPOSLinkResponse(String posLinkResponse) {
        this._posLinkResponse = posLinkResponse;
    }

    @JsonProperty("ExtData")
    public String getExtData() {
        return this._extData;
    }

    public void setExtData(String extData) {
        this._extData = extData;
    }

    @Override
    public String toString() {
        return StringUtils.convertObject2Json(this);
    }
}
