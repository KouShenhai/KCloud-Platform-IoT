### 项目备注
* 项目：KCloud
* 作者：老寇
* 语言：Java
* 职位：Java工程师
* 时间：2020.06.08 ~ 至今

### 项目介绍
老寇云，是基于SpringCloud开发，面向Java编程的学习者，用于技术进阶，技术知识体系架构的构建，用生动的代码来感受技术的魅力。

使用的中间件有redis

...

...

...

### 功能介绍
* 用户管理
* 角色管理
* 菜单管理
* 日志管理
* 字典管理
* 流程定义
* 流程任务
* 接口文档
* 数据监控
* 服务监控

### 技术体系

#### 四层架构
* application <=> 应用层
* domain <=> 领域层
* infrastructure <=> 基础层
* interfaces <=> 用户接口层

#### 基础框架
* Shiro
* SpringBoot
* SpringCloud Netflix

#### 技术栈
* mysql
* redis
* mybatis-plus
* apollo

#### 一键部署
* docker-compose

#### 持续交付
* jenkins

#### 项目结构
~~~
|-- laokou-base
   |-- laokou-common -- 公共组件
   |-- laokou-dynamic-datasource 多数据源组件
   |-- laokou-log -- 日志组件
|-- laokou-cloud
   |-- laokou-gateway -- 服务网关
   |-- laokou-monitor -- 服务监控
   |-- laokou-register -- 服务治理
|-- laokou-parent -- 版本依赖
|-- laokou-service
   |-- laokou-admin -- 后台管理模块
   |-- laokou-redis -- 缓存模块
~~~

### 项目配置
* 安装jdk1.8、mysql5.7、redis、apollo
* 创建数据库
* 开启apr模式
* 修改第三方相关配置
* 修改中间件相关配置

```yaml
  # mysql
  datasource:
    druid:
      # 连接地址
      url: jdbc:mysql://127.0.0.1:3306/kcloud?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false
      # 用户名
      username: root
      # 密码
      password: XXXXXX
  # redis
  redis:
    #数据库索引
    database: 0
    #主机
    host: 127.0.0.1
    #端口
    port: 6379
    #连接超时时长（毫秒）
    timeout: 6000ms 
```

### 多数据源配置
##### 代码引入
```java
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysUserApplicationServiceImpl implements SysUserApplicationService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    @DataSource("master")
    public IPage<SysUserVO> queryUserPage(SysUserQO qo) {
        IPage<SysUserVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysUserService.getUserPage(page,qo);
    }
}
```
##### 配置文件
```yaml
dynamic:
  datasource:
    slave:
      driver-class-name: com.mysql.jdbc.Driver
      url: jdbc:mysql://127.0.0.1:3306/kcloud?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false
      username: root
      password: 123456
```

### 数据权限
##### 代码引入
```java
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysUserApplicationServiceImpl implements SysUserApplicationService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    @DataFilter(tableAlias = "boot_sys_user")
    public IPage<SysUserVO> queryUserPage(SysUserQO qo) {
        IPage<SysUserVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysUserService.getUserPage(page,qo);
    }
}
```
##### 配置文件
```xml
<if test="qo.sqlFilter != null and qo.sqlFilter != ''">
    and ${qo.sqlFilter}
</if>
```

