---
description: 创建通用组件模块（在 laokou-common 下）
---

# 创建通用组件模块工作流

本工作流用于在 KCloud-Platform-IoT 项目中创建一个新的通用组件模块。

## 前置条件
- 确定组件名称（如：`mqtt`、`websocket` 等）
- 确定组件用途和依赖

## 步骤

### 1. 创建模块目录结构
在 `laokou-common` 下创建以下目录结构：

```
laokou-common-{组件名}/
├── src/
│   ├── main/
│   │   ├── java/org/laokou/common/{组件名}/
│   │   │   ├── config/           # 自动配置类
│   │   │   ├── annotation/       # 自定义注解（可选）
│   │   │   ├── handler/          # 处理器（可选）
│   │   │   └── util/             # 工具类（可选）
│   │   └── resources/
│   │       └── META-INF/
│   │           └── spring/
│   │               └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
│   └── test/
│       └── java/org/laokou/common/{组件名}/
└── pom.xml
```

### 2. 创建 POM 文件
// turbo
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.laokou</groupId>
        <artifactId>laokou-common</artifactId>
        <version>4.0.1-SNAPSHOT</version>
    </parent>
    <artifactId>laokou-common-{组件名}</artifactId>
    <name>laokou-common-{组件名}</name>
    <description>{组件描述}</description>

    <dependencies>
        <!-- 通常依赖 core 模块 -->
        <dependency>
            <groupId>org.laokou</groupId>
            <artifactId>laokou-common-core</artifactId>
        </dependency>
        
        <!-- 其他依赖 -->
    </dependencies>
</project>
```

### 3. 创建自动配置类
```java
package org.laokou.common.{组件名}.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({组件名}Properties.class)
public class {组件名}AutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public {组件名}Template {组件名}Template({组件名}Properties properties) {
        return new {组件名}Template(properties);
    }
}
```

### 4. 创建配置属性类
```java
package org.laokou.common.{组件名}.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.{组件名}")
public class {组件名}Properties {
    private String host = "localhost";
    private int port = 8080;
    // 其他配置...
}
```

### 5. 创建核心模板类
```java
package org.laokou.common.{组件名};

import lombok.RequiredArgsConstructor;
import org.laokou.common.{组件名}.config.{组件名}Properties;

@RequiredArgsConstructor
public class {组件名}Template {
    
    private final {组件名}Properties properties;
    
    // 核心方法...
}
```

### 6. 注册自动配置
在 `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` 文件中添加：
```
org.laokou.common.{组件名}.config.{组件名}AutoConfig
```

### 7. 编写单元测试
```java
package org.laokou.common.{组件名};

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class {组件名}TemplateTest {

    @Test
    void testXxx() {
        // 测试代码
    }
}
```

### 8. 在父模块注册
在 `laokou-common/pom.xml` 的 `<modules>` 中添加新模块：
```xml
<module>laokou-common-{组件名}</module>
```

## 注意事项
- 遵循 Spring Boot Starter 规范
- 使用 `@ConditionalOnXxx` 注解控制 Bean 创建条件
- 配置属性使用 `@ConfigurationProperties` 绑定
- 提供合理的默认值
- 编写充分的单元测试
