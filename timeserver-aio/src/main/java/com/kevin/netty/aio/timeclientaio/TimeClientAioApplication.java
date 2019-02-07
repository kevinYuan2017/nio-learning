package com.kevin.netty.aio.timeclientaio;


import com.kevin.netty.aio.timeclientaio.thread.AsyncTimeClientHandler;

public class TimeClientAioApplication {
    public static void main(String[] args) {
        int port = 9000;
        if (args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                // use default value
            }
        }
        System.out.println("port = " + port);
        new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-AsyncTimeClientHandler-001").start();
    }
}
