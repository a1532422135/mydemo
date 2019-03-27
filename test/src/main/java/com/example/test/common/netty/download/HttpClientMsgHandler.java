package com.example.test.common.netty.download;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
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
                    log.info("HEADER: " + name + " = " + value);
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
        log.info("文件长度为:{}",bytes.length);
        FileInputStream fileInputStream = new FileInputStream("aaaa.jpg");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
        log.info("文件长度为:{}", byteBuffer.array().length);
        fileInputStream.close();
        channel.close();
        HttpClient.map.put("1", response.content());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接{}被关闭了", ctx.channel());
    }
}
