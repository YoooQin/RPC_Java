package common.Message;

import java.io.Serializable;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class RpcResponse implements Serializable{
    //状态信息
    private Integer code;
    private String message;
    //返回的数据
    private Object data;
    //构造成功信息
    public static RpcResponse success(Object data){
        return RpcResponse.builder()
        .code(200)
        .message("success")
        .data(data)
        .build();
    }
    //失败信息
    public static RpcResponse fail(Integer code,String message){
        return RpcResponse.builder()
        .code(500)
        .message("服务器发生错误")
        .build();
    }
}    