package com.kevin.learning.netty.timeserver;

import com.kevin.learning.netty.timeserver.thread.MultiplexerTimeSerer;

public class TimeserverApplication {

	public static void main(String[] args) {
//		SpringApplication.run(TimeserverApplication.class, args);
        int port = 9000;
        if (args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                // 采用默认值
            }
        }
        System.out.printf("port = %d\n", port);
        MultiplexerTimeSerer timeSerer = new MultiplexerTimeSerer(port);
        new Thread(timeSerer, "NIO-MultiplexerTimeServer-001").start();
    }

}

