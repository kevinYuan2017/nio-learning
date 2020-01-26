package com.yzk.demo.nio.messagepack.codec;

import com.yzk.demo.nio.messagepack.model.UserInfo;
import com.yzk.demo.nio.messagepack.util.MsgpackUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgpackEncoder extends MessageToByteEncoder<UserInfo> {
    @Override
    protected void encode(ChannelHandlerContext ctx, UserInfo msg, ByteBuf out) throws Exception {
        out.writeBytes(MsgpackUtil.toBytes(msg));
    }
}
