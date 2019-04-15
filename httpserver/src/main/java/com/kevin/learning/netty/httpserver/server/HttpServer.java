package com.kevin.learning.netty.httpserver.server;

import com.kevin.learning.netty.httpserver.config.ServerConfig;
import com.kevin.learning.netty.httpserver.handler.MyHttpRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class HttpServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    @Resource
    private ServerConfig serverConfig;

    public HttpServer() {
    }

    @PostConstruct
    public void run() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(4);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(8);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("http-codec", new HttpServerCodec());
                            ch.pipeline().addLast("http-aggregate", new HttpObjectAggregator(serverConfig.getHttp().getMaxContentLength()));
                            ch.pipeline().addLast("http-decoder", new HttpResponseDecoder());
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            ch.pipeline().addLast("requstHandler", new MyHttpRequestHandler());
                        }
                    });
            ChannelFuture channelFuture = b.bind(serverConfig.getHttp().getHost(), serverConfig.getHttp().getPort()).sync();
            LOGGER.info("HTTP Server started on: http://{}:{}",  serverConfig.getHttp().getHost(), serverConfig.getHttp().getPort());
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
