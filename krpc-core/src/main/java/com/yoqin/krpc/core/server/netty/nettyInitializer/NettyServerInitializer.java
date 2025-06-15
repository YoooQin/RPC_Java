package com.yoqin.krpc.core.server.netty.nettyInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import com.yoqin.krpc.core.server.provider.ServiceProvider;
import com.yoqin.krpc.core.server.netty.handler.NettyRpcServerHandler;
import com.yoqin.krpc.common.serializer.myCode.MyDecoder;
import com.yoqin.krpc.common.serializer.myCode.MyEncoder;
import com.yoqin.krpc.common.serializer.mySerializer.JsonSerializer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //使用自定义的编/解码器
        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new MyDecoder());
        pipeline.addLast(new NettyRpcServerHandler(serviceProvider));
    }
}