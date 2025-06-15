package org.example.krpc.provider;

import Server.provider.ServiceProvider;
import Server.server.RpcServer;
import Server.server.impl.NettyRpcServer;
//import Server.server.impl.SimpleRPCServer;
import org.example.krpc.api.service.UserService;
import org.example.krpc.provider.impl.UserServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        //创建一个本地服务注册中心
        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 9999);
        //创建一个服务实例
        UserService userService = new UserServiceImpl();
        //将服务实例注册到服务提供者中
        serviceProvider.provideServiceInterface(userService, true);
        //创建一个服务器实例
        RpcServer rpcServer = new NettyRpcServer(serviceProvider);
        //启动服务器
        rpcServer.start(9999);
    }
    
}
