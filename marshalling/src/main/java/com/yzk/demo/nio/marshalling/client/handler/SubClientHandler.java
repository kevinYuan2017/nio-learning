package com.yzk.demo.nio.marshalling.client.handler;

import com.yzk.demo.nio.marshalling.model.SubReq;
import com.yzk.demo.nio.marshalling.model.SubResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SubClientHandler extends SimpleChannelInboundHandler<SubResp> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubClientHandler.class);
    private int subCount;

    public SubClientHandler(int subCount) {
        this.subCount = subCount;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < subCount; i++) {
            SubReq subReq = new SubReq();
            subReq.setReqId(UUID.randomUUID().toString());
            subReq.setUserName("Zhongkai Yuan");
            subReq.setProductName("The Old Man And The Sea");
            List<String> address = new ArrayList<>();
            address.add("第一大街11号");
            address.add("第二大街11号");
            address.add("第三大街11号");
            address.add("第四大街11号");
            subReq.setAddress(address);
            ctx.write(subReq);
        }
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SubResp msg) throws Exception {
        LOGGER.info("Client receive msg from server: " + msg);
    }
}
