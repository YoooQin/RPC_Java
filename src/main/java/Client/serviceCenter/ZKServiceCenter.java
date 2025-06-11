package Client.serviceCenter;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import Client.cache.serviceCache;
import Client.serviceCenter.ZkWatcher.WatchZk;
import Client.serviceCenter.balance.Impl.ConsistencyHashBalance;


import java.net.InetSocketAddress;
import java.util.List;

public class ZKServiceCenter implements serviceCenter {
    //curator提供的zookeeper客户端
    private CuratorFramework client;
    //zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";
    //serviceCache
    private serviceCache cache;

    //负责zookeeper客户端的初始化，并与zookeeper服务端建立连接
    public ZKServiceCenter() throws InterruptedException {
        //指数时间尝试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        //zookeeper的地址固定，不管是服务提供者还是消费者都要与之建立连接
        //sessionTimeoutms与zoo.cfg中的tickTime有关系
        //zk还会根据minSessionTimeout与maxSessionTimeout来设置会话的超时时间，默认分别为tickTime的2倍和20倍
        //使用心跳监控机制
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
        .sessionTimeoutMs(40000)
        .retryPolicy(policy).namespace(ROOT_PATH).build();
        this.client.start();
        System.out.println("zookeeper连接成功");
        this.cache = new serviceCache();
        //加入zookeeper事件监听器
        WatchZk watcher=new WatchZk(client,cache);
        watcher.watchToUpdate(ROOT_PATH);
    }
    //根据服务名返回地址
    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            //先从本地缓存中找
            List<String> serviceList=cache.getServiceFromCache(serviceName);
            //如果找不到，再去zookeeper中找
            //这种i情况基本不会发生，或者说只会出现在初始化阶段
            if(serviceList==null) {
                serviceList=client.getChildren().forPath("/" + serviceName);
            }
            // 负载均衡得到地址
            String address = new ConsistencyHashBalance().balance(serviceList);
            return parseAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //地址 -> XXX.XXX.XXX.XXX:port字符串
    private String getServiceAddress(InetSocketAddress serverAddress){
        return serverAddress.getHostName() +
        ":" + serverAddress.getPort();
    }
    private InetSocketAddress parseAddress(String address){
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
