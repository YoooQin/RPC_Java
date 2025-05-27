package Client.proxy;

import java.lang.reflect.Proxy;

import Client.IOClient;
import lombok.AllArgsConstructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import common.Message.RpcRequest;
import common.Message.RpcResponse;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler{
    //传入参数service接口的class对象，反射封装成一个request
    //初始化代理类时，传入服务端的ip和端口号
    private String host;
    private int port;
    @Override
    public Object invoke(Object proxy,Method method,Object[] args) throws Throwable{
        //构建request
        RpcRequest request = RpcRequest.builder()
        .interfaceName(method.getDeclaringClass().getName())
        .methodName(method.getName())
        .params(args)
        .paramsType(method.getParameterTypes())
        .build();
        //与服务端通信，将请求发送出去，并且接收服务端的响应
        //通过IOClient发送请求
        RpcResponse response = IOClient.sendRequest(host,port,request);
        return response.getData();
    }
    //动态生成一个实现制定接口的代理对象
    public <T> T getProxy(Class<T> clazz){
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
        return (T)proxy;
    }
}
