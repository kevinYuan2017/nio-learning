package com.yzk.demo.nio.httpxml.codec;

import com.yzk.demo.nio.httpxml.codec.abs.AbstractHttpXmlDecoder;
import com.yzk.demo.nio.httpxml.codec.model.HttpXmlResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.List;

public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<FullHttpResponse> {
    public HttpXmlResponseDecoder(Class<?> clazz) {
        super(clazz, false);
    }

    public HttpXmlResponseDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpResponse msg, List<Object> out) throws Exception {
        HttpXmlResponse httpXmlResponse = new HttpXmlResponse(msg, decode0(ctx, msg.content()));
        out.add(httpXmlResponse);
    }
}
