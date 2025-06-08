package Client.rpcClient;

import common.Message.RpcRequest;
import common.Message.RpcResponse;

public interface RpcClient {
    //定义底层的通信方法
    RpcResponse sendRequest(RpcRequest request);
}
