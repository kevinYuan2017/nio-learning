package com.kevin.learning.netty.timeclient;

import com.kevin.learning.netty.timeclient.thread.TimeClientHandle;

public class TimeClientApplication {
    public static void main(String[] args) {
        int port = 9000;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        System.out.println("port = " + port);

        new Thread(new TimeClientHandle("127.0.0.1", port), "TimeClient-001").start();
    }
}
