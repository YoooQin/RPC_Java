# RPC Mine

一个基于 Java 实现的简单、可扩展的 RPC 框架，支持 Netty 和 Socket 通信。

## ✨ 核心特性

- **多种通信方式**：支持 Netty (NIO) 和 Socket (BIO) 两种底层通信实现。
- **服务注册与发现**：集成 ZooKeeper 作为服务注册中心，客户端可自动发现服务。
- **动态代理**：客户端通过动态代理无感调用远程服务，简化开发。
- **负载均衡**：内置一致性哈希、轮询、随机等多种负载均衡策略。
- **可扩展序列化**：支持 JSON 序列化（可轻松扩展至 Protobuf、Kryo 等）。
- **重试机制**：为关键服务提供基于 Guava-Retrying 的失败重试能力。
- **服务缓存**：客户端会缓存服务地址，提升性能并降低注册中心压力。

## 🛠️ 技术栈

- **核心框架**: Java 21, Netty, ZooKeeper, Curator
- **序列化**: FastJSON
- **依赖管理**: Maven
- **辅助工具**: Lombok, SLF4J + Log4j2, Guava-Retrying

## 📂 项目结构

```
src/main/java/
├── Client/                # 客户端模块
│   ├── proxy/            # 动态代理
│   ├── cache/            # 本地服务缓存
│   ├── serviceCenter/    # 服务发现与负载均衡
│   ├── netty/            # Netty 客户端实现
│   └── rpcClient/        # RPC 客户端接口与实现
├── Server/                # 服务端模块
│   ├── provider/          # 服务提供者
│   ├── serviceRegister/   # 服务注册
│   ├── server/           # RPC 服务器接口与实现
│   └── netty/            # Netty 服务端实现
└── common/                 # 公共模块
    ├── Message/           # RPC 消息定义
    ├── pojo/              # 数据实体
    ├── service/           # 服务接口
    └── serializer/        # 序列化接口与实现
```

## 🚀 快速开始

### 1. 环境配置
- **Java**: JDK 21 或更高版本
- **Maven**: 3.9 或更高版本
- **Git**
- **ZooKeeper**: 3.7+
  - **安装与启动**: 推荐使用 Homebrew 进行管理：
    ```bash
    brew install zookeeper
    brew services start zookeeper
    ```
  - **验证**: 启动后，请确保 ZooKeeper 正在 `2181` 端口监听。
    ```bash
    # 此命令应返回 "succeeded!"
    nc -zv localhost 2181
    ```
  - **注意**: 默认的 ZooKeeper 配置可能会额外占用 `8080` 端口。本 RPC 服务端默认在 `9999` 端口运行以避免冲突。如果遇到其他端口占用问题，请先使用 `lsof -i:<端口号>` 命令排查。

### 2. 克隆与构建
```bash
# 克隆项目
git clone https://github.com/YoooQin/RPC_Java.git
cd RPC_Java

# 使用 Maven 编译并打包项目
mvn clean install
```
> 如果在编译过程中遇到依赖问题，请检查您的 Maven 配置和网络。

### 3. 运行项目
**重要提示**: 服务端和客户端需要在 **两个独立的终端窗口** 中启动和运行。

**窗口 1: 启动服务端**
```bash
# 此命令会启动 Netty 服务端，它会持续运行并等待客户端连接
mvn exec:java -Dexec.mainClass="Server.TestServer"
```
> 服务端成功启动后，你会在日志中看到 **"netty服务端启动了"**。此时，`UserService` 已被注册到 ZooKeeper，等待客户端调用。

**窗口 2: 运行客户端**
由于 `TestClient.java` 已移除，您需要自行创建一个包含 `main` 方法的客户端类来调用RPC服务。例如，创建一个 `com.your.package.YourClient` 类，然后执行：
```bash
# 将 "com.your.package.YourClient" 替换为您的客户端主类
mvn exec:java -Dexec.mainClass="com.your.package.YourClient"
```
> 客户端成功运行后，您会在其控制台看到服务调用的输出，表明整个 RPC 调用已成功。

## 📝 待办列表

- [x] 服务注册中心（ZooKeeper）
- [x] 多种负载均衡策略
- [x] 失败重试机制
- [ ] 服务熔断与降级
- [ ] 异步调用支持
- [ ] 连接池优化
- [ ] 心跳检测
- [ ] 引入更高性能的序列化方案（如 Protobuf, Kryo） 