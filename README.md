# RPC Mine

一个基于 Java 实现的简单 RPC 框架，支持多种通信方式（Socket/Netty）。

## 技术栈

- Java 21
- Maven 3.9
- Netty 4.1.107.Final
- ZooKeeper 3.7.2（计划中）
- Curator 5.6.0（计划中）
- Lombok
- SLF4J + Log4j2

## 项目结构

```
src/main/java/
├── common/                 # 公共模块
│   ├── Message/           # RPC 消息定义（请求/响应）
│   ├── pojo/              # 数据对象
│   └── service/           # 服务接口定义
├── Server/                # 服务端模块
│   ├── provider/          # 服务提供者（服务注册）
│   ├── server/           # 服务器实现
│   │   ├── impl/         # 具体服务器实现类（Simple/ThreadPool/Netty）
│   │   └── work/         # 工作线程处理
│   ├── netty/            # Netty 服务端实现
│   │   ├── handler/      # Netty 处理器
│   │   └── nettyInitializer/ # Netty 初始化器
│   └── TestServer.java   # 服务端测试类
└── Client/                # 客户端模块
    ├── proxy/            # 动态代理实现
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
- 服务注册与发现（计划中）
- 网络通信
  - 基于 Socket 的 BIO 实现
  - 基于 Netty 的 NIO 实现
- 序列化与反序列化（Java 原生序列化）

## 开发环境要求

- JDK 21
- Maven 3.9+
- ZooKeeper 3.7.2（计划中）

## 构建和运行

```bash
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
3. 注册服务（使用 ServiceProvider）
4. 客户端通过代理调用远程服务，可选择不同的实现方式

## 待实现功能

- [ ] 服务注册中心（ZooKeeper）
- [ ] 负载均衡
- [ ] 服务熔断
- [ ] 异步调用
- [ ] 更多序列化方式支持（Protocol Buffers/JSON）
- [ ] 连接池管理
- [ ] 心跳检测
- [ ] 超时重试机制 