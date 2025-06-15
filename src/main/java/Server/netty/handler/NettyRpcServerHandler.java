package Server.netty.handler;

import lombok.AllArgsConstructor;
import common.Message.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import Server.provider.ServiceProvider;
import common.Message.RpcResponse;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import Server.ratelimit.RateLimit;

@AllArgsConstructor
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest>{
    private ServiceProvider serviceProvider;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        //接受request，读取并调用服务
        RpcResponse response = getResponse(request);
        ctx.writeAndFlush(response);
        ctx.close();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    private RpcResponse getResponse(RpcRequest request){
        //获取服务名
        String interfaceName = request.getInterfaceName();
        //获取速率限制器
        RateLimit rateLimit = serviceProvider.getRateLimitProvider().getRateLimit(interfaceName);
        //判断是否需要限流
        if(!rateLimit.getToken()){
            //若获取令牌失败，则进行限流
            System.out.println("服务限流！");
            return RpcResponse.fail();
        }
        //获取服务端服务名对应的实现类
        Object service = serviceProvider.getService(interfaceName);
        //反射调用方法
        Method method = null;
        try{
            method = service.getClass().getMethod(request.getMethodName(), request.getParamsType());
            Object invoke = method.invoke(service, request.getParams());
            return RpcResponse.success(invoke);
        }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail();
        }
    }
}
