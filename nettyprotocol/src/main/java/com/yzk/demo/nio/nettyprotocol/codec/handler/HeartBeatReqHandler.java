package com.yzk.demo.nio.nettyprotocol.codec.handler;

import com.yzk.demo.nio.nettyprotocol.codec.enums.MessageType;
import com.yzk.demo.nio.nettyprotocol.codec.frame.Header;
import com.yzk.demo.nio.nettyprotocol.codec.frame.NettyMessage;
import io.netty.channel.*;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeartBeatReqHandler extends SimpleChannelInboundHandler<NettyMessage> {
    private volatile ScheduledFuture<?> heartBeat;

    /**
     * Calls {@link ChannelHandlerContext#fireChannelRead(Object)} to forward
     * to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     * @param message
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, NettyMessage message) throws Exception {
        // 握手成功, 主动发送心跳消息
        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeartTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
        }else if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
            System.out.println("Client received server heart beat message : --> " + message);
        }else {
            ctx.fireChannelRead(message);
        }
    }

    /**
     * Calls {@link ChannelHandlerContext#fireExceptionCaught(Throwable)} to forward
     * to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    private class HeartBeartTask implements Runnable {
        private ChannelHandlerContext ctx;
        public HeartBeartTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("Client send heart beat message to server : ---> " + heartBeat);
            ctx.writeAndFlush(heartBeat);
        }

        private NettyMessage buildHeartBeat() {
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ.value());
            message.setHeader(header);
            return message;
        }
    }
}
