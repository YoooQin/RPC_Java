package com.yoqin.krpc.common.serializer.mySerializer;

/*
 * 用于为对象提供序列化和反序列化的功能
 * 通过静态工厂方法根据类型代码返回具体的序列化容器
 */
public interface Serializer {
    //把对象序列化为字节数组
    byte[] serialize(Object obj);
    //把字节数组反序列化为对象
    //如果用Java自带的，不用messageType也能得到对应对象
    //其他方法需要指定消息格式，再根据messageType转化成消息对象
    Object deserialize(byte[] bytes, int messageType);
    //返回使用的序列化器是哪个
    //0为Java自带的序列化器，1为Json序列化器
    int getType();
    //静态工厂方法
    //根据序号取出序列化器，暂时有两种实现方法，需要其他方式，实现这个接口即可
    static Serializer getSerializerByCode(int code){
        switch(code){
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
