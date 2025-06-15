package com.yoqin.krpc.core.client.rpcClient;

import com.yoqin.krpc.common.message.RpcRequest;
import com.yoqin.krpc.common.message.RpcResponse;

public interface RpcClient {
    //定义底层的通信方法
    RpcResponse sendRequest(RpcRequest request);
}
