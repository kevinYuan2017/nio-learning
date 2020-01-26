package com.yzk.demo.nio.httphello.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHandler.class);
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 1000000; i++) {
            ctx.writeAndFlush(new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/hello"));
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        int count = counter.incrementAndGet();
        LOGGER.info("client received msg from server : " + msg.content().toString(Charset.defaultCharset()) + ", time: " + count);
    }
}
