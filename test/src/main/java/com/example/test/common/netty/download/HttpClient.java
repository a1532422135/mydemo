package com.example.test.common.netty.download;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpClient {
    public final static Logger logger = LoggerFactory.getLogger(HttpClient.class);
    public static final Map<String, ByteBuf> map = new ConcurrentHashMap<String, ByteBuf>();
    private static Bootstrap b;
    private static EventLoopGroup group;

    public static void main(String[] args) throws Exception {
        downloadByNetty("http://192.168.230.246:8093/test7.jpg", "a");
    }

    static {
        group = new NioEventLoopGroup();
        b = new Bootstrap();
    }

    private static void downloadByNetty(String url, String topic) throws Exception {
        URI uri = new URI(url);
        String host = uri.getHost();
        int port = uri.getPort();
        long time = System.currentTimeMillis();
        b.group(group).channel(NioSocketChannel.class).handler(new HttpClientInitializer());
        Channel ch = b.connect(host, port).sync().channel();
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
        ch.writeAndFlush(request);
        while (true) {
            if (map.get("1") != null) {
                break;
            }
        }
        System.out.println("用netty下载耗时:" + (System.currentTimeMillis() - time));
//        ch.closeFuture();
//        group.shutdownGracefully();
        System.err.println(ch.isActive() + "" + ch.isOpen());
    }
}
