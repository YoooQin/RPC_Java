package com.yoqin.krpc.consumer;


import com.yoqin.krpc.core.client.proxy.ClientProxy;
import com.yoqin.krpc.api.pojo.User;
import com.yoqin.krpc.api.service.UserService;

public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        ClientProxy clientProxy=new ClientProxy();
        UserService proxy=clientProxy.getProxy(UserService.class);
        for(int i = 0; i < 120; i++) {
            Integer i1 = i;
            if (i%30==0) {
                Thread.sleep(10000);
            }
            new Thread(()->{
                try{
                    User user = proxy.getUserByUserId(i1);

                    System.out.println("从服务端得到的user="+user.toString());

                    Integer id = proxy.insertUserId(User.builder().id(i1).userName("User" + i1.toString()).sex(true).build());
                    System.out.println("向服务端插入user的id"+id);
                } catch (NullPointerException e){
                    System.out.println("user为空");
                    e.printStackTrace();
                }
            }).start();
        }
    }
    //User user = proxy.getUserByUserId(1);
    //System.out.println("从服务端得到的user="+user.toString());
    //
    //User u=User.builder().id(100).userName("wxx").sex(true).build();
    //Integer id = proxy.insertUserId(u);
    //System.out.println("向服务端插入user的id"+id);
}
