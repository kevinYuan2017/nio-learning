package com.kevin.learning.nio.netty.echoserver;


import com.kevin.learning.nio.netty.echoserver.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoserverApplication {
	public void bind(int port) throws Exception {
		// config NIO eventLoopGroup for Server
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
							socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
							socketChannel.pipeline().addLast(new StringDecoder());
							socketChannel.pipeline().addLast(new EchoServerHandler());
						}
					});

			// bind port, wait to sync success
			ChannelFuture channelFuture = b.bind(port).sync();
			// wait server port close
			channelFuture.channel().closeFuture().sync();
		} finally {
			// gracefully shutdown
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 9000;
		if (args != null && args.length > 0){
			try {
				port = Integer.valueOf(args[0]);
			}catch (NumberFormatException e){
				// use default value
			}
		}
		new EchoserverApplication().bind(port);
	}

}

