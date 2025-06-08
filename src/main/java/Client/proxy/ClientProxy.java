package Client.proxy;

import java.lang.reflect.Proxy;
import Client.rpcClient.RpcClient;
import Client.rpcClient.Impl.NettyRpcClient;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import Client.rpcClient.Impl.SimpleSocketRpcClient;
import common.Message.RpcRequest;
import common.Message.RpcResponse;

public class ClientProxy implements InvocationHandler{
    //传入参数service接口的class对象，反射封装成一个request
    //传入不同的client(netty/simple)，调用公告接口sendRequest发送请求
    private RpcClient rpcClient;
    public ClientProxy(String host,int port, int choose){
        switch(choose){
            case 0:
                this.rpcClient = new NettyRpcClient(host,port);
                break;
            case 1:
                this.rpcClient = new SimpleSocketRpcClient(host,port);
        }
    }
    public ClientProxy(String host, int port){
        this.rpcClient = new NettyRpcClient(host,port);
    }
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
        RpcResponse response = rpcClient.sendRequest(request);
        return response.getData();
    }
    //动态生成一个实现制定接口的代理对象
    public <T> T getProxy(Class<T> clazz){
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
        return (T)proxy;
    }
}
