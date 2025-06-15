package Client.circuitBreaker;

import java.util.HashMap;
import java.util.Map;

public class CircuitBreakerProvider {

    //用一个Map存储每个服务的熔断器实例，key为服务名，value为熔断器实例
    private static final Map<String, CircuitBreaker> circuitBreakers = new HashMap<>();

    //根据服务名获取熔断器实例
    public static CircuitBreaker getCircuitBreaker(String serviceName) {
        CircuitBreaker circuitBreaker;
        if (circuitBreakers.containsKey(serviceName)) {
            circuitBreaker = circuitBreakers.get(serviceName);
        } else {
            System.out.println("serviceName = " + serviceName + " 创建一个新的熔断器实例");
            //创建一个新的熔断器实例，失败阈值为1，半开状态下的成功率阈值为0.5，重置时间周期为10000ms
            circuitBreaker = new CircuitBreaker(1, 0.5, 10000);
            circuitBreakers.put(serviceName, circuitBreaker);
        }
        
        return circuitBreaker;
    }
}
