package Server.server.work;

import lombok.AllArgsConstructor;
import java.net.Socket;
import Server.provider.ServiceProvider;
import org.example.krpc.common.message.RpcRequest;
import org.example.krpc.common.message.RpcResponse;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

@AllArgsConstructor
public class WorkThread implements Runnable{
    private Socket socket;//建立网络连接
    private ServiceProvider serviceProvider;//本地服务注册中心

    @Override
    public void run(){
        try{
            //将相应数据通过网络发送给客户端
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //从客户端网络中接收数据
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //读取从客户端传来的Request
            RpcRequest request = (RpcRequest) ois.readObject();
            //反射调用服务方法获取返回值
            RpcResponse response = getResponse(request);
            //向客户端写入相应数据
            oos.writeObject(response);
            oos.flush();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    //读取客户端请求，根据请求内容调用对应的请求方法，并返回处理的结果
    public RpcResponse getResponse(RpcRequest request){
        //得到服务名
        String interfaceName = request.getInterfaceName();
        //得到服务端相应服务实现类
        Object service = serviceProvider.getService(interfaceName);
        //得到服务端相应方法
        Method method = null;
        try{
            //得到服务端相应方法对象
            method = service.getClass().getMethod(request.getMethodName(), request.getParamsType());
            //调用服务端相应方法
            Object result = method.invoke(service, request.getParams());
            //返回服务端相应方法的返回值
            return RpcResponse.success(result);
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail();
        }
    }
}
