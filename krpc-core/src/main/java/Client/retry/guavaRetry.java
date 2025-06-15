package Client.retry;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import Client.rpcClient.RpcClient;
import org.example.krpc.common.message.RpcRequest;
import org.example.krpc.common.message.RpcResponse;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.github.rholder.retry.RetryListener;
import com.github.rholder.retry.Attempt;

public class guavaRetry {
    //用来发送Rpc请求
    private RpcClient rpcClient;

    public RpcResponse sendServiceWithRetry(RpcRequest request, RpcClient rpcClient){
        this.rpcClient = rpcClient;
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
        .retryIfException()
        //重试会在请求发生异常或者返回500时进行重试
        .retryIfResult(rpcResponse -> Objects.equals(rpcResponse.getCode(), 500))
        //重试间隔时间：2s重试一次
        .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
        //重试次数：3次
        .withStopStrategy(StopStrategies.stopAfterAttempt(3))
        //重试监听器
        .withRetryListener(new RetryListener() {
            @Override
            public <V> void onRetry(Attempt<V> attempt) {
                System.out.println("RetryListener: 第" + attempt.getAttemptNumber() + "次调用");
            }
        })
        .build();
        try{
            return retryer.call(() -> rpcClient.sendRequest(request));
        }catch(Exception e){
            e.printStackTrace();
        }
        return RpcResponse.fail();
    }
}