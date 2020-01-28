package com.yzk.demo.nio.nettyprotocol.codec.codec;

import com.yzk.demo.nio.nettyprotocol.codec.frame.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Netty消息编码类
 * */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
    private static final Logger LOGGER = Logger.getLogger(NettyMessageEncoder.class.getName());
    private MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if (msg == null || msg.getHeader() == null)
            throw new Exception("The encode message is null");

        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionID());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        sendBuf.writeInt(msg.getHeader().getAttachment().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param :
             msg.getHeader().getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes("UTF-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);

            value = param.getValue();
            marshallingEncoder.encode(value, sendBuf);
        }

        key = null;
        keyArray = null;
        value = null;
        if (msg.getBody() != null) {
            marshallingEncoder.encode(msg.getBody(), sendBuf);
        }else
            sendBuf.writeInt(0);

        // modify数据体长度 = readableBytes - (crc/4字节 + length自身/4字节)的长度
        // index是4, 是应为crc的长度是4个字节, 所以, length的起始位/index就是4, 由于我本身对这部分不熟悉, 增加这个注释
        sendBuf.setInt(4, sendBuf.readableBytes() - (4 + 4));

        ByteBuf copy = sendBuf.copy();
        byte[] bytes = new byte[copy.readableBytes()];
        copy.readBytes(bytes, 0, copy.readableBytes());
        copy.release();
        LOGGER.info(" byteArray after encoded NettyMessage object to byteBuf : " + Arrays.toString(bytes));
        out.add(sendBuf);
    }
}
