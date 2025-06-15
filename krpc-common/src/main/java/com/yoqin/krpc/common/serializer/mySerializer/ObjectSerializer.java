package com.yoqin.krpc.common.serializer.mySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class ObjectSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = null;
        //创建一个内存中的输出流，用于存储序列化后的对象
        //ByteArrayOutputStream是一个可变大小的字节缓冲区，数据都会写入这个缓冲区中
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            //将对象转换成二进制数据，将对象数据写入缓冲区bos
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            //将对象写入输出流
            oos.writeObject(obj);
            //强制将缓冲区中的数据刷新到底层流bos中
            oos.flush();
            //将字节缓冲区中的数据转换为字节数组
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
    
    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        //将字节流包装为一个输入流
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try{
            //用ObjectInputStream包装输入流
            ObjectInputStream ois = new ObjectInputStream(bis);
            //从ois中读取对象
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public int getType() {
        return 0;
    }
}
