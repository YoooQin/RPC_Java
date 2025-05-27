package Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

import common.Message.RpcResponse;

public class IOClient {
    //负责底层与服务端的通信，发送request，返回response
    public static RpcResponse sendRequest(String host,int port,Object request){//服务端的IP地址，端口号，请求对象
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
