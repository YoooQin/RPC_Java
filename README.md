# RPC Mine

一个基于 Java 实现的简单 RPC 框架，支持多种通信方式（Socket/Netty）。

## 技术栈

- Java 21
- Maven 3.9
- Netty 4.1.107.Final
- ZooKeeper 3.7.2
- Curator 5.6.0
- Lombok
- SLF4J + Log4j2
- FastJSON 1.2.83
- Guava Retrying 2.0.0

## 项目结构

```
src/main/java/
├── common/                 # 公共模块
│   ├── Message/           # RPC 消息定义（请求/响应）
│   ├── pojo/              # 数据对象
│   ├── service/           # 服务接口定义
│   └── serializer/        # 序列化相关实现
│       ├── myCode/        # 自定义编解码器
│       └── mySerializer/  # 序列化器实现
├── Server/                # 服务端模块
│   ├── provider/          # 服务提供者（服务注册）
│   ├── serviceRegister/   # ZooKeeper 服务注册实现
│   ├── server/           # 服务器实现
│   │   ├── impl/         # 具体服务器实现类（Simple/ThreadPool/Netty）
│   │   └── work/         # 工作线程处理
│   ├── netty/            # Netty 服务端实现
│   │   ├── handler/      # Netty 处理器
│   │   └── nettyInitializer/ # Netty 初始化器
│   └── TestServer.java   # 服务端测试类
└── Client/                # 客户端模块
    ├── proxy/            # 动态代理实现
    ├── cache/            # 本地服务缓存
    ├── serviceCenter/    # 服务发现实现
    │   ├── ZkWatcher/    # ZooKeeper 监听器
    │   └── balance/      # 负载均衡实现
    ├── netty/            # Netty 客户端实现
    │   ├── handler/      # Netty 处理器
    │   └── nettyInitializer/ # Netty 初始化器
    ├── rpcClient/        # RPC 客户端实现（Simple/Netty）
    └── TestClient.java   # 客户端测试类
```

## 核心功能

- 基于动态代理的远程调用
- 支持多种服务器实现
  - 简单 Socket 服务器
  - 线程池 Socket 服务器
  - Netty 服务器
- 支持多种客户端实现
  - 简单 Socket 客户端
  - Netty 客户端
- 服务注册与发现
  - 基于 ZooKeeper 的服务注册中心
  - 本地服务缓存
  - ZooKeeper 事件监听
  - 多种负载均衡策略
    - 一致性哈希
    - 随机
    - 轮询
- 网络通信
  - 基于 Socket 的 BIO 实现
  - 基于 Netty 的 NIO 实现
- 序列化与反序列化
  - 自定义序列化接口
  - JSON 序列化（FastJSON）
  - 可扩展的序列化框架

## 开发环境要求

- JDK 21
- Maven 3.9+
- ZooKeeper 3.7.2

## 构建和运行

```bash
# 启动 ZooKeeper 服务
# 确保 ZooKeeper 已安装并运行在默认端口 2181

# 构建项目
mvn clean install

# 运行服务端（选择实现方式）
# Simple Socket 实现
mvn exec:java -Dexec.mainClass="Server.TestServer" -Dexec.args="simple"
# Netty 实现
mvn exec:java -Dexec.mainClass="Server.TestServer" -Dexec.args="netty"

# 运行客户端（选择实现方式）
# Simple Socket 实现
mvn exec:java -Dexec.mainClass="Client.proxy.TestClient" -Dexec.args="simple"
# Netty 实现
mvn exec:java -Dexec.mainClass="Client.proxy.TestClient" -Dexec.args="netty"
```

## 使用示例

1. 定义服务接口（在 common.service 包中）
2. 实现服务接口（在服务端）
3. 注册服务（使用 ServiceProvider，自动注册到 ZooKeeper）
4. 客户端通过代理调用远程服务，自动从 ZooKeeper 发现服务

## 待实现功能

- [x] 服务注册中心（ZooKeeper）
- [x] 负载均衡
- [x] 更多序列化方式支持（JSON）
- [x] 超时重试机制
- [ ] 服务熔断
- [ ] 异步调用
- [ ] 连接池管理
- [ ] 心跳检测
- [ ] 更多序列化方式支持（Protocol Buffers） 