package com.yzk.demo.nio.marshalling.client;

import com.yzk.demo.nio.marshalling.client.handler.SubClientHandler;
import com.yzk.demo.nio.marshalling.codec.MarshallingCodeCFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SubClient {
    private String host;
    private int port;
    private int subCount;

    public SubClient(String host, int port, int subCount) {
        this.host = host;
        this.port = port;
        this.subCount = subCount;
    }

    public void connect() throws InterruptedException {
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(clientGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.TRACE))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("marshalling-decoder", MarshallingCodeCFactory.buildMarshallingDecoder());
                            pipeline.addLast("marshalling-encoder", MarshallingCodeCFactory.buildMarshallingEncoder());
                            pipeline.addLast("sub-client-handler", new SubClientHandler(subCount));
                        }
                    });
            ChannelFuture channelFuture = b.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            clientGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new SubClient("localhost", 9000, 100000).connect();
    }
}
