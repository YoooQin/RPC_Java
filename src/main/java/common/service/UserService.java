package common.service;

import common.pojo.User;

//客户端通过这个接口调用服务端的实现类
public interface UserService {
    //根据用户id查找用户信息
    User getUserByUserId(Integer id);
    //新增一个用户
    Integer insertUserId(User user);

}
