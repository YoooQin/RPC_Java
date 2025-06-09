package Server.provider;

import Server.serviceRegister.ServiceRegister;
import Server.serviceRegister.Impl.ZKServiceRegister;

import java.util.HashMap;
import java.util.Map;
import java.net.InetSocketAddress;

//本地服务存放器
public class ServiceProvider {
    //借助map集中存放服务的实例，接口的全限定名(String)，接口的实现类的实例(Object)
    private Map<String,Object> interfaceProvider;
    private String host;
    private int port;
    //注册服务类
    private ServiceRegister serviceRegister;
    public ServiceProvider(String host,int port){
        //需要传入服务端自身的网络地址
        this.host=host;
        this.port=port;
        this.interfaceProvider=new HashMap<>();
        this.serviceRegister=new ZKServiceRegister();
    }
    //本地注册服务
    public void provideServiceInterface(Object service){//接收一个服务实例
        //获得服务实例的类名
        String serviceName = service.getClass().getName();
        //获得服务对象实现的所有接口
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for(Class<?> i : interfaces){
            //将接口的全限定名和实例传入map
            interfaceProvider.put(i.getName(),service);
            //注册服务
            serviceRegister.register(i.getName(),new InetSocketAddress(host,port));
        }
    }
    //获取服务实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
