package com.example.test.netty;

import com.example.test.netty.client.NettyClient;
import com.example.test.netty.server.NettyServer;

public class Netty {
    public static void main(String[] args) {
        NettyServer.start("192.168.70.248", 60000);
        NettyClient.connect("192.168.70.248", 60000);
    }
}
