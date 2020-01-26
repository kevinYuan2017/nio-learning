package com.yzk.demo.nio.marshalling.server.handler;

import com.yzk.demo.nio.marshalling.model.SubReq;
import com.yzk.demo.nio.marshalling.model.SubResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubServerHandler extends SimpleChannelInboundHandler<SubReq> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubServerHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SubReq msg) throws Exception {
        LOGGER.info("Server receive msg from client: " + msg);
        SubResp subResp = new SubResp();
        subResp.setReqId(msg.getReqId());
        subResp.setRespCode(0);
        subResp.setDesc("订购成功!");
        ctx.write(subResp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
