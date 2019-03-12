package com.example.test.common.netty;

import com.example.test.common.netty.client.NettyClient;
import com.example.test.common.netty.server.NettyServer;

public class Netty {
    public static void main(String[] args) {
        NettyServer.start("192.168.70.248", 60000);
        NettyClient.connect("192.168.70.248", 60000);
    }
}
