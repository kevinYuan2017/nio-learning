package com.kevin.learning.netty.timeserver.thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeSerer implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean stop;

    /**
    * 初始化多路复用器，绑定监听端口
    * @param port
    * */
    public MultiplexerTimeSerer(int port){
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The TimeServer is start in port: " + port);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("TimeServer is shutting down, bye bye!");
            System.exit(1);
        }
    }

    public void stop(){
        this.stop = true;
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()){
            // 处理新接入的请求消息
            if (key.isAcceptable()){
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                // Add the new Configuration to the selector
                socketChannel.register(selector, SelectionKey.OP_READ);
            }

            if (key.isReadable()){
                // read the data
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(readBuffer);
                if (readBytes > 0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("The TimeServer receive order: " + body);
                    System.out.println("order length: " + body.length());

                    String[] bodyStrs = body.split("\n");
                    int length = bodyStrs.length;
                    System.out.println("bodyStrs.length = " + length);
                    for (int i = 0; i < length; i++) {
                        System.out.println("bodyStr[" + i + "] = " + bodyStrs[i]);
                    }

                    String currentTime = "QUERY TIME ORDER"
                    .equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                    doWrite(socketChannel, currentTime);
                }else if (readBytes < 0){
                    // 对端链路关闭
                    key.cancel();
                    socketChannel.close();
                }else {
                    // 读到0字节，忽略
                }
            }
        }
    }

    public void doWrite(SocketChannel channel, String response) throws IOException {
        if (response != null && response.trim().length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
    @Override
    public void run() {
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    }catch (Exception e){
                        key.cancel();
                        if (key.channel() != null){
                            key.channel().close();
                        }
                    }
                }
            }catch (Throwable t){
                t.printStackTrace();
            }
        }
    }
}
