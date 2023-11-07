package com.sp.tradelink.common;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import java.io.IOException;

public class RestTemplateLogInterceptor implements ClientHttpRequestInterceptor {

    private MessageChannel logChannel;

    public MessageChannel getLogChannel() {
        return logChannel;
    }

    public RestTemplateLogInterceptor setLogChannel(MessageChannel logChannel) {
        this.logChannel = logChannel;
        return this;
    }

    @Override
    @NonNull
    public ClientHttpResponse intercept(
            @NonNull HttpRequest request,
            @NonNull byte[] body,
            @NonNull ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = null;
        try {
            response = execution.execute(request, body);
        } catch (Exception ex) {
            getLogChannel().send(new GenericMessage<>(ex));
            throw ex;
        }
        return response;
    }
}