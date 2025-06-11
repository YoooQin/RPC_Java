package Server.netty.nettyInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import Server.provider.ServiceProvider;
import Server.netty.handler.NettyRpcServerHandler;
import common.serializer.myCode.MyDecoder;
import common.serializer.myCode.MyEncoder;
import common.serializer.mySerializer.JsonSerializer;
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