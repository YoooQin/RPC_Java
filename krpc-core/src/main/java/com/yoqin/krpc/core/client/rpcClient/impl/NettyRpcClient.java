package com.yoqin.krpc.core.client.rpcClient.impl;

import com.yoqin.krpc.core.client.rpcClient.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import com.yoqin.krpc.core.client.netty.nettyInitializer.NettyClientInitializer;
import io.netty.channel.ChannelFuture;
import com.yoqin.krpc.common.message.RpcRequest;
import com.yoqin.krpc.common.message.RpcResponse;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import com.yoqin.krpc.core.client.serviceCenter.ZKServiceCenter;
import com.yoqin.krpc.core.client.serviceCenter.serviceCenter;
import java.net.InetSocketAddress;

public class NettyRpcClient implements RpcClient{
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    private serviceCenter serviceCenter;
    public NettyRpcClient() throws InterruptedException {
        this.serviceCenter = new ZKServiceCenter();
    }
    // netty客户端初始化，重复使用，所以用static
    static{
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup) 
        .channel(NioSocketChannel.class)
        .handler(new NettyClientInitializer());
    }
    @Override
    public RpcResponse sendRequest(RpcRequest request){
        try{
            //从注册中心获取host,port
            InetSocketAddress address = serviceCenter.serviceDiscovery(request.getInterfaceName());
            String host = address.getHostName();
            int port = address.getPort();
            //创建一个ChannelFuture对象，代表这一个操作对象，sync方法表示阻塞直到connect完成
            ChannelFuture future = bootstrap.connect(host,port).sync();
            //channel表示一个连接的单位，类似socket
            Channel channel = future.channel();
            //将RpcRequest对象写入到Channel中
            channel.writeAndFlush(request);
            //阻塞等待，直到Channel关闭
            channel.closeFuture().sync();
            //阻塞地获得结果，通过给channel设计别名，获得特定名字下的channel中的内容（这个在handler中设置）
            //AttributeKey是线程隔离的，不会出现线程安全问题
            //当前场景下选择阻塞获得结果
            //其他场景也可以选择添加监听器的方式异步获取结果 channelFuture.addListener(new ChannelFutureListener(){});
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
            RpcResponse response = channel.attr(key).get();
            System.out.println("response: " + response);
            return response;
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }
}
