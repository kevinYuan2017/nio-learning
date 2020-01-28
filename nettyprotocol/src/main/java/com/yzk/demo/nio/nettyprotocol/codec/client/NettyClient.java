package com.yzk.demo.nio.nettyprotocol.codec.client;

import com.yzk.demo.nio.nettyprotocol.codec.codec.NettyMessageDecoder;
import com.yzk.demo.nio.nettyprotocol.codec.codec.NettyMessageEncoder;
import com.yzk.demo.nio.nettyprotocol.codec.consts.NettyConstant;
import com.yzk.demo.nio.nettyprotocol.codec.handler.HeartBeatReqHandler;
import com.yzk.demo.nio.nettyprotocol.codec.handler.LoginAuthReqHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.yzk.demo.nio.nettyprotocol.codec.consts.NettyConstant.PORT;

public class NettyClient {
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String host, int port) throws Exception {
        // 配置客户端NIO线程组
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
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
                            pipeline.addLast("login-auth-req-handler", new LoginAuthReqHandler());
                            pipeline.addLast("heartbeat-req-handler", new HeartBeatReqHandler());
                        }
                    });
            // 发起异步连接操作
//            ChannelFuture channelFuture = b.connect(new InetSocketAddress(host, port), new InetSocketAddress(NettyConstant.LOCALIP, NettyConstant.LOCAL_PORT)).sync();
            ChannelFuture channelFuture = b.connect(new InetSocketAddress(host, port)).sync();
            System.out.println(this.getClass().getName() + " connected to server on : " + channelFuture.channel().remoteAddress());
            channelFuture.channel().closeFuture().sync();
        }finally {
//            group.shutdownGracefully();
            // 所有资源释放完之后, 清空资源, 再次发起重连操作
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("断线, 5秒钟后重连...");
                        TimeUnit.SECONDS.sleep(5);
                        try {
                            System.out.println("重连开始...");
                            connect(NettyConstant.REMOTEIP, NettyConstant.PORT); // 发起重连操作
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyClient().connect(NettyConstant.REMOTEIP, PORT);
    }
}
