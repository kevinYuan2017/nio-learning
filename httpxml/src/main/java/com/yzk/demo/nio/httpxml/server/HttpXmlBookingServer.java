package com.yzk.demo.nio.httpxml.server;

import com.yzk.demo.nio.httpxml.codec.HttpXmlRequestDecoder;
import com.yzk.demo.nio.httpxml.codec.HttpXmlResponseEncoder;
import com.yzk.demo.nio.httpxml.pojo.Order;
import com.yzk.demo.nio.httpxml.server.handler.HttpXmlBookingServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HttpXmlBookingServer {
    private int port;

    public HttpXmlBookingServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.TRACE))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast("http-server-codec", new HttpServerCodec());
                            // xml 解析器
                            pipeline.addLast("http-req-decoder", new HttpRequestDecoder());
                            pipeline.addLast("http-obj-aggregator", new HttpObjectAggregator(8192));
                            pipeline.addLast("http-xml-req-decoder", new HttpXmlRequestDecoder(Order.class, true));

                            pipeline.addLast("http-resp-encoder", new HttpResponseEncoder());
                            // xml 编码器
                            pipeline.addLast("http-xml-resp-encoder", new HttpXmlResponseEncoder());

                            pipeline.addLast("http-xml-booking-server-handler", new HttpXmlBookingServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            System.out.println(this.getClass().getName() + " started on: " + channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();
        }finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new HttpXmlBookingServer(9000).start();
    }
}
