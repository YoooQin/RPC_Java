package Server.ratelimit.Impl;

import Server.ratelimit.RateLimit;

public class TokenBucketRateLimitImpl implements RateLimit{
    //令牌产生速率
    private static int RATE;
    //令牌桶容量
    private static int CAPACITY;
    //当前桶容量
    private volatile int curCAPACITY;
    //上次请求的时间戳(可以理解为向令牌桶中添加令牌的时间)
    private volatile long timeStamp = System.currentTimeMillis();

    public TokenBucketRateLimitImpl(int rate, int capacity){
        RATE = rate;
        CAPACITY = capacity;
        curCAPACITY = CAPACITY;
    }

    @Override
    public synchronized boolean getToken(){
        //如果桶内有令牌，直接取出一个令牌并返回true
        if(curCAPACITY > 0){
            curCAPACITY--;
            return true;
        }
        //如果桶内没有令牌，则计算需要等待的时间
        long current = System.currentTimeMillis();
        //判断自上次获取令牌以来是否过去足够时间
        if(current - timeStamp >= RATE){
            //计算经过时间内生成了多少个令牌
            if((current - timeStamp) / RATE >= 2){
                //若生成的令牌数大于2，则加入(当前时间 - 上次时间) / RATE - 1 个令牌
                curCAPACITY += (current - timeStamp) / RATE - 1;
            }

            //保证令牌桶的总令牌量不大于最大容量 CAPACITY
            if(curCAPACITY > CAPACITY){
                curCAPACITY = CAPACITY;
            }

            //更新时间戳为当期时间
            timeStamp = current;
            return true;
        }
    
        //如果桶内没有令牌，则返回false
        return false;
    }
    
}
