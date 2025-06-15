package com.yoqin.krpc.core.client.circuitBreaker;

import java.util.concurrent.atomic.AtomicInteger;

enum CircuitBreakerState {
    CLOSED,
    OPEN,
    HALF_OPEN
}

public class CircuitBreaker {
    //熔断器初始状态：关闭
    private CircuitBreakerState state = CircuitBreakerState.CLOSED;
    //失败请求计数
    private AtomicInteger failureCount = new AtomicInteger(0);
    //成功请求计数
    private AtomicInteger successCount = new AtomicInteger(0);
    //请求总数
    private AtomicInteger requestCount = new AtomicInteger(0);
    //失败阈值
    private final int failureThreshold;
    //半开状态下的成功率阈值
    private final double halfOpenSuccessRate;
    //重置时间周期
    private final long resetPeriod;
    //最后一次失败时间
    private long lastFailureTime = 0;

    //构造函数初始化熔断器参数
    public CircuitBreaker(int failureThreshold, double halfOpenSuccessRate, long resetPeriod) {
        this.failureThreshold = failureThreshold;
        this.halfOpenSuccessRate = halfOpenSuccessRate;
        this.resetPeriod = resetPeriod;
    }

    //根据当前熔断器状态判断是否允许请求
    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        switch (state) {
            case OPEN:
                if (currentTime - lastFailureTime > resetPeriod) {
                    state = CircuitBreakerState.HALF_OPEN;
                    //重置计数
                    resetCounts();
                    return true;
                }
                return false;
            case HALF_OPEN:
                //半开状态下记录请求
                requestCount.incrementAndGet();
                return true;
            case CLOSED:
            default:
                return true;
        }
    }

    //记录成功请求
    public void recordSuccess() {
        if (state == CircuitBreakerState.HALF_OPEN) {
            successCount.incrementAndGet();
            if(successCount.get() > halfOpenSuccessRate * requestCount.get()) {
                //熔断器状态从半开转为关闭
                state = CircuitBreakerState.CLOSED;
                //重置计数
                resetCounts();
            }
        }
        else{
            //重置计数
            resetCounts();
        }
    }

    //记录失败请求
    public void recordFailure() {
        //增加失败次数
        failureCount.incrementAndGet();
        //更新最后失败时间
        lastFailureTime = System.currentTimeMillis();
        //半开状态下，熔断器状态从半开转为打开
        if(state == CircuitBreakerState.HALF_OPEN){
            state = CircuitBreakerState.OPEN;
        }
        else if(failureCount.get() >= failureThreshold){
            //失败的次数到达阈值，熔断器状态从关闭转为打开
            state = CircuitBreakerState.OPEN;
        }
    }

    //重置计数
    private void resetCounts() {
        failureCount.set(0);
        successCount.set(0);
        requestCount.set(0);
    }

    //获取熔断器状态
    public CircuitBreakerState getState() {
        return state;
    }
}
