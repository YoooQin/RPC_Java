package com.yoqin.krpc.core.client.serviceCenter.balance.impl;

import java.util.List;
import java.util.Random;

import com.yoqin.krpc.core.client.serviceCenter.balance.LoadBalance;

public class RandomLoadBalance implements LoadBalance{
    @Override
    public String balance(List<String> addressList) {
        Random random = new Random();
        int choose = random.nextInt(addressList.size());
        String address = addressList.get(choose);
        System.out.println("随机负载均衡选择" + address + "服务器");
        return address;
    }
    @Override
    public void addNode(String node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addNode'");
    }
    @Override
    public void delNode(String node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delNode'");
    }
}
