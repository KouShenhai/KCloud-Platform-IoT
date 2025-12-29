### 1.ARM安装与卸载JDK25

```shell
# 安装
sudo pacman -Syy
sudo pacman -S jdk25-openjdk
```

```shell
# 卸载
sudo pacman -R jdk25-openjdk
```

### 2.边缘网关调优指南【1G内存】

<font color="red">👉 一旦 JVM 发生 OOM，立刻、直接退出进程（kill -9 级别）</font>

```shell
java \
  -Xms256m \
  -Xmx256m \
  -XX:MaxMetaspaceSize=128m \
  -XX:ReservedCodeCacheSize=32m \
  -XX:+UseSerialGC \
  -XX:MaxDirectMemorySize=32m \
  -XX:+ExitOnOutOfMemoryError \
  -jar app.jar
```

#### 2.1.锁死堆内存

<font color="red">👉 防止JVM运行中偷偷扩容</font>

<font color="red">👉 避免Full GC频繁抖动</font>

```shell
-Xms256m -Xmx256m
```

#### 2.2.垃圾回收器
<font color="red">👉 G1/ZGC/Shenandoah都有额外线程和元数据开销</font>

<font color="red">👉 在<1GB内存下，Serial GC反而最省内存</font>

<font color="red">👉 JDK25里Serial GC依然是稳定、可用、低占用的</font>

```shell
-XX:+UseSerialGC
```

#### 2.3锁死元空间

<font color="red">👉 加载的类一多，Metaspace会吃掉你剩余的内存</font>

<font color="red">👉 OOM往往不是Heap，而是Metaspace</font>

```shell
-XX:MaxMetaspaceSize=128m
```

#### 2.4JIT生成的机器码

```shell
-XX:ReservedCodeCacheSize=32m
```

**小内存（≤1GB）推荐值**

| 场景          | Code Cache        |
|-------------|-------------------|
| CLI / 工具    | 16m               |
| 普通服务        | 32m               |
| Spring Boot | 32m ~ 48m         |
| 超高 QPS      | 64m（不建议 500MB 环境） |


#### 2.5.堆外内存

<font color="red">👉 堆外内存，GC 只能“间接回收”，回收慢、不可控</font>

<font color="red">👉 回收依赖：Cleaner、引用失效、Full GC</font>

```shell
-XX:MaxDirectMemorySize=32m
```

**广义堆外内存**

| 类型            | 例子          |
|---------------|-------------|
| Direct Memory | NIO / Netty |
| Code Cache    | JIT 机器码     |
| Metaspace     | 类元数据        |
| JNI malloc    | 本地库         |
| Thread Stack  | 每个线程的栈      |

#### 2.6.tomcat

<font color="red">👉 线程栈也会吃内存（每线程 ~1MB）</font>

```shell
server:
  tomcat:
    max-threads: 50
    accept-count: 100
```
