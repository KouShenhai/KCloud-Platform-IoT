# KCloud-Platform-IoT 代码模板

本文档提供项目常用的代码模板和最佳实践。

## 目录
- [领域模型](#领域模型)
- [应用服务](#应用服务)
- [REST Controller](#rest-controller)
- [MyBatis Mapper](#mybatis-mapper)
- [事件处理](#事件处理)
- [异常处理](#异常处理)
- [单元测试](#单元测试)

---

## 领域模型

### 聚合根（Aggregate Root）
```java
package org.laokou.{模块名}.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.laokou.common.domain.entity.AggregateRoot;
import org.laokou.common.core.util.IdGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XxxA extends AggregateRoot {

    /** 名称 */
    private String name;

    /** 状态 */
    private Integer status;

    /** 描述 */
    private String description;

    /**
     * 创建聚合
     */
    public static XxxA create(String name, String description) {
        XxxAggregate aggregate = new XxxAggregate();
        aggregate.setId(IdGenerator.defaultSnowflakeId());
        aggregate.setName(name);
        aggregate.setDescription(description);
        aggregate.setStatus(StatusEnum.ENABLED.getValue());
        aggregate.addEvent(new XxxCreatedEvent(aggregate.getId(), name));
        return aggregate;
    }

    /**
     * 校验
     */
    public void validate() {
        if (StringUtil.isBlank(name)) {
            throw new BusinessException("名称不能为空");
        }
    }

    /**
     * 启用
     */
    public void enable() {
        this.status = StatusEnum.ENABLED.getValue();
        addEvent(new XxxEnabledEvent(this.id));
    }

    /**
     * 禁用
     */
    public void disable() {
        this.status = StatusEnum.DISABLED.getValue();
        addEvent(new XxxDisabledEvent(this.id));
    }
}
```

### 领域事件（Domain Event）
```java
package org.laokou.{模块名}.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.domain.event.DomainEvent;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XxxCreatedEvent extends DomainEvent {

    private Long xxxId;
    private String name;

    @Override
    public String getEventType() {
        return "XXX_CREATED";
    }
}
```

### 值对象（Value Object）
```java
package org.laokou.{模块名}.model;

import lombok.Value;

/**
 * 地址值对象（不可变）
 */
@Value
public class Address {
    String province;
    String city;
    String district;
    String detail;

    public String getFullAddress() {
        return province + city + district + detail;
    }
}
```

---

## 应用服务

### 命令执行器（Command Executor）
```java
package org.laokou.{模块名}.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.laokou.{模块名}.dto.XxxCmd;
import org.laokou.{模块名}.gateway.XxxGateway;
import org.laokou.{模块名}.model.XxxAggregate;
import org.laokou.common.domain.publish.DomainEventPublisher;

@Slf4j
@Component
@RequiredArgsConstructor
public class XxxCreateCmdExe {

    private final XxxGateway gateway;
    private final DomainEventPublisher eventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public void execute(XxxCmd cmd) {
        // 1. 创建聚合
        XxxAggregate aggregate = XxxAggregate.create(cmd.getName(), cmd.getDescription());

        // 2. 校验
        aggregate.validate();

        // 3. 保存
        gateway.save(aggregate);

        // 4. 发布领域事件
        eventPublisher.publish(aggregate.getEvents());

        log.info("创建成功，ID: {}", aggregate.getId());
    }
}
```

### 查询执行器（Query Executor）
```java
package org.laokou.{模块名}.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.laokou.{模块名}.dto.XxxQry;
import org.laokou.{模块名}.dto.clientobject.XxxCO;
import org.laokou.{模块名}.gatewayimpl.database.XxxMapper;
import org.laokou.common.core.page.Page;

@Component
@RequiredArgsConstructor
public class XxxPageQryExe {

    private final XxxMapper mapper;

    public Page<XxxCO> execute(XxxQry qry) {
        return mapper.selectPage(qry);
    }
}
```

### 服务类（Service）
```java
package org.laokou.{模块名};

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.laokou.{模块名}.api.XxxApi;
import org.laokou.{模块名}.command.*;
import org.laokou.{模块名}.query.*;
import org.laokou.{模块名}.dto.*;
import org.laokou.{模块名}.dto.clientobject.XxxCO;
import org.laokou.common.core.page.Page;

@Service
@RequiredArgsConstructor
public class XxxService implements XxxApi {

    private final XxxCreateCmdExe createCmdExe;
    private final XxxUpdateCmdExe updateCmdExe;
    private final XxxDeleteCmdExe deleteCmdExe;
    private final XxxPageQryExe pageQryExe;
    private final XxxGetByIdQryExe getByIdQryExe;

    @Override
    public void create(XxxCmd cmd) {
        createCmdExe.execute(cmd);
    }

    @Override
    public void update(XxxCmd cmd) {
        updateCmdExe.execute(cmd);
    }

    @Override
    public void delete(Long id) {
        deleteCmdExe.execute(id);
    }

    @Override
    public XxxCO getById(Long id) {
        return getByIdQryExe.execute(id);
    }

    @Override
    public Page<XxxCO> page(XxxQry qry) {
        return pageQryExe.execute(qry);
    }
}
```

---

## REST Controller

### 标准 Controller
```java
package org.laokou.{模块名}.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.laokou.common.core.result.Result;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.{模块名}.XxxService;
import org.laokou.{模块名}.dto.*;
import org.laokou.{模块名}.dto.clientobject.XxxCO;
import org.laokou.common.core.page.Page;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/xxx")
@Tag(name = "Xxx管理", description = "Xxx管理相关接口")
public class XxxController {

    private final XxxService xxxService;

    @PostMapping
    @PreAuthorize("hasAuthority('xxx:create')")
    @OperateLog(module = "Xxx管理", operation = "创建Xxx")
    @Operation(summary = "创建Xxx")
    public Result<Void> create(@Validated @RequestBody XxxCmd cmd) {
        xxxService.create(cmd);
        return Result.ok();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('xxx:update')")
    @OperateLog(module = "Xxx管理", operation = "修改Xxx")
    @Operation(summary = "修改Xxx")
    public Result<Void> update(
            @Parameter(description = "ID", required = true) @PathVariable Long id,
            @Validated @RequestBody XxxCmd cmd) {
        cmd.setId(id);
        xxxService.update(cmd);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('xxx:delete')")
    @OperateLog(module = "Xxx管理", operation = "删除Xxx")
    @Operation(summary = "删除Xxx")
    public Result<Void> delete(@Parameter(description = "ID", required = true) @PathVariable Long id) {
        xxxService.delete(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('xxx:view')")
    @Operation(summary = "查询Xxx详情")
    public Result<XxxCO> getById(@Parameter(description = "ID", required = true) @PathVariable Long id) {
        return Result.ok(xxxService.getById(id));
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('xxx:view')")
    @Operation(summary = "分页查询Xxx")
    public Result<Page<XxxCO>> page(@Validated XxxQry qry) {
        return Result.ok(xxxService.page(qry));
    }
}
```

---

## MyBatis Mapper

### Mapper 接口
```java
package org.laokou.{模块名}.gatewayimpl.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.{模块名}.dto.XxxQry;
import org.laokou.{模块名}.dto.clientobject.XxxCO;
import org.laokou.{模块名}.gatewayimpl.database.dataobject.XxxDO;
import org.laokou.common.core.page.Page;

import java.util.List;

@Mapper
public interface XxxMapper extends BaseMapper<XxxDO> {

    /**
     * 分页查询
     */
    Page<XxxCO> selectPage(@Param("qry") XxxQry qry);

    /**
     * 根据名称查询
     */
    XxxDO selectByName(@Param("name") String name);

    /**
     * 批量插入
     */
    int batchInsert(@Param("list") List<XxxDO> list);
}
```

### Mapper XML
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.laokou.{模块名}.gatewayimpl.database.XxxMapper">

    <select id="selectPage" resultType="org.laokou.{模块名}.dto.clientobject.XxxCO">
        SELECT id, name, status, description, create_time, update_time
        FROM t_xxx
        WHERE del_flag = 0
        <if test="qry.name != null and qry.name != ''">
            AND name LIKE CONCAT('%', #{qry.name}, '%')
        </if>
        <if test="qry.status != null">
            AND status = #{qry.status}
        </if>
        ORDER BY create_time DESC
    </select>

    <select id="selectByName" resultType="org.laokou.{模块名}.gatewayimpl.database.dataobject.XxxDO">
        SELECT id, name, status, description
        FROM t_xxx
        WHERE del_flag = 0 AND name = #{name}
    </select>

</mapper>
```

---

## 事件处理

### 事件监听器
```java
package org.laokou.{模块名}.event.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.laokou.{模块名}.event.XxxCreatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class XxxEventHandler {

    @Async
    @EventListener
    public void handleXxxCreated(XxxCreatedEvent event) {
        log.info("处理 Xxx 创建事件，ID: {}", event.getXxxId());
        // 处理逻辑...
    }
}
```

---

## 异常处理

### 自定义业务异常
```java
package org.laokou.{模块名}.exception;

import org.laokou.common.core.exception.BizException;

public class XxxNotFoundException extends BizException {

    public XxxNotFoundException(Long id) {
        super("Xxx not found: " + id);
    }
}
```

---

## 单元测试

### Service 测试
```java
package org.laokou.{模块名};

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.laokou.{模块名}.command.XxxCreateCmdExe;
import org.laokou.{模块名}.dto.XxxCmd;

import org.assertj.core.api.Assertions;

@ExtendWith(MockitoExtension.class)
class XxxServiceTest {

    @Mock
    private XxxCreateCmdExe createCmdExe;

    @InjectMocks
    private XxxService xxxService;

    @BeforeEach
    void setUp() {
        // 初始化
    }

    @Test
    @DisplayName("测试创建Xxx")
    void testCreate() {
        // Given
        XxxCmd cmd = new XxxCmd();
        cmd.setName("test");

        // When
        xxxService.create(cmd);

        // Then
		Mockito.verify(createCmdExe, times(1)).execute(cmd);
    }
}
```

### 集成测试（使用 Testcontainers）
```java
package org.laokou.{模块名};

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.assertj.core.api.Assertions;

@Testcontainers
@SpringBootTest
class XxxIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private XxxService xxxService;

    @Test
    void testIntegration() {
        // 集成测试逻辑
    }
}
```
