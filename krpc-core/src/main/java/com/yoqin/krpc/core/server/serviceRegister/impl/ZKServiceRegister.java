package com.yoqin.krpc.core.server.serviceRegister.impl;

import com.yoqin.krpc.core.server.serviceRegister.ServiceRegister;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import java.net.InetSocketAddress;
import org.apache.zookeeper.CreateMode;

public class ZKServiceRegister implements ServiceRegister{
    //curator提供的zookeeper客户端
    private CuratorFramework client;
    //zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";
    private static final String RETRY = "CanRetry";
    //负责zookeeper客户端的初始化，并与zookeeper服务端建立连接
    public ZKServiceRegister(){
        //指数时间尝试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
        .sessionTimeoutMs(40000).retryPolicy(policy).namespace(ROOT_PATH).build();
        this.client.start();
        System.out.println("zookeeper连接成功");
    }
    //注册服务到注册中心
    @Override
    public void register(String serviceName, InetSocketAddress serviceAddress, boolean canRetry) {
        try{
            //serviceName创建成永久节点，服务下线时，不删服务名，只删地址
            if(client.checkExists().forPath("/" + serviceName) == null){
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }
            //serviceAddress创建成临时节点，服务下线时，自动删除
            String path = "/" + serviceName + "/" + getServiceAddress(serviceAddress);
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            //如果服务是幂等的，就添加到节点中
            if(canRetry){
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/" + RETRY + "/" + serviceName);
            }
        }catch(Exception e){
            System.out.println("此服务已存在");
        }
    }
    //地址 -> XXX.XXX.XXX.XXX:port字符串
    private String getServiceAddress(InetSocketAddress serverAddress){
        return serverAddress.getHostName() +
        ":" + serverAddress.getPort();
    }
    //字符串解析为地址
    private InetSocketAddress parseAddress(String address){
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
