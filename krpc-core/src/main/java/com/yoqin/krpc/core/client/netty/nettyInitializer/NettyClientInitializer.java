package com.yoqin.krpc.core.client.netty.nettyInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.ChannelPipeline;
import com.yoqin.krpc.core.client.netty.handler.NettyClientHandler;
import com.yoqin.krpc.common.serializer.myCode.MyDecoder;
import com.yoqin.krpc.common.serializer.myCode.MyEncoder;
import com.yoqin.krpc.common.serializer.mySerializer.JsonSerializer;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //使用自定义的编/解码器
        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new NettyClientHandler());
    }
}