package com.yoqin.krpc.core.client.serviceCenter.ZkWatcher;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

import com.yoqin.krpc.core.client.cache.serviceCache;

public class WatchZk{
    //zookeeper客户端
    private CuratorFramework client;
    //服务缓存
    private serviceCache cache;
    public WatchZk(CuratorFramework client, serviceCache cache) {
        this.client = client;
        this.cache = cache;
    }
    //监听当前节点和子节点的更新、创建和删除
    public void watchToUpdate(String path) throws InterruptedException {
        //用于监视指定路径下的节点变化，并在节点变化时更新本地缓存
        //CuratorCache是Curator用于监听节点变化的API
        //他会监听指定路径节点变化，这里监听的是根路径/
        CuratorCache curatorCache = CuratorCache.build(client, "/");
        //注册一个监听器，用于处理节点变化事件
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            //第一个参数：事件类型（枚举）
            //第二个参数：节点更新前的状态、数据
            //第三个参数：节点更新后的状态、数据
            //创建节点时，节点刚被创建，故不存在第二个参数，为null
            //删除节点时，节点刚被删除，故不存在第三个参数，为null
            @Override
            public void event(Type type, ChildData childData, ChildData childData1) {
                switch(type.name()){
                    //节点创建
                    case "NODE_CREATED":
                        //获取节点路径
                        String[] pathList = parsePath(childData1);
                        if(pathList.length <= 2) break;
                        else{
                            String serviceName = pathList[1];
                            String serviceAddress = pathList[2];
                            cache.addServiceToCache(serviceName, serviceAddress);
                        }
                        break;
                    //节点更新
                    case "NODE_UPDATED":
                        if(childData != null){
                            System.out.println("修改前的数据" + new String(childData.getData()));
                        }
                        else{
                            System.out.println("节点第一次赋值");
                        }
                        String[] oldPath = parsePath(childData);
                        String[] newPath = parsePath(childData1);
                        cache.replaceServiceAddress(oldPath[1], oldPath[2], newPath[2]);
                        System.out.println("修改后的数据" + new String(childData1.getData()));
                        break;
                    //节点删除
                    case "NODE_REMOVED":
                        String[] pathList_d = parsePath(childData);
                        if(pathList_d.length <= 2) break;
                        else{
                            String serviceName = pathList_d[1];
                            String serviceAddress = pathList_d[2];
                            cache.deleteServiceFromCache(serviceName, serviceAddress);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        //启动监听
        curatorCache.start();
    }
    //解析路径
    private String[] parsePath(ChildData childData){
        String path = childData.getPath();
        String[] pathList = path.split("/");
        return pathList;
    }
}
