package common.serializer.myCode;

import lombok.AllArgsConstructor;

import common.serializer.mySerializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import common.Message.RpcRequest;
import common.Message.RpcResponse;
import common.Message.MessageType;

//MessageToByteEncoder是Netty专门用来设计实现编码器的抽象类，将对象编码为字节数组
@AllArgsConstructor
public class MyEncoder extends MessageToByteEncoder<Object> {
    private Serializer serializer;
    //netty在写入数据的时候，会调用这个方法
    //ctx是管道上下文，包含通道和处理器等信息
    //msg是待编码的对象
    //out是字节缓冲区，用于存储编码后的数据
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //打印消息对象的类名，便于调试
        System.out.println(msg.getClass());
        //判断消息是RpcRequest还是RpcResponse
        if(msg instanceof RpcRequest){
            out.writeShort(MessageType.REQUEST.getCode());
        }else if(msg instanceof RpcResponse){
            out.writeShort(MessageType.RESPONSE.getCode());
        }
        //写入当前序列化器的类型
        out.writeShort(serializer.getType());
        //将消息对象序列化
        byte[] serializeBytes = serializer.serialize(msg);
        //写入消息对象的字节数组
        out.writeInt(serializeBytes.length);
        //将序列化后的字节数组写入缓冲区
        out.writeBytes(serializeBytes);
    }
}
