package org.example.krpc.common.serializer.mySerializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.krpc.common.message.RpcRequest;
import org.example.krpc.common.message.RpcResponse;

public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = JSONObject.toJSONBytes(obj);
        return bytes;
    }
    
    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        switch(messageType){
            case 0:
                //将字节数组转化为RpcRequest对象
                RpcRequest request = JSON.parseObject(bytes, RpcRequest.class);
                //创建一个obj数组，用于存储解析后的请求参数
                Object[] objects = new Object[request.getParamsType().length];
                //fastjson可以读出基本数据类型，不需要转化
                //对转化后的request的params属性进行类型判断
                for(int i = 0; i < request.getParamsType().length; i++){
                    Class<?> paramsType = request.getParamsType()[i];
                    //判断每个对象类型是否和paramsTypes中的一致
                    if (!paramsType.isAssignableFrom(request.getParams()[i].getClass())){
                        //如果不一致，就行进行类型转换
                        objects[i] = JSONObject.toJavaObject((JSONObject) request.getParams()[i],request.getParamsType()[i]);
                    }else{
                        //如果一致就直接赋给objects[i]
                        objects[i] = request.getParams()[i];
                    }
                }
                //将解析后的参数赋给request
                request.setParams(objects);
                obj = request;
                break;
            case 1:
                //将字节数组转化为RpcResponse对象
                RpcResponse response = JSON.parseObject(bytes, RpcResponse.class);
                //根据对象获取对象类型
                Class<?> dataType = response.getDataType();
                //判断转化后的response对象中的data的类型是否正确
                if(!dataType.isAssignableFrom(response.getData().getClass())){
                    response.setData(JSONObject.toJavaObject((JSONObject) response.getData(),dataType));
                }
                obj = response;
                break;
            default:
                System.out.println("暂时不支持这种消息类型");
                throw new RuntimeException();
        }
        return obj;
    }
    
    @Override
    public int getType() {
        return 1;
    }
}
