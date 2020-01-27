package com.yzk.demo.nio.httpxml.codec.model;

import io.netty.handler.codec.http.FullHttpResponse;

public class HttpXmlResponse {
    private FullHttpResponse httpResponse;
    private Object result;  // 业务需要返回的应答

    public HttpXmlResponse(FullHttpResponse httpResponse, Object result) {
        this.httpResponse = httpResponse;
        this.result = result;
    }

    public FullHttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(FullHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
