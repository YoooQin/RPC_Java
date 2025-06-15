package Server.ratelimit.provider;

import java.util.HashMap;
import java.util.Map;

import Server.ratelimit.RateLimit;
import Server.ratelimit.Impl.TokenBucketRateLimitImpl;

public class RateLimitProvider {
    //存储接口与对应的速率限制器的映射
    private Map<String, RateLimit> rateLimitMap = new HashMap<>();

    //根据接口名称去获取对应的速率限制器实例，若不存在，则创建一个
    public RateLimit getRateLimit(String interfaceName){
        //检查速率限制器是否存在
        if(!rateLimitMap.containsKey(interfaceName)){
            //若不存在，则创建一个（使用令牌桶算法进行限流）
            RateLimit rateLimit = new TokenBucketRateLimitImpl(100, 10);
            //存储到HashMap中
            rateLimitMap.put(interfaceName, rateLimit);
            return rateLimit;
        }
        //若存在，则直接返回
        return rateLimitMap.get(interfaceName);
    }
}
