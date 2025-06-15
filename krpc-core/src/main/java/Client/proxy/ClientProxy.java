package Client.proxy;

import java.lang.reflect.Proxy;
import Client.rpcClient.RpcClient;
import Client.rpcClient.Impl.NettyRpcClient;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.example.krpc.common.message.RpcRequest;
import org.example.krpc.common.message.RpcResponse;
import Client.serviceCenter.serviceCenter;
import Client.serviceCenter.ZKServiceCenter;
import Client.retry.guavaRetry;
import Client.circuitBreaker.CircuitBreakerProvider;
import Client.circuitBreaker.CircuitBreaker;

public class ClientProxy implements InvocationHandler{
    //传入参数service接口的class对象，反射封装成一个request
    //传入不同的client(netty/simple)，调用公告接口sendRequest发送请求
    private RpcClient rpcClient;
    private serviceCenter serviceCenter;
    private CircuitBreakerProvider circuitBreakerProvider;
    public ClientProxy() throws InterruptedException {
        this.rpcClient = new NettyRpcClient();
        this.serviceCenter = new ZKServiceCenter();
        this.circuitBreakerProvider = new CircuitBreakerProvider();
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
        //获取熔断器实例
        CircuitBreaker circuitBreaker = circuitBreakerProvider.getCircuitBreaker(method.getName());
        //判断熔断器是否通过请求
        if(!circuitBreaker.allowRequest()){
            //如果熔断器不通过请求，则抛出异常
            //throw new RuntimeException("服务熔断，请求被拒绝");
            return null;
        }
        //与服务端通信，将请求发送出去，并且接收服务端的响应
        //通过IOClient发送请求
        RpcResponse response;
        //后续添加逻辑：为保持幂等性，对白名单上的服务进行重试
        if(serviceCenter.checkRetry(request.getInterfaceName())){
            //调用retry包中的guavaRetry类进行重试
            response = new guavaRetry().sendServiceWithRetry(request, rpcClient);
        }
        else{//只调用一次
            response = rpcClient.sendRequest(request);
        }
        return response.getData();
    }
    //动态生成一个实现制定接口的代理对象
    public <T> T getProxy(Class<T> clazz){
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
        return (T)proxy;
    }
}
