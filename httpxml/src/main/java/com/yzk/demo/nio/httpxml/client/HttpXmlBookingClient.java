package com.yzk.demo.nio.httpxml.client;

import com.yzk.demo.nio.httpxml.client.handler.HttpXmlBookingClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class HttpXmlBookingClient {
    private String host;
    private int port;

    public HttpXmlBookingClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws Exception {
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-client-codec", new HttpClientCodec());
                            pipeline.addLast("http-client-obj-aggregator", new HttpObjectAggregator(8192));
                            pipeline.addLast("http-xml-booking-client-handler", new HttpXmlBookingClientHandler(host));
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            System.out.println(this.getClass().getName() + " connected to booking server on : " + channelFuture.channel().remoteAddress());
            channelFuture.channel().closeFuture().sync();
        }finally {
            System.out.println("program is shutting down, bye bye ...");
            clientGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new HttpXmlBookingClient("localhost", 9000).connect();
    }
}
