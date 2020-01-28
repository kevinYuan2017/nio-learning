package com.yzk.demo.nio.nettyprotocol.codec.server;

import com.yzk.demo.nio.nettyprotocol.codec.codec.NettyMessageDecoder;
import com.yzk.demo.nio.nettyprotocol.codec.codec.NettyMessageEncoder;
import com.yzk.demo.nio.nettyprotocol.codec.consts.NettyConstant;
import com.yzk.demo.nio.nettyprotocol.codec.handler.HeartBeatRespHandler;
import com.yzk.demo.nio.nettyprotocol.codec.handler.LoginAuthRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyServer {
    public void bind() throws Exception {
        // 配置服务端的NIO线程组
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.TRACE))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * This method will be called once the {@link Channel} was registered. After the method returns this instance
                         * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
                         *
                         * @param ch the {@link Channel} which was registered.
                         * @throws Exception is thrown if an error occurs. In that case the {@link Channel} will be closed.
                         */
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("netty-message-decoder", new NettyMessageDecoder(1024 * 1024, 4, 4));
                            pipeline.addLast("netty-message-encoder", new NettyMessageEncoder());
                            pipeline.addLast("read-timeout-handler", new ReadTimeoutHandler(10));
                            pipeline.addLast("login-auth-resp-handler", new LoginAuthRespHandler());
                            pipeline.addLast("heartbeat-resp-handler", new HeartBeatRespHandler());
                        }
                    });
            // 绑定端口, 同步等待成功
            ChannelFuture channelFuture = b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
            System.out.println(this.getClass().getName() + " start OK on : " + channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();
        }finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyServer().bind();
    }
}
