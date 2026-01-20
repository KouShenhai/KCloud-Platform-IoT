---
description: 创建 REST API 接口
---

# 创建 REST API 接口工作流

本工作流用于在现有业务模块中添加新的 REST API 接口。

## 前置条件
- 确定功能名称和所属模块
- 确定接口路径和 HTTP 方法
- 确定请求/响应数据结构

## 步骤

### 1. 创建 DTO（client 层）

#### 命令 DTO（用于创建/修改操作）
```java
package org.laokou.{模块名}.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "{功能}命令")
public class {功能名}Cmd {
    
    @NotBlank(message = "名称不能为空")
    @Schema(description = "名称")
    private String name;
    
    // 其他字段...
}
```

#### 查询 DTO（用于查询操作）
```java
package org.laokou.{模块名}.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.core.page.PageQuery;

@Data
@Schema(description = "{功能}查询")
public class {功能名}Qry extends PageQuery {
    
    @Schema(description = "名称")
    private String name;
    
    // 查询条件...
}
```

#### 响应 DTO
```java
package org.laokou.{模块名}.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "{功能}响应")
public class {功能名}CO {
    
    @Schema(description = "ID")
    private Long id;
    
    @Schema(description = "名称")
    private String name;
    
    // 响应字段...
}
```

### 2. 创建 API 接口（client 层）
```java
package org.laokou.{模块名}.api;

import org.laokou.common.core.result.Result;
import org.laokou.{模块名}.dto.{功能名}Cmd;
import org.laokou.{模块名}.dto.{功能名}Qry;
import org.laokou.{模块名}.dto.clientobject.{功能名}CO;

public interface {功能名}sServiceI {

    Result<Void> save{功能名}({功能名}Cmd cmd);
    
    Result<Void> modify{功能名}({功能名}Cmd cmd);
    
    Result<Void> remove{功能名}(Long id);
    
    Result<{功能名}CO> get{功能名}ById(Long id);
    
    Result<Page<{功能名}CO>> page{功能名}({功能名}Qry qry);
}
```

### 3. 创建应用服务（app 层）

#### 命令执行器
```java
package org.laokou.{模块名}.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.laokou.{模块名}.dto.{功能名}Cmd;

@Component
@RequiredArgsConstructor
public class {功能名}CmdExe {

    private final {功能名}Gateway gateway;

    public void execute({功能名}Cmd cmd) {
        // 命令处理逻辑
        {功能名}Aggregate aggregate = convert(cmd);
        aggregate.validate();
        gateway.save(aggregate);
    }
}
```

#### 查询执行器
```java
package org.laokou.{模块名}.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.laokou.{模块名}.dto.{功能名}Qry;
import org.laokou.{模块名}.dto.clientobject.{功能名}CO;

@Component
@RequiredArgsConstructor
public class {功能名}QryExe {

    private final {功能名}Mapper mapper;

    public Page<{功能名}CO> execute({功能名}Qry qry) {
        // 查询处理逻辑
        return mapper.selectPage(qry);
    }
}
```

#### 服务类
```java
package org.laokou.{模块名};

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.laokou.{模块名}.command.{功能名}CmdExe;
import org.laokou.{模块名}.query.{功能名}QryExe;

@Service
@RequiredArgsConstructor
public class {功能名}sServiceImpl implements {功能名}sServiceI {

    private final {功能名}CmdExe cmdExe;
    private final {功能名}QryExe qryExe;

    public void create({功能名}Cmd cmd) {
        cmdExe.execute(cmd);
    }

    public Page<{功能名}CO> page({功能名}Qry qry) {
        return qryExe.execute(qry);
    }
}
```

### 4. 创建 Controller（adapter 层）
```java
package org.laokou.{模块名}.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.laokou.common.core.result.Result;
import org.laokou.{模块名}.{功能名}Service;
import org.laokou.{模块名}.dto.{功能名}Cmd;
import org.laokou.{模块名}.dto.{功能名}Qry;
import org.laokou.{模块名}.dto.clientobject.{功能名}CO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/{模块名}/{功能路径}")
@Tag(name = "{功能名}管理", description = "{功能描述}")
public class {功能名}sController {

    private final {功能名}Service serviceI;

    @PostMapping
    @Operation(summary = "保存{功能名}")
    public void save{功能名}(@RequestBody {功能名}Cmd cmd) {
        service.save{功能名}(cmd);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改{功能名}")
    public Result<Void> update(@RequestBody {功能名}Cmd cmd) {
        service.update(cmd);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除{功能名}")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询{功能名}详情")
    public Result<{功能名}CO> getById(@Parameter(description = "ID") @PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询{功能名}")
    public Result<Page<{功能名}CO>> page(@Validated {功能名}Qry qry) {
        return Result.ok(service.page(qry));
    }
}
```

### 5. 创建领域模型（domain 层）

#### 聚合根
```java
package org.laokou.{模块名}.model;

import lombok.Data;
import org.laokou.common.domain.entity.AggregateRoot;

@Data
public class {功能名}Aggregate extends AggregateRoot<Long> {
    
    private String name;
    // 业务字段...
    
    public void validate() {
        // 业务校验
    }
    
    public void create() {
        // 创建逻辑
        addEvent(new {功能名}CreatedEvent(this));
    }
}
```

#### 网关接口
```java
package org.laokou.{模块名}.gateway;

import org.laokou.{模块名}.model.{功能名}Aggregate;

public interface {功能名}Gateway {
    
    void save({功能名}Aggregate aggregate);
    
    void update({功能名}Aggregate aggregate);
    
    void delete(Long id);
    
    {功能名}Aggregate getById(Long id);
}
```

### 6. 创建基础设施（infrastructure 层）

#### DO（数据对象）
```java
package org.laokou.{模块名}.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.laokou.common.mybatisplus.entity.BaseDO;

@Data
@TableName("{表名}")
public class {功能名}DO extends BaseDO {
    
    private String name;
    // 数据库字段...
}
```

#### Mapper
```java
package org.laokou.{模块名}.gatewayimpl.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.laokou.{模块名}.gatewayimpl.database.dataobject.{功能名}DO;

@Mapper
public interface {功能名}Mapper extends BaseMapper<{功能名}DO> {
    
}
```

#### 网关实现
```java
package org.laokou.{模块名}.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.laokou.{模块名}.gateway.{功能名}Gateway;
import org.laokou.{模块名}.gatewayimpl.database.{功能名}Mapper;
import org.laokou.{模块名}.model.{功能名}Aggregate;

@Component
@RequiredArgsConstructor
public class {功能名}GatewayImpl implements {功能名}Gateway {

    private final {功能名}Mapper mapper;

    @Override
    public void save({功能名}Aggregate aggregate) {
        {功能名}DO entity = convert(aggregate);
        mapper.insert(entity);
    }
    
    // 其他方法实现...
}
```

## 注意事项
- 使用 `@Validated` 进行参数校验
- 使用 Swagger 注解生成 API 文档
- 遵循 RESTFul 规范设计接口
- 命令操作使用 POST/PUT/DELETE，查询使用 GET
- 返回统一的 Result 响应格式