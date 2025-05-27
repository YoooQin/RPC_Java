package Server.server.impl;

import lombok.AllArgsConstructor;
import Server.server.RpcServer;
import Server.provider.ServiceProvider;
import java.net.ServerSocket;
import java.net.Socket;
import Server.server.work.WorkThread;
import java.io.IOException;

@AllArgsConstructor
public class SimpleRPCServer implements RpcServer{
    private ServiceProvider serviceProvider;//本地注册中心
  
    @Override
    public void start(int port) {
        try{
            //创建一个socket实例，用于在指定端口监听客户端的连接请求
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务器启动成功");
            //服务端持续接受客户端的连接请求
            while(true){
                //等待客户端连接（若无连接，则在此阻塞）
                Socket socket = serverSocket.accept();
                //创建一个线程处理客户端请求
                new Thread(new WorkThread(socket,serviceProvider)).start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }        
    }
    @Override
    public void stop(){
        //TODO：未来补充
    }
}
