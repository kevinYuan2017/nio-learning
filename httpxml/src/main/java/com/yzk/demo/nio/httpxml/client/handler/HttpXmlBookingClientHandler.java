package com.yzk.demo.nio.httpxml.client.handler;

import com.yzk.demo.nio.httpxml.codec.model.HttpXmlRequest;
import com.yzk.demo.nio.httpxml.codec.model.HttpXmlResponse;
import com.yzk.demo.nio.httpxml.pojo.Order;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HttpXmlBookingClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HttpXmlRequest xmlRequest = new HttpXmlRequest(null, Order.create(123));
        ctx.writeAndFlush(xmlRequest);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
        System.out.println("The client receive response of http header is: " + msg.getHttpResponse().headers().names());
        System.out.println("The client receive response of http body is: " + msg.getResult());
    }
}
