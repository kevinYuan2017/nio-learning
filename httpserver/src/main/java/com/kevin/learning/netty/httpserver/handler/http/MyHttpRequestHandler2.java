package com.kevin.learning.netty.httpserver.handler.http;

import com.kevin.learning.netty.httpserver.Util.HttpResponseUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyHttpRequestHandler2 extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyHttpRequestHandler2.class);
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (request.getDecoderResult().isSuccess()) {
            if (request.getUri().equals("/hello2")) {
//                LOGGER.info("RequestURI: {}", request.getUri());
                HttpResponseUtil.returnJson(ctx, HttpResponseStatus.OK, "Hi2");
            }else {
                HttpResponseUtil.returnJson(ctx, HttpResponseStatus.OK, "Hi0");
            }
        }
    }
}
