package com.kevin.learning.netty.timeserver;


import com.kevin.learning.netty.timeserver.handler.DiscardServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServerApplication {
    private int port;

    public DiscardServerApplication(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)                  //for the NioServerSocketChannel that accepts incoming connections
                    .childOption(ChannelOption.SO_KEEPALIVE, true);     // for the Channels accepted by the parent ServerChannel, which is NioServerSocketChannel in this case.
            // bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();

            // wait until the server socket is closed.
            // in this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
//		SpringApplication.run(TimeserverApplication.class, args);
        int port = 8080;
        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        new DiscardServerApplication(port).run();
    }

}

