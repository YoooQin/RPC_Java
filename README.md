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

### 1. 环境准备

- **JDK 21** 或更高版本
- **Maven 3.9** 或更高版本
- **ZooKeeper 3.7+**
  - 请确保 ZooKeeper 服务已启动，并监听在 `127.0.0.1:2181`。
  - 如果您使用 Homebrew, 可通过 `brew services start zookeeper` 启动。

### 2. 启动服务端

```bash
# 克隆项目
git clone https://github.com/your-username/RPC_mine.git
cd RPC_mine

# 编译并打包
mvn clean install

# 启动服务端 (Netty)
mvn exec:java -Dexec.mainClass="Server.TestServer" -Dexec.args="netty"
```

服务端启动后，会将 `UserService` 注册到 ZooKeeper。

### 3. 创建并运行客户端

由于 `TestClient.java` 已被移除，您可以创建自己的测试客户端来调用服务。

1.  在 `src/main/java` 目录下创建一个新的测试包，例如 `Test`。
2.  在包内创建一个新的Java类, 例如 `MyTestClient.java`。
3.  将以下代码复制到 `MyTestClient.java` 中：

    ```java
    package Test;

    import Client.proxy.ClientProxy;
    import common.pojo.User;
    import common.service.UserService;

    public class MyTestClient {
        public static void main(String[] args) throws InterruptedException {
            // 1. 创建代理
            ClientProxy clientProxy = new ClientProxy();
            UserService userService = clientProxy.getProxy(UserService.class);

            // 2. 调用服务
            // 查询用户
            User user = userService.getUserByUserId(1);
            System.out.println("✅ 成功从服务端获取用户: " + user);

            // 插入用户
            User newUser = User.builder().id(100).userName("yoooq").sex(true).build();
            Integer id = userService.insertUserId(newUser);
            System.out.println("✅ 成功向服务端插入用户，ID为: " + id);
        }
    }
    ```

4.  运行 `MyTestClient`：

    ```bash
    # 在一个新的终端窗口中执行
    mvn exec:java -Dexec.mainClass="Test.MyTestClient"
    ```

您将会在客户端控制台看到服务调用的成功输出，同时在服务端控制台看到相应的日志。

## 📝 待办列表

- [x] 服务注册中心（ZooKeeper）
- [x] 多种负载均衡策略
- [x] 失败重试机制
- [ ] 服务熔断与降级
- [ ] 异步调用支持
- [ ] 连接池优化
- [ ] 心跳检测
- [ ] 引入更高性能的序列化方案（如 Protobuf, Kryo） 