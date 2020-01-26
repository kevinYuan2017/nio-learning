package com.kevin.learning.netty.httpserver.handler.http;

import com.kevin.learning.netty.httpserver.Util.HttpResponseUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.spdy.SpdyHeaders.HttpNames.METHOD;

@Component
public class MyHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyHttpRequestHandler.class);
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (request.getDecoderResult().isSuccess()) {
            if (request.getUri().equals("/hello")) {
//                LOGGER.info("RequestURI: {}", request.getUri());
                HttpResponseUtil.returnJson(ctx, HttpResponseStatus.OK, "Hi");
            }else {
                request.retain();
                ctx.fireChannelRead(request);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
