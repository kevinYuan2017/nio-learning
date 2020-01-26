package com.yzk.demo.nio.httpfileserver;

import com.yzk.demo.nio.httpfileserver.handler.HttpFileServerHanlder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.logging.Logger;

public class FileServer {
    private static final Logger LOGGER = Logger.getLogger(FileServer.class.getName());
    private static final int DEFAULT_PORT = 9000;
    private int port;

    public FileServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.TRACE))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("http-server-codec", new HttpServerCodec());
                            p.addLast("http-obj-aggregator", new HttpObjectAggregator(65535));
//                            p.addLast("http-response-encoder", new HttpResponseEncoder());
                            p.addLast("http-chunked-writer", new ChunkedWriteHandler());
                            p.addLast("http-file-server-handler", new HttpFileServerHanlder());
                        }
                    });
            ChannelFuture channelFuture = b.bind(port).sync();
            LOGGER.info("文件服务器启动成功, 网址是: http://localhost:" + port);
            channelFuture.channel().closeFuture().sync();
        }finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new FileServer(DEFAULT_PORT).start();
    }
}
