package org.example.krpc.common.serializer.myCode;

import io.netty.handler.codec.ByteToMessageDecoder;
import org.example.krpc.common.serializer.mySerializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.example.krpc.common.message.MessageType;
import java.util.List;

//ByteToMessageDecoder是Netty专门用来设计实现解码器的抽象类，将字节数组解码为对象
public class MyDecoder extends ByteToMessageDecoder{
    //它负责将传入的字节流转化为对象，并将解码后的对象传入到out中，给下一个handler处理
    //in是字节缓冲区，可以看作字节数组
    //out是解码后的对象列表
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //读取消息的类型
        short messageType = in.readShort();
        //判断消息的类型，如果并非RpcRequest或RpcResponse，则打印错误信息
        if(messageType != MessageType.REQUEST.getCode() && messageType != MessageType.RESPONSE.getCode()){
            System.out.println("暂时不支持这种消息类型");            
        }
        //读取序列化器的类型
        short serializerType = in.readShort();
        //获取对于的序列化器对象
        Serializer serializer = Serializer.getSerializerByCode(serializerType);
        if(serializer == null){
            throw new RuntimeException("不存在这种序列化器");
        }
        //读取消息的长度
        int length = in.readInt();
        byte[] bytes = new byte[length];
        //将消息序列化后存入bytes数组
        in.readBytes(bytes);
        //将bytes数组反序列化
        Object obj = serializer.deserialize(bytes, messageType);
        //将反序列化后的对象添加到out列表中
        out.add(obj);
    }
    
}
