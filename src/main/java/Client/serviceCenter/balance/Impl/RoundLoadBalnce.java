package Client.serviceCenter.balance.Impl;

import java.util.List;

import Client.serviceCenter.balance.LoadBalance;

//轮询 负载均衡
public class RoundLoadBalnce implements LoadBalance{
    private int choose = -1;
    @Override
    public String balance(List<String> addressList) {
        choose++;
        choose %= addressList.size();
        String address = addressList.get(choose);
        System.out.println("轮询负载均衡选择" + address + "服务器");
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
