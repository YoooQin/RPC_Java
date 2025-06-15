package Client.rpcClient;

import org.example.krpc.common.message.RpcRequest;
import org.example.krpc.common.message.RpcResponse;

public interface RpcClient {
    //定义底层的通信方法
    RpcResponse sendRequest(RpcRequest request);
}
