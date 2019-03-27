package com.example.test.common.netty.download;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class HttpClientInitializer extends ChannelInitializer {
    @Override
    public void initChannel(Channel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpClientCodec());
        p.addLast(new HttpContentDecompressor());//这里要添加解压,不然打印时会乱码
        p.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));//添加HttpObjectAggregator, HttpClientMsgHandler才会收到FullHttpResponse
        p.addLast(new HttpClientMsgHandler());
    }

}
