package com.yzk.demo.nio.messagepack.server.handler;

import com.yzk.demo.nio.messagepack.model.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EchoServerHandler extends SimpleChannelInboundHandler<UserInfo> {
    @Override
    public void channelRead0(ChannelHandlerContext ctx, UserInfo userInfo) throws Exception {
        System.out.println("Server receive msgpack msg: " + userInfo);
        ctx.write(userInfo);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
