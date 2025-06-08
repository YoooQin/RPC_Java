package Server.netty.nettyInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import Server.provider.ServiceProvider;
import Server.netty.handler.NettyRpcServerHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //初始化，每一个SocketChannel都有一个独立的pipeline，用于定义数据的处理流程
        ChannelPipeline pipeline = ch.pipeline();
        //消息格式[长度][消息体]，解决沾包问题
        /*
         * 参数含义：
         * Integer.MAX_VALUE：允许的最大帧长度
         * 0,4：表示字段的起始位置和字段长度
         * 0,4：去掉字段长度后，计算实际数据的偏移量
         */
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));  
        //计算当前待发送消息的长度，写入到前4个字节中
        pipeline.addLast(new LengthFieldPrepender(4));
        //编码器
        //使用Java序列化的方式，netty自带的解码编码器支持传输这种结构
        pipeline.addLast(new ObjectEncoder());
        //解码器
        //使用Netty中的ObjectDecoder，用于将字节流解码为Java对象
        pipeline.addLast(new ObjectDecoder(new ClassResolver(){
            @Override
            public Class<?> resolve(String className) throws ClassNotFoundException {
                return Class.forName(className);
            }
        }));
        //将NettyRpcServerHandler添加到pipeline中，负责处理客户端发送的RpcRequest
        pipeline.addLast(new NettyRpcServerHandler(serviceProvider));
    }
}
