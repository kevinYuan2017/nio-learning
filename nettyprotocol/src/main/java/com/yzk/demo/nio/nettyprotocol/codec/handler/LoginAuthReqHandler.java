package com.yzk.demo.nio.nettyprotocol.codec.handler;

import com.yzk.demo.nio.nettyprotocol.codec.enums.MessageType;
import com.yzk.demo.nio.nettyprotocol.codec.frame.Header;
import com.yzk.demo.nio.nettyprotocol.codec.frame.NettyMessage;
import io.netty.channel.*;

public class LoginAuthReqHandler extends SimpleChannelInboundHandler<NettyMessage> {
    /**
     * Calls {@link ChannelHandlerContext#fireChannelActive()} to forward
     * to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }


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
        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            byte loginResult = (Byte) message.getBody();      // 书中原文中写道的强转为byte类型是不对的
            if (loginResult != (byte) 0) {
                ctx.close();
            }else {
                System.out.println("Login is ok: " + message);
                ctx.fireChannelRead(message);
            }
        }else {
            ctx.fireChannelRead(message);
        }
    }

    private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
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
        super.exceptionCaught(ctx, cause);
    }
}
