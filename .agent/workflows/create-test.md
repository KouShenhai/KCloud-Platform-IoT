---
description: 创建单元测试
---

# 创建单元测试工作流

本工作流用于为现有代码创建单元测试。

## 前置条件
- 确定要测试的类和方法
- 了解项目测试规范

## 测试框架
- **JUnit 5**: 测试框架
- **AssertJ**: 断言库
- **Mockito**: Mock 框架
- **Testcontainers**: 集成测试容器

## 步骤

### 1. 创建测试类
测试类放在 `src/test/java` 目录下，包路径与被测类一致。

```java
package org.laokou.{模块名};

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.assertj.core.api.Assertions;
import org.mockito.Mockito;
import org.mockito.ArgumentMatchers;

@ExtendWith(MockitoExtension.class)
@DisplayName("{类名}测试")
class XxxTest {

    @Mock
    private Dependency dependency;

    @InjectMocks
    private Xxx xxx;

    @BeforeEach
    void setUp() {
        // 初始化
    }

    @AfterEach
    void tearDown() {
        // 清理
    }

    @Test
    @DisplayName("测试方法描述")
    void testMethodName() {
        // Given
        String input = "test";
        when(dependency.method(any())).thenReturn("result");

        // When
        String result = xxx.method(input);

        // Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo("expected");
        verify(dependency).method(input);
    }

    @Test
    @DisplayName("测试异常情况")
    void testException() {
        // Given
        when(dependency.method(any())).thenThrow(new RuntimeException());

        // When & Then
        assertThatThrownBy(() -> xxx.method("input"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("expected message");
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c"})
    @DisplayName("参数化测试")
    void testParameterized(String input) {
        // 参数化测试
        assertThat(xxx.validate(input)).isTrue();
    }

    @Nested
    @DisplayName("嵌套测试组")
    class NestedTests {
        @Test
        void nestedTest() {
            // 嵌套测试
        }
    }
}
```

### 2. 常用断言

#### AssertJ 断言
```java
// 基础断言
assertThat(actual).isEqualTo(expected);
assertThat(actual).isNotNull();
assertThat(actual).isNull();
assertThat(actual).isTrue();
assertThat(actual).isFalse();

// 字符串断言
assertThat(str).isEmpty();
assertThat(str).isNotEmpty();
assertThat(str).contains("sub");
assertThat(str).startsWith("prefix");
assertThat(str).matches("regex");

// 集合断言
assertThat(list).isEmpty();
assertThat(list).hasSize(3);
assertThat(list).contains("a", "b");
assertThat(list).containsExactly("a", "b", "c");
assertThat(list).containsAnyOf("a", "x");

// 异常断言
assertThatThrownBy(() -> method())
    .isInstanceOf(IllegalArgumentException.class)
    .hasMessageContaining("error");

assertThatCode(() -> method()).doesNotThrowAnyException();

// 对象断言
assertThat(obj)
    .extracting("field1", "field2")
    .containsExactly("value1", "value2");
```

### 3. Mockito 使用

#### Mock 设置
```java
// 返回值
when(mock.method(any())).thenReturn(result);
when(mock.method(anyString())).thenReturn(result);
when(mock.method(eq("specific"))).thenReturn(result);

// 抛出异常
when(mock.method(any())).thenThrow(new Exception());

// 多次调用不同返回值
when(mock.method(any()))
    .thenReturn(first)
    .thenReturn(second);

// void 方法
doNothing().when(mock).voidMethod(any());
doThrow(new Exception()).when(mock).voidMethod(any());

// 参数捕获
ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
Mockito.verify(mock).method(captor.capture());
assertThat(captor.getValue()).isEqualTo("expected");
```

#### 验证调用
```java
// 验证调用次数
Mockito.verify(mock).method(any());
Mockito.verify(mock, times(2)).method(any());
Mockito.verify(mock, never()).method(any());
Mockito.verify(mock, atLeast(1)).method(any());
Mockito.verify(mock, atMost(3)).method(any());

// 验证顺序
InOrder inOrder = inOrder(mock1, mock2);
inOrder.verify(mock1).method1();
inOrder.verify(mock2).method2();

// 验证无更多交互
verifyNoMoreInteractions(mock);
verifyNoInteractions(mock);
```

### 4. 集成测试

#### 使用 Testcontainers
```java
@Testcontainers
@SpringBootTest
class IntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = 
        new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Container
    static RedisContainer redis = 
        new RedisContainer(DockerImageName.parse("redis:8"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private XxxService xxxService;

    @Test
    void testIntegration() {
        // 集成测试
    }
}
```

### 5. 架构测试

#### 使用 ArchUnit
```java
@AnalyzeClasses(packages = "org.laokou")
class ArchitectureTest {

    @ArchTest
    static final ArchRule controllers_should_be_in_web_package =
        classes()
            .that().haveNameMatching(".*Controller")
            .should().resideInAPackage("..web..");

    @ArchTest
    static final ArchRule services_should_not_depend_on_controllers =
        noClasses()
            .that().resideInAPackage("..service..")
            .should().dependOnClassesThat().resideInAPackage("..web..");

    @ArchTest
    static final ArchRule domain_should_not_depend_on_infrastructure =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..");
}
```

## 最佳实践
- 每个测试方法只测试一个场景
- 使用 `@DisplayName` 描述测试目的
- 遵循 Given-When-Then 模式
- 测试覆盖正常路径和异常路径
- 避免测试私有方法
- 测试命名：`test{方法名}_{场景}_{期望结果}`