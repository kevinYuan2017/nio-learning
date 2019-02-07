package com.kevin.netty.aio.timeserveraio;

import com.kevin.netty.aio.timeserveraio.thread.AsyncTimeServerHandler;


public class TimeserverAioApplication {

	public static void main(String[] args) {
//		SpringApplication.run(TimeserverAioApplication.class, args);
        int port = 9000;
        if (args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                // 采用默认值
            }
        }
        System.out.println("port = " + port);
        AsyncTimeServerHandler asyncTimeServerHandler = new AsyncTimeServerHandler(port);
        new Thread(asyncTimeServerHandler, "AIO-AsyncTimeServerHandler-001").start();
    }

}

