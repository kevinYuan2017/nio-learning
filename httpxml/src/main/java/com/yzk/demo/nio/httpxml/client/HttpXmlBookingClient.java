package com.yzk.demo.nio.httpxml.client;

import com.yzk.demo.nio.httpxml.client.handler.HttpXmlBookingClientHandler;
import com.yzk.demo.nio.httpxml.codec.HttpXmlRequestEncoder;
import com.yzk.demo.nio.httpxml.codec.HttpXmlResponseDecoder;
import com.yzk.demo.nio.httpxml.pojo.Order;
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
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

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
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-resp-decoder", new HttpResponseDecoder());
                            pipeline.addLast("http-client-obj-aggregator", new HttpObjectAggregator(8192));
                            // xml 解码器
                            pipeline.addLast("http-xml-resp-decoder", new HttpXmlResponseDecoder(Order.class, true));


                            pipeline.addLast("http-req-encoder", new HttpRequestEncoder());
                            // xml 编码器
                            pipeline.addLast("http-xml-req-encoder", new HttpXmlRequestEncoder());

                            pipeline.addLast("http-xml-booking-client-handler", new HttpXmlBookingClientHandler());
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
