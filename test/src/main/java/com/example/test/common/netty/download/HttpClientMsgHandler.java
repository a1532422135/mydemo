package com.example.test.common.netty.download;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class HttpClientMsgHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {
        if (!response.headers().isEmpty()) {
            for (CharSequence name : response.headers().names()) {
                for (CharSequence value : response.headers().getAll(name)) {
                    System.err.println("HEADER: " + name + " = " + value);
                }
            }
            System.err.println();
        }
        byte[] bytes = new byte[response.content().readableBytes()];
        response.content().readBytes(bytes);
        FileChannel fc = new FileOutputStream("aaaa.jpg").getChannel();
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        fc.write(bb);
        fc.close();
        FileOutputStream fileOutputStream = new FileOutputStream("bbbb.jpg");
        fileOutputStream.write(bytes);
        fileOutputStream.close();
        HttpClient.map.put("1", response.content());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("连接被关闭了");
    }
}
