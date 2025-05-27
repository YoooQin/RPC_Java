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
│   ├── pojo/              # 数据对象
│   ├── Message/           # 消息定义
│   └── service/           # 服务接口
├── server/                # 服务端模块
│   ├── provider/          # 服务提供者
│   ├── server/           # 服务器实现
│   └── work/             # 工作线程
└── client/                # 客户端模块
    └── proxy/            # 代理实现
```

## 开发环境要求

- JDK 21
- Maven 3.9+
- ZooKeeper 3.7.2

## 构建和运行

```bash
# 构建项目
mvn clean install

# 运行服务端
# TODO: 添加运行说明

# 运行客户端
# TODO: 添加运行说明
``` 