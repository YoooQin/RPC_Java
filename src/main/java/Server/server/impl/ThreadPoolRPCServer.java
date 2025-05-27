package Server.server.impl;

import Server.server.RpcServer;
import Server.provider.ServiceProvider;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import Server.server.work.WorkThread;

public class ThreadPoolRPCServer implements RpcServer{
    private ServiceProvider serviceProvider;
    //定义一个线程池，用于管理和执行线程任务
    private final ThreadPoolExecutor threadPool;
    //默认构造函数：核心数等于CPU核心数，最大线程数1000，线程空闲时间60秒，队列长度1000
    public ThreadPoolRPCServer(ServiceProvider serviceProvider){
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
        1000,60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));
        this.serviceProvider = serviceProvider;
    }
    //自定义构造函数
    public ThreadPoolRPCServer(ServiceProvider serviceProvider,int corePoolSize,int maxPoolSize,long keepAliveTime,TimeUnit timeUnit,BlockingQueue<Runnable> workQueue){
        threadPool = new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveTime,timeUnit,workQueue);
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port){
        System.out.println("服务器启动了");
        try{
            //初始化ServerSocket，监听客户端端口
            ServerSocket serverSocket = new ServerSocket(port);
            while(true){
                //等待客户端连接，通过accept方法阻塞等待客户端连接请求，返回一个Socket对象
                Socket socket = serverSocket.accept();
                //将客户端请求提交给线程池
                threadPool.execute(new WorkThread(socket,serviceProvider));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop(){
        //TODO：未来补充
        threadPool.shutdown();
    }
}