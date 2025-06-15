package Client.serviceCenter.balance.Impl;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import Client.serviceCenter.balance.LoadBalance;

public class ConsistencyHashBalance implements LoadBalance{
    //虚拟节点数量
    private static final int VIRTUAL_NUM = 5;
    //保存虚拟节点的hash值和对应的虚拟节点，key为hash值，value为虚拟节点
    private SortedMap<Integer, String> shards = new TreeMap<>();
    //真实节点列表
    private List<String> realNodes = new LinkedList<>();
    //模拟虚拟服务器
    private String[] servers = null;
    //初始化负载均衡器，将真实的服务节点和对应的虚拟节点加入到哈希环中
    public void init(List<String> serviceList){
        for(String server : serviceList){
            realNodes.add(server);
            System.out.println("真实节点" + server + "加入负载均衡器");
            //遍历真serviceList(真实节点)，每个真实节点都会生成VIRTUAL_NUM个虚拟节点，并计算他们的哈希值
            for(int i = 0; i < VIRTUAL_NUM; i++){
                //虚拟节点的命名规则是：真实节点+&&VN+虚拟节点序号(i)
                String virtualNode = server + "&&VN" + i;
                //计算虚拟节点的哈希值
                int hash = getHash(virtualNode);
                //将虚拟节点加入到哈希环中
                shards.put(hash, virtualNode);
                System.out.println("虚拟节点：[" + virtualNode + "] hash：" + hash + " 被加入负载均衡器");
            }
        }
    }
    //根据请求的node(比如某个请求的标识符)，选择一个服务器节点
    public String getServer(String node, List<String> serviceList){
        //首先调用init(serviceList)初始化哈希环
        init(serviceList);
        //通过getHash(node)计算请求的node的哈希值
        int hash = getHash(node);
        Integer key = null;
        //使用shards.tailMap(hash)获取大于等于hash的子集
        SortedMap<Integer, String> subMap = shards.tailMap(hash);
        //如果子集为空，则选择shards的第一个节点
        if(subMap.isEmpty()){
            key = shards.firstKey();
        }
        else{
            key = subMap.firstKey();
        }
        //根据key获取对应的虚拟节点
        String virtualNode = shards.get(key);
        //返回真实节点
        return virtualNode.substring(0, virtualNode.indexOf("&&VN"));
    }
    //添加真实节点以及虚拟节点到哈希环中
    public void addNode(String node){
        if(!realNodes.contains(node)){
            realNodes.add(node);
            System.out.println("真实节点[" + node + "] 上线添加");
            for(int i = 0; i < VIRTUAL_NUM; i++){
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.put(hash, virtualNode);
                System.out.println("虚拟节点：[" + virtualNode + "] hash：" + hash + " 被加入负载均衡器");
            }
        }
    }
    //删除真实节点以及虚拟节点
    public void delNode(String node){
        if(realNodes.contains(node)){
            realNodes.remove(node);
            System.out.println("真实节点[" + node + "] 下线删除");
            for(int i = 0; i < VIRTUAL_NUM; i++){
                String virtualNode = node + "&&VN" + i;
                int hash = getHash(virtualNode);
                shards.remove(hash);
                System.out.println("虚拟节点：[" + virtualNode + "] hash：" + hash + " 被删除");
            }
        }
    }
    //FNV1_32_HASH算法计算哈希值
    public int getHash(String str){
        final int p = 16777619;
        int hash = (int)2166136261L;
        for(int i = 0; i < str.length(); i++){
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        //如果hash值为负数，则取绝对值
        if(hash < 0){
            hash = Math.abs(hash);
        }
        return hash;
    }
    //模拟负载均衡，通过生成一串随机字符串(random)来模拟请求，最终通过一致性哈希选择服务器
    @Override
    public String balance(List<String> addressList) {
        String random = UUID.randomUUID().toString();
        return getServer(random, addressList);
    }
}
