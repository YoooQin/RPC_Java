package Server.provider;

import java.util.HashMap;
import java.util.Map;

//本地服务存放器
public class ServiceProvider {
    //借助map集中存放服务的实例，接口的全限定名(String)，接口的实现类的实例(Object)
    private Map<String,Object> interfaceProvider;
    public ServiceProvider(){
        this.interfaceProvider = new HashMap<>();
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
        }

    }
    //获取服务实例
    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }
}
