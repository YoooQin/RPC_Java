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
├── Client/
│   ├── proxy/
│   ├── cache/
│   ├── serviceCenter/
│   ├── netty/
│   └── rpcClient/
├── Server/
│   ├── provider/
│   ├── serviceRegister/
│   ├── server/
│   └── netty/
└── common/
    ├── Message/
    ├── pojo/
    ├── service/
    └── serializer/
```

## 构建和运行

### 1. 环境准备
- 确保 JDK 21, Maven, 和 Git 已正确安装。
- 确保 ZooKeeper 服务已启动并监听在 `127.0.0.1:2181`。
  - **提示**: `brew services start zookeeper` 可用于启动。

### 2. 构建项目
```bash
mvn clean install
```

### 3. 运行
**注意**: 服务端和客户端需在不同终端窗口运行。

**启动服务端:**
```bash
# 此命令将启动 Netty 服务端
mvn exec:java -Dexec.mainClass="Server.TestServer"
```

**运行客户端:**
由于 `TestClient.java` 已移除，您需要自行创建一个包含 `main` 方法的客户端类来调用RPC服务。例如，创建一个 `com.your.package.YourClient` 类，然后执行：
```bash
# 将 "com.your.package.YourClient" 替换为您的客户端主类
mvn exec:java -Dexec.mainClass="com.your.package.YourClient"
```

## 待实现功能

- [x] 服务注册中心（ZooKeeper）
- [x] 负载均衡
- [x] 失败重试机制
- [ ] 服务熔断与降级
- [ ] 异步调用支持
- [ ] 连接池优化
- [ ] 心跳检测
- [ ] 引入更高性能的序列化方案（如 Protobuf, Kryo） 