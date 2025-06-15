# KRPC Framework

一个基于 Java 实现的企业级、可扩展的 RPC 框架，采用多模块架构设计，支持 Netty 和 Socket 通信。

## ✨ 核心特性

- **多种通信方式**：支持 Netty (NIO) 和 Socket (BIO) 两种底层通信实现
- **服务注册与发现**：集成 ZooKeeper 作为服务注册中心，客户端可自动发现服务
- **动态代理**：客户端通过动态代理无感调用远程服务，简化开发
- **负载均衡**：内置一致性哈希、轮询、随机等多种负载均衡策略
- **可扩展序列化**：支持 JSON 序列化（可轻松扩展至 Protobuf、Kryo 等）
- **重试机制**：为关键服务提供基于 Guava-Retrying 的失败重试能力
- **服务缓存**：客户端会缓存服务地址，提升性能并降低注册中心压力
- **服务限流**：基于令牌桶算法实现的服务限流，保护服务端不被过载
- **熔断机制**：集成熔断器，在服务异常时自动熔断，防止级联故障
- **多模块架构**：采用 Maven 多模块设计，模块职责清晰，便于维护和扩展

## 🛠️ 技术栈

- **核心框架**: Java 21, Netty, ZooKeeper, Curator
- **序列化**: FastJSON
- **依赖管理**: Maven 多模块架构
- **辅助工具**: Lombok, SLF4J + Log4j2, Guava-Retrying

## 📂 项目结构

### 多模块架构
```
rpc-mine/                    # 父模块
├── krpc-api/               # API 模块 - 服务接口和数据模型
│   └── com.yoqin.krpc.api/
│       ├── pojo/           # 数据实体类
│       └── service/        # 服务接口定义
├── krpc-common/            # 公共模块 - 消息和序列化
│   └── com.yoqin.krpc.common/
│       ├── message/        # RPC 消息定义
│       └── serializer/     # 序列化接口与实现
├── krpc-core/              # 核心模块 - RPC 框架核心功能
│   └── com.yoqin.krpc.core/
│       ├── client/         # 客户端实现
│       │   ├── proxy/      # 动态代理
│       │   ├── cache/      # 本地服务缓存
│       │   ├── serviceCenter/ # 服务发现与负载均衡
│       │   ├── netty/      # Netty 客户端实现
│       │   ├── rpcClient/  # RPC 客户端接口与实现
│       │   ├── circuitbreaker/ # 熔断器实现
│       │   └── retry/      # 重试机制
│       └── server/         # 服务端实现
│           ├── provider/   # 服务提供者
│           ├── serviceRegister/ # 服务注册
│           ├── server/     # RPC 服务器接口与实现
│           ├── netty/      # Netty 服务端实现
│           └── ratelimit/  # 服务限流实现
├── krpc-provider/          # 服务提供者示例
│   └── com.yoqin.krpc.provider/
│       ├── TestServer      # 服务端启动类
│       └── impl/           # 服务实现类
└── krpc-consumer/          # 服务消费者示例
    └── com.yoqin.krpc.consumer/
        └── TestClient      # 客户端测试类
```

### 模块依赖关系
```
krpc-provider → krpc-core → krpc-common → krpc-api
krpc-consumer → krpc-core → krpc-common → krpc-api
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
  - **注意**: 默认的 ZooKeeper 配置可能会额外占用 `8080` 端口。本 RPC 服务端默认在 `9999` 端口运行以避免冲突。

### 2. 克隆与构建
```bash
# 克隆项目
git clone https://github.com/YoooQin/RPC_Java.git
cd RPC_Java

# 使用 Maven 编译并打包所有模块
mvn clean compile
```

### 3. 运行项目
**重要提示**: 服务端和客户端需要在 **两个独立的终端窗口** 中启动和运行。

**窗口 1: 启动服务端**
```bash
# 启动 Netty 服务端，监听 9999 端口
mvn exec:java -pl krpc-provider -Dexec.mainClass="com.yoqin.krpc.provider.TestServer"
```
> 服务端成功启动后，你会在日志中看到 **"netty服务端启动了"** 和 **"zookeeper连接成功"**。

**窗口 2: 运行客户端**
```bash
# 运行客户端测试
mvn exec:java -pl krpc-consumer -Dexec.mainClass="com.yoqin.krpc.consumer.TestClient"
```
> 客户端成功运行后，您会看到服务调用的输出和性能测试结果。

## 🔧 配置说明

### 服务端配置
- **端口**: 默认 9999（可在 `TestServer.java` 中修改）
- **ZooKeeper**: 默认连接 `127.0.0.1:2181`
- **限流配置**: 令牌桶容量 100，每秒补充 10 个令牌

### 客户端配置
- **负载均衡**: 支持随机、轮询、一致性哈希
- **重试机制**: 最大重试 3 次，指数退避
- **熔断器**: 失败率阈值可配置

## 📊 性能特性

- **高并发**: 基于 Netty 的异步 I/O，支持高并发连接
- **低延迟**: 本地服务缓存，减少注册中心查询
- **高可用**: 熔断器 + 重试机制，保障服务稳定性
- **可扩展**: 模块化设计，易于扩展新功能

## 📝 开发计划

- [x] 服务注册中心（ZooKeeper）
- [x] 多种负载均衡策略
- [x] 失败重试机制
- [x] 服务熔断与降级
- [x] 服务限流
- [x] 多模块 Maven 架构
- [x] 统一包名规范
- [ ] 异步调用支持
- [ ] 连接池优化
- [ ] 心跳检测
- [ ] 引入更高性能的序列化方案（如 Protobuf, Kryo）
- [ ] 服务监控与指标收集
- [ ] 配置中心集成

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

本项目采用 MIT 许可证。 