package Client.rpcClient.Impl;

import Client.rpcClient.RpcClient;
import org.example.krpc.common.message.RpcRequest;
import org.example.krpc.common.message.RpcResponse;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SimpleSocketRpcClient implements RpcClient{
    String host;//主机地址
    int port;//端口号
    public SimpleSocketRpcClient(String host,int port){
        this.host = host;
        this.port = port;
    }
    @Override
    public RpcResponse sendRequest(RpcRequest request){//请求对象
        try{
            //通过socket与服务端建立TCP连接
            Socket socket = new Socket(host,port);
            //通过socket获取输出流
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //将请求对象序列化后发送给服务端
            oos.writeObject(request);
            //刷新缓冲区
            oos.flush();
            //通过socket获取输入流
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //将响应对象反序列化后返回
            return (RpcResponse)ois.readObject();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
}
