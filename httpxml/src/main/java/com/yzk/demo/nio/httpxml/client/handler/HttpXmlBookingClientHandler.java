package com.yzk.demo.nio.httpxml.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpXmlBookingClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    private String host;

    public HttpXmlBookingClientHandler(String host) {
        this.host = host;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/hello");
        request.headers().set(HttpHeaderNames.HOST, host);
        request.content().writeBytes("hello".getBytes());
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, "hello".getBytes().length);
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
        ctx.writeAndFlush(request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        System.out.println("client received msg from server: " + msg.content().toString(CharsetUtil.UTF_8));
    }
}
