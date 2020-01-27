package com.yzk.demo.nio.httpxml.codec;

import com.yzk.demo.nio.httpxml.codec.abs.AbstractHttpXmlEncoder;
import com.yzk.demo.nio.httpxml.codec.model.HttpXmlResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.List;

public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlResponse msg, List<Object> out) throws Exception {
        ByteBuf body = encode0(ctx, msg.getResult());
        FullHttpResponse response = msg.getHttpResponse();
        if (response == null) {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        }else {
            response = new DefaultFullHttpResponse(msg.getHttpResponse().protocolVersion(), msg.getHttpResponse().status(), body);
        }

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/xml");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, body.readableBytes());
        out.add(response);
    }
}
