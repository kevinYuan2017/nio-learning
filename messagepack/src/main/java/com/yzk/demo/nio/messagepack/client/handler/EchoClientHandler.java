package com.yzk.demo.nio.messagepack.client.handler;

import com.yzk.demo.nio.messagepack.model.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EchoClientHandler extends SimpleChannelInboundHandler<UserInfo> {
    private int sendNumber;

    public EchoClientHandler(int sendNumber) {
        this.sendNumber = sendNumber;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < sendNumber; i++) {
            UserInfo userInfo = new UserInfo("ABC" + i, i);
            ctx.write(userInfo);
        }
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, UserInfo userInfo) throws Exception {
        System.out.println("Client receive the msgpack msg: " + userInfo);
//        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
    }
}
