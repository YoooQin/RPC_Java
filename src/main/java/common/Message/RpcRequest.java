package common.Message;

import java.io.Serializable;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class RpcRequest implements Serializable{
    //服务类名，客户端只知道接口名，在服务端接口指向实现类
    private String className;
    //调用的方法名
    private String methodName;
    //传入的参数
    private Object[] params;
    //返回值类型
    private Class<?> returnType;

}
