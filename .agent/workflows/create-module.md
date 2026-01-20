---
description: 创建新的业务模块（遵循 COLA 架构）
---

# 创建新业务模块工作流

本工作流用于在 KCloud-Platform-IoT 项目中创建一个符合 COLA 架构的新业务模块。

## 前置条件
- 确定模块名称（如：`device`、`product` 等）
- 确定模块所属服务（通常在 `laokou-service` 下）

## 步骤

### 1. 创建模块目录结构
在 `laokou-service` 下创建以下目录结构：

```
laokou-{模块名}/
├── laokou-{模块名}-adapter/       # 适配器层
│   └── src/main/java/org/laokou/{模块名}/web/
├── laokou-{模块名}-app/           # 应用层
│   └── src/main/java/org/laokou/{模块名}/
│       ├── command/               # 命令执行器
│       ├── query/                 # 查询执行器
│       └── event/                 # 事件处理器
├── laokou-{模块名}-client/        # 客户端层
│   └── src/main/java/org/laokou/{模块名}/
│       ├── api/                   # API 接口定义
│       └── dto/                   # 数据传输对象
├── laokou-{模块名}-domain/        # 领域层
│   └── src/main/java/org/laokou/{模块名}/
│       ├── ability/               # 领域能力
│       ├── event/                 # 领域事件
│       ├── gateway/               # 网关接口
│       └── model/                 # 聚合根、实体、值对象
├── laokou-{模块名}-infrastructure/ # 基础设施层
│   └── src/main/java/org/laokou/{模块名}/
│       ├── gateway/               # 网关实现
│       └── mapper/                # MyBatis Mapper
├── laokou-{模块名}-start/         # 启动模块
│   └── src/main/java/org/laokou/{模块名}/
│       └── {模块名}Application.java
└── pom.xml                        # 聚合 POM
```

### 2. 创建父 POM 文件
// turbo
```bash
# 参考 laokou-admin 的 pom.xml 结构创建
```

### 3. 创建各子模块的 POM 文件
每个子模块需要独立的 pom.xml，依赖关系如下：
- **adapter** 依赖 app, client
- **app** 依赖 client, domain, infrastructure
- **client** 无内部依赖
- **domain** 无内部依赖
- **infrastructure** 依赖 domain
- **start** 依赖 adapter

### 4. 创建领域模型
在 domain 层创建：
- 聚合根类（XxxAggregate 或 XxxEntity）
- 值对象（不可变对象）
- 领域事件（XxxEvent）
- 网关接口（XxxGateway）

### 5. 创建 DTO
在 client 层创建：
- Command DTO（XxxCmd）
- Query DTO（XxxQry）
- Response DTO（XxxCO）

### 6. 创建应用服务
在 app 层创建：
- 命令执行器（XxxCmdExe）
- 查询执行器（XxxQryExe）
- 服务类（XxxService）

### 7. 创建 REST Controller
在 adapter 层创建：
- Controller 类（使用 @RestController）
- 添加 Swagger 注解

### 8. 创建基础设施实现
在 infrastructure 层创建：
- Mapper 接口
- Gateway 实现类
- DO（Database Object）

### 9. 添加配置
在 start 模块添加：
- application.yml
- bootstrap.yml
- Spring Boot 启动类

### 10. 注册模块
在父项目 `laokou-service/pom.xml` 中添加新模块

## 示例代码模板

### 聚合根示例
```java
package org.laokou.{模块名}.model;

import lombok.Data;
import org.laokou.common.domain.entity.AggregateRoot;

@Data
public class XxxAggregate extends AggregateRoot<Long> {
    private String name;
    // 业务字段...
    
    // 领域行为
    public void doSomething() {
        // 业务逻辑
        // 发布领域事件
        addEvent(new XxxCreatedEvent(this));
    }
}
```

### Controller 示例
```java
package org.laokou.{模块名}.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/{模块名}")
@Tag(name = "Xxx管理")
public class XxxController {

    private final XxxService xxxService;

    @PostMapping
    @Operation(summary = "创建")
    public Result<Void> create(@RequestBody XxxCmd cmd) {
        xxxService.create(cmd);
        return Result.ok();
    }
}
```
