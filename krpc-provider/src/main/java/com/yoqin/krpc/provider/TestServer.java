package com.yoqin.krpc.provider;

import com.yoqin.krpc.core.server.provider.ServiceProvider;
import com.yoqin.krpc.core.server.server.RpcServer;
import com.yoqin.krpc.core.server.server.impl.NettyRpcServer;
//import Server.server.impl.SimpleRPCServer;
import com.yoqin.krpc.api.service.UserService;
import com.yoqin.krpc.provider.impl.UserServiceImpl;

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
