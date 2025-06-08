package Client.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import common.Message.RpcResponse;
import io.netty.util.AttributeKey;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse>{
    //用于读取服务端响应
    @Override
    protected void channelRead0(ChannelHandlerContext ctx,RpcResponse response) throws Exception{
        //接收到response，给Channel设计别名，让sendRequest方法读取response
        //将服务端返回的RpcResponse绑定到当前Channel的属性上，以便后续能够通过Channel获取response
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
        ctx.channel().attr(key).set(response);
        //关闭Channel(短连接模式)
        ctx.channel().close();
    }
    //用于捕获运行过程中出现的异常，进行处理并释放资源
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
}
