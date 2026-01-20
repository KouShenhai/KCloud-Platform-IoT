# KCloud-Platform-IoT 项目词汇表

本文档定义项目中常用的术语、缩写和命名规范，帮助 AI 更准确地理解和生成代码。

## 核心概念

### 架构术语
| 术语 | 全称 | 说明 |
|------|------|------|
| COLA | Clean Object-oriented and Layered Architecture | 阿里应用架构框架 |
| DDD | Domain Driven Design | 领域驱动设计 |
| CQRS | Command Query Responsibility Segregation | 命令查询职责分离 |
| RBAC | Role-Based Access Control | 基于角色的访问控制 |

### 分层术语
| 术语 | 说明 |
|------|------|
| Adapter | 适配器层，处理外部请求 |
| App | 应用层，编排业务流程 |
| Client | 客户端层，定义接口和 DTO |
| Domain | 领域层，核心业务逻辑 |
| Infrastructure | 基础设施层，技术实现 |

### 命名后缀
| 后缀 | 说明 | 示例 |
|------|------|------|
| A | 聚合根 | UserA |
| E | 实体 | DeviceE |
| V / Value | 值对象 | AddressV |
| Event | 领域事件 | UserCreatedEvent |
| Cmd | 命令对象 | CreateUserCmd |
| Qry | 查询对象 | UserPageQry |
| CO | 客户端对象(响应) | UserCO |
| DO | 数据对象 | UserDO |
| DTO | 数据传输对象 | UserDTO |
| QO | 数据查询对象 | UserQO |
| Exe | 执行器 | CreateUserCmdExe |
| Handler | 处理器 | UserEventHandler |
| Gateway | 网关接口 | UserGateway |
| GatewayImpl | 网关实现 | UserGatewayImpl |
| Mapper | MyBatis映射 | UserMapper |
| Service | 服务类 | UserService |
| Controller | 控制器 | UsersController |
| ServiceI | 接口 | UserServiceI |
| ServiceImpl | 接口实现 | UserServiceImpl |
| Properties | 配置属性 | RedisProperties |
| Config | 配置类 | RedisConfig |
| AutoConfig | 自动配置 | RedisAutoConfig |
| Template | 模板类 | RedisTemplate |
| Utils | 工具类 | StringUtils |
| Constants | 常量类 | UserConstants |
| Enum | 枚举类 | Status |

## 业务术语

### IoT 相关
| 术语 | 说明 |
|------|------|
| Device | 设备 |
| Product | 产品 |
| ThingModel | 物模型 |
| Property | 属性 |
| Service | 服务（设备能力） |
| Event | 事件（设备上报） |
| Gateway | 网关设备 |
| SubDevice | 子设备 |
| Protocol | 协议（Modbus/MQTT/CoAP等） |
| DataPoint | 数据点 |
| Telemetry | 遥测数据 |
| Command | 指令/命令 |

### 系统管理
| 术语 | 说明 |
|------|------|
| User | 用户 |
| Role | 角色 |
| Menu | 菜单 |
| Dept | 部门 |
| Permission | 权限 |
| Tenant | 租户 |
| Resource | 资源 |
| Dict | 字典 |
| Config | 配置 |
| Log | 日志 |

### 数据库字段
| 字段 | 说明 |
|------|------|
| id | 主键ID |
| create_time | 创建时间 |
| update_time | 更新时间 |
| create_by | 创建人 |
| update_by | 更新人 |
| del_flag | 删除标识（0正常 1删除） |
| status | 状态（0禁用 1启用） |
| tenant_id | 租户ID |
| sort | 排序 |
| remark | 备注 |

## 包名规范

### 通用模块包结构
```
org.laokou.common.{组件名}/
├── annotation/     # 注解
├── config/         # 配置
├── constant/       # 常量
├── exception/      # 异常
├── handler/        # 处理器
├── filter/         # 过滤器
├── interceptor/    # 拦截器
├── util/           # 工具类
└── support/        # 支持类
```

### 业务模块包结构
```
org.laokou.{模块名}/
├── web/                    # adapter 层
├── command/                # app 层 - 命令
├── query/                  # app 层 - 查询
├── event/                  # app 层 - 事件
├── api/                    # client 层 - 接口
├── dto/                    # client 层 - DTO
│   └── clientobject/       # 响应对象
├── model/                  # domain 层 - 模型
├── ability/                # domain 层 - 能力
├── gateway/                # domain 层 - 网关接口
├── gatewayimpl/            # infrastructure 层 - 网关实现
│   └── database/           # 数据库相关
│       └── dataobject/     # DO
└── convertor/              # 转换器
```

## API 路径规范

### 路径格式
```
/v{版本号}/{模块}/{功能}/{操作}
```

### 示例
```
POST   /v1/admin/users           # 创建用户
PUT    /v1/admin/users/{id}      # 修改用户
DELETE /v1/admin/users/{id}      # 删除用户
GET    /v1/admin/users/{id}      # 查询用户详情
GET    /v1/admin/users/page      # 分页查询用户
POST   /v1/admin/users/export    # 导出用户
POST   /v1/admin/users/import    # 导入用户
```

## 响应格式

### 统一响应结构
```json
{
    "code": 200,
    "msg": "success",
    "data": {},
    "traceId": "xxx"
}
```

### 分页响应结构
```json
{
    "code": 200,
    "msg": "success",
    "data": {
        "records": [],
        "total": 100,
        "size": 10,
        "current": 1,
        "pages": 10
    }
}
```

## 异常码规范

| 范围 | 说明 |
|------|------|
| 200 | 成功 |
| 400-499 | 客户端错误 |
| 500-599 | 服务端错误 |
| 1000-1999 | 认证授权错误 |
| 2000-2999 | 参数校验错误 |
| 3000-3999 | 业务错误 |
| 4000-4999 | 外部服务错误 |
