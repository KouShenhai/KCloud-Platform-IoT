# KCloud-Platform-IoT 项目规则

## 项目概述
KCloud-Platform-IoT（老寇IoT云平台）是一个企业级微服务架构的云服务多租户IoT平台。

## 技术栈
- **JDK**: 25 (支持虚拟线程和GraalVM)
- **Spring Boot**: 4.0.1
- **Spring Cloud**: 2025.1.0
- **Spring Cloud Alibaba**: 2025.1.0.0
- **Spring Framework**: 7.0.3
- **Spring Security**: 7.0.2
- **Spring gRPC**: 1.0.1
- **Spring AI**: 1.1.2
- **MyBatis Plus**: 3.5.16
- **Vert.x**: 5.0.7
- **Nacos**: 3.1.1
- **Redis/Redisson**: 4.1.0
- **Elasticsearch**: 9.2.4
- **Netty**: 4.2.9.Final
- **Kafka**: 4.0.1
- **gRPC**: 1.78.0
- **Jackson**: 3.0.3
- **Testcontainers**: 2.0.3

## 项目架构

### COLA 架构分层
项目遵循阿里 COLA 应用框架，每个业务模块包含以下分层：
- **adapter**: 适配器层，包含 REST Controller、Web 接口
- **app**: 应用层，包含 Command/Query 执行器、事件处理器
- **client**: 客户端层，包含 DTO、API 接口定义
- **domain**: 领域层，包含聚合根、实体、领域事件、领域服务
- **infrastructure**: 基础设施层，包含数据库访问、外部服务集成
- **start**: 启动模块，包含 Spring Boot 启动类和配置

### 模块说明

#### laokou-common（通用模块）
| 模块 | 说明 |
|------|------|
| laokou-common-core | 核心工具类、异常、配置 |
| laokou-common-domain | 领域事件组件 |
| laokou-common-security | 认证授权组件 |
| laokou-common-mybatis-plus | 数据库访问组件 |
| laokou-common-redis | 缓存组件 |
| laokou-common-kafka | 消息队列组件 |
| laokou-common-elasticsearch | 搜索组件 |
| laokou-common-grpc | gRPC 通信组件 |
| laokou-common-lock | 分布式锁组件 |
| laokou-common-trace | 链路跟踪组件 |
| laokou-common-i18n | 国际化组件 |
| laokou-common-tenant | 多租户组件 |
| laokou-common-modbus4j | Modbus 协议组件 |
| laokou-common-coap | CoAP 协议组件 |
| laokou-common-tdengine | 时序数据库组件 |
| laokou-common-influxdb | InfluxDB 组件 |

#### laokou-cloud（云服务模块）
| 模块 | 说明 |
|------|------|
| laokou-gateway | API 网关 |
| laokou-monitor | 服务监控 |
| laokou-nacos | 服务治理 |

#### laokou-service（业务模块）
| 模块 | 说明 |
|------|------|
| laokou-admin | 后台管理模块 |
| laokou-auth | 认证授权模块 |
| laokou-iot | 物联网模块 |
| laokou-logstash | 日志收集模块 |
| laokou-generator | 代码生成模块 |
| laokou-report | 报表统计模块 |
| laokou-mcp | MCP 模块 |
| laokou-network | 网络模块 |

## 代码规范

### 命名规范
- **包名**: `org.laokou.模块名.分层名`，如 `org.laokou.admin.domain`
- **类名**: 使用大驼峰命名，如 `UserA`、`MenuController`
- **方法名**: 使用小驼峰命名
- **常量**: 使用全大写下划线分隔

### DDD 领域驱动设计
- **聚合根**: 以 `A` 或 `E` 结尾
- **值对象**: 使用不可变对象
- **领域事件**: 以 `Event` 结尾
- **领域服务**: 以 `DomainService` 结尾
- **仓储接口**: 以 `Repository` 结尾

### CQRS 模式
- **Command**: 修改数据的操作，以 `Cmd` 结尾
- **Query**: 查询数据的操作，以 `Qry` 结尾
- **Executor**: 执行器，以 `Exe` 结尾
- **Handler**: 处理器， 以 `Handler` 结尾

### REST API 规范
- 遵循 RESTFul 设计风格
- 使用 Swagger/OpenAPI 注解文档化
- 路径使用小写，单词间用 `-` 分隔
- 版本控制使用 `/v1/`、`/v2/` 前缀

### 异常处理
- 使用自定义异常类
- 全局异常处理器统一处理
- 返回标准 Result 响应格式

### 日志规范
- 使用 Log4j2
- 支持链路跟踪 TraceId
- 日志级别：ERROR > WARN > INFO > DEBUG

## 测试规范
- 使用 JUnit 5 + AssertJ
- 使用 Testcontainers 进行集成测试
- 使用 ArchUnit 进行架构测试
- 测试类以 `Test` 结尾

## Maven 构建
- 多模块聚合项目
- 使用 BOM 统一管理依赖版本
- 支持 Docker 镜像构建
- 支持 GraalVM Native Image

## 配置管理
- 使用 Nacos 作为配置中心
- 配置文件使用 YAML 格式
- 敏感配置使用 Jasypt 加密

## 数据库
- 主库：PostgreSQL
- 时序库：TimescaleDB / TDengine / InfluxDB
- 缓存：Redis
- 搜索：Elasticsearch
- 使用 Liquibase 进行数据库版本管理

## IoT 相关
- 支持 Modbus (TCP/RTU/ASCII)
- 支持 MQTT (通过 EMQX)
- 支持 CoAP 协议
- 支持设备管理、产品管理、物模型管理
