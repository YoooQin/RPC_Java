package Client.proxy;

import common.service.UserService;
import common.pojo.User;

public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        //创建ClientProxy对象，并将其连接到服务端
        ClientProxy clientProxy = new ClientProxy();
        //创建代理对象
        UserService userService = clientProxy.getProxy(UserService.class);
        User user = userService.getUserByUserId(1);
        System.out.println("从服务端得到的user="+user.toString());
        User u = User.builder().id(100).userName("wxx").sex(true).build();
        Integer id = userService.insertUserId(u);
        System.out.println("向服务端插入的user的id="+id);
    }
}
