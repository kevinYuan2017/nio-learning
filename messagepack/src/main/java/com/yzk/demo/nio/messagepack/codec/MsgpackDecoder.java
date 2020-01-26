package com.yzk.demo.nio.messagepack.codec;

import com.yzk.demo.nio.messagepack.model.UserInfo;
import com.yzk.demo.nio.messagepack.util.MsgpackUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final byte[] array;
        final int length = msg.readableBytes();
        array = new byte[length];
        msg.getBytes(msg.readerIndex(), array, 0, length);
        out.add(MsgpackUtil.toObject(array, UserInfo.class));
    }
}
