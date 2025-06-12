package Client.serviceCenter;

import java.net.InetSocketAddress;

public interface serviceCenter {
    //查询：根据服务名查找地址
    InetSocketAddress serviceDiscovery(String serviceName);
    //判断能否重试
    boolean checkRetry(String serviceName);
}