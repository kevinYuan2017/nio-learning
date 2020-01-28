package com.yzk.demo.nio.nettyprotocol.codec.handler;

import com.yzk.demo.nio.nettyprotocol.codec.enums.MessageType;
import com.yzk.demo.nio.nettyprotocol.codec.frame.Header;
import com.yzk.demo.nio.nettyprotocol.codec.frame.NettyMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;

public class HeartBeatRespHandler extends ChannelHandlerAdapter {
    /**
     * Calls {@link ChannelHandlerContext#fireChannelRead(Object)} to forward
     * to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        // 返回心跳应答消息
        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
            System.out.println("Received client heart beat message : --> " + message);
            NettyMessage heartbeat = buildHeartBeat();
            System.out.println("Send heart beat response message to client : --> " + message);
            ctx.writeAndFlush(heartbeat);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeartBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}
