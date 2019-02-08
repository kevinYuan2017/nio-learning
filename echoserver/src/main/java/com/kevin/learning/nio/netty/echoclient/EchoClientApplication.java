package com.kevin.learning.nio.netty.echoclient;


import com.kevin.learning.nio.netty.echoclient.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClientApplication {

	public void connect(String host, int port) throws  Exception {
		// config Nio eventLoopGroup for client
		NioEventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
							socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
							socketChannel.pipeline().addLast(new StringDecoder());
							socketChannel.pipeline().addLast(new EchoClientHandler());
						}
					});

			// lunch connect action
			ChannelFuture channelFuture = b.connect(host, port).sync();

			// wait channel to close
			channelFuture.channel().closeFuture().sync();
		}finally {
			// gracefully shutdown
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 9000;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			}catch (NumberFormatException e) {
				// use deafult value
			}
		}
		new EchoClientApplication().connect("127.0.0.1", port);
	}

}

