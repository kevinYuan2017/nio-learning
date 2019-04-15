package com.kevin.learning.netty.httpserver.handler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.sun.deploy.net.HttpRequest.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Values.KEEP_ALIVE;
import static io.netty.handler.codec.rtsp.RtspHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.spdy.SpdyHeaders.HttpNames.METHOD;

@Component
public class MyHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyHttpRequestHandler.class);
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        LOGGER.info("FullHttpRequest: {}", request);
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put(METHOD, request.getMethod().name());
        returnMap.put("REQUEST_URI", request.getUri());
        String jsonString = JSON.toJSONString(returnMap);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(jsonString.getBytes());

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        response.headers().set(CONTENT_TYPE, "application/json; charset=utf-8");
        response.headers().set(CONNECTION, KEEP_ALIVE);
        response.headers().set(CONTENT_LENGTH, jsonString.length());
        LOGGER.info(response.toString());

        ctx.writeAndFlush(response);
    }
}
