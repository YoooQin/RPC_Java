# RPC Mine

一个基于 Java 实现的简单 RPC 框架。

## 技术栈

- Java 21
- Maven 3.9
- Netty 4.1.107.Final
- ZooKeeper 3.7.2
- Curator 5.6.0
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
│   │   ├── impl/         # 具体服务器实现类
│   │   └── work/         # 工作线程处理
│   └── TestServer.java   # 服务端测试类
└── Client/                # 客户端模块
    ├── proxy/            # 动态代理实现
    ├── IOClient.java     # 客户端网络通信
    └── TestClient.java   # 客户端测试类
```

## 核心功能

- 基于动态代理的远程调用
- 支持多种服务器实现（简单服务器/线程池服务器）
- 服务注册与发现
- 网络通信（基于 Socket）
- 序列化与反序列化

## 开发环境要求

- JDK 21
- Maven 3.9+
- ZooKeeper 3.7.2（用于服务注册与发现）

## 构建和运行

```bash
# 构建项目
mvn clean install

# 运行服务端
mvn exec:java -Dexec.mainClass="Server.TestServer"

# 运行客户端
mvn exec:java -Dexec.mainClass="Client.proxy.TestClient"
```

## 使用示例

1. 定义服务接口（在 common.service 包中）
2. 实现服务接口（在服务端）
3. 注册服务（使用 ServiceProvider）
4. 客户端通过代理调用远程服务

## 待实现功能

- [ ] 服务注册中心（ZooKeeper）
- [ ] 负载均衡
- [ ] 服务熔断
- [ ] 异步调用
- [ ] 更多序列化方式支持 