package com.sp.tradelink.common;

public class HttpResponseException extends Exception {
    public HttpResponseException() {
        super("Http connection error");
    }

    public HttpResponseException(String message) {
        super(message);
    }

    public HttpResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpResponseException(Throwable cause) {
        super(cause);
    }

    public HttpResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
