package com.yzk.demo.nio.httphello.client;

import com.yzk.demo.nio.httphello.client.handler.HttpClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class HelloClient {
    private String host;
    private int port;

    public HelloClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws InterruptedException {
        NioEventLoopGroup clientEventGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(clientEventGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-client-codec", new HttpClientCodec());
                            pipeline.addLast("http-aggregator", new HttpObjectAggregator(4096));
                            pipeline.addLast("http-client-handler", new HttpClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            System.out.println(this.getClass().getName() + " connected to : " + channelFuture.channel().remoteAddress());
            channelFuture.channel().closeFuture().sync();
        }finally {
            clientEventGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 9000;
        if (args != null && args.length > 1) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        new HelloClient(host, port).connect();
    }
}
