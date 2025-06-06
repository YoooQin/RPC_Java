package Server;

import Server.provider.ServiceProvider;
import Server.server.RpcServer;
import Server.server.impl.SimpleRPCServer;
import common.service.UserService;
import common.service.impl.UserServiceImpl;


public class TestServer {
    public static void main(String[] args) {
        //创建一个本地服务注册中心
        ServiceProvider serviceProvider = new ServiceProvider();
        //创建一个服务实例
        UserService userService = new UserServiceImpl();
        //将服务实例注册到服务提供者中
        serviceProvider.provideServiceInterface(userService);
        //创建一个服务器实例
        RpcServer rpcServer = new SimpleRPCServer(serviceProvider);
        //启动服务器
        rpcServer.start(9999);
    }
    
}
