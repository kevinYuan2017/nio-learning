package com.yzk.demo.nio.websocketchat.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;

import java.util.Date;

public class ChatHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;
            System.out.println(textWebSocketFrame.content().toString(CharsetUtil.UTF_8));
            TextWebSocketFrame frame = new TextWebSocketFrame("欢迎使用websocket聊天, 当前系统时间: " + new Date());
            ctx.writeAndFlush(frame);
        }
    }
}
