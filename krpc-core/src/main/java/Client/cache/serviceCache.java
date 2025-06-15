package Client.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class serviceCache {
    //成员变量，存储服务名和地址类表
    private static final Map<String, List<String>> cache = new HashMap<>();
    //添加服务
    public void addServiceToCache(String serviceName, String serviceAddress){
        if(cache.containsKey(serviceName)){
            List<String> serviceAddresses = cache.get(serviceName);
            serviceAddresses.add(serviceAddress);
            System.out.println("将name为" + serviceName + "和地址为" + serviceAddress + "的服务添加到本地缓存中");
        }else{
            List<String> serviceAddresses = new ArrayList<>();
            serviceAddresses.add(serviceAddress);
            cache.put(serviceName, serviceAddresses);
        }
    }
    //修改服务
    public void replaceServiceAddress(String serviceName, String oldAddress, String newAddress){
        if(cache.containsKey(serviceName)){
            List<String> serviceAddresses = cache.get(serviceName);
            serviceAddresses.remove(oldAddress);
            serviceAddresses.add(newAddress);
            System.out.println("将name为" + serviceName + "的地址从" + oldAddress + "修改为" + newAddress);
        }else{
            System.out.println("修改失败，服务不存在");
        }
    }
    //从缓存中取服务地址
    public List<String> getServiceFromCache(String serviceName){
        if(!cache.containsKey(serviceName)){
            System.out.println("获取失败，服务不存在");
            return null;
        }
        List<String> serviceAddresses = cache.get(serviceName);
        return serviceAddresses;
    }
    //从缓存中删除服务地址
    public void deleteServiceFromCache(String serviceName, String serviceAddress){
        List<String> addressList = cache.get(serviceName);
        addressList.remove(serviceAddress);
        System.out.println("将name为" + serviceName + "和地址为" + serviceAddress + "的服务从本地缓存中删除");
    }
}
