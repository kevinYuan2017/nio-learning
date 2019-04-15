package com.kevin.learning.netty.httpserver.Util;

import com.alibaba.fastjson.JSON;
import com.sun.istack.internal.NotNull;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.sun.deploy.net.HttpRequest.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Values.KEEP_ALIVE;
import static io.netty.handler.codec.rtsp.RtspHeaders.Names.CONTENT_LENGTH;

public class HttpResponseUtil {
    private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpResponseUtil.class);
    public static void returnJson(ChannelHandlerContext ctx, HttpResponseStatus status, @NotNull Object data) {
        String jsonString = JSON.toJSONString(data);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(jsonString.getBytes());

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, byteBuf);
        response.headers().set(CONTENT_TYPE, CONTENT_TYPE_JSON);
        response.headers().set(CONNECTION, KEEP_ALIVE);
        response.headers().set(CONTENT_LENGTH, jsonString.length());

        LOGGER.info(response.toString());
        ctx.writeAndFlush(response);
    }
}
