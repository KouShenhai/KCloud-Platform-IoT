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
* 系统监控

### 技术体系
#### 基础框架
* Shiro
* SpringBoot
* SpringCloud

#### 技术栈
* mysql
* redis
* mybatis-plus
* apollo

#### 一键部署
* docker-compose

#### 项目结构
~~~
|--laokou-base
   |--laokou-common -- 公共组件
   |--laokou-log -- 日志组件
|--laokou-cloud
   |--laokou-gateway -- 服务网关
   |--laokou-monitor -- 服务监控
   |--laokou-register -- 服务治理
|--laokou-parent -- 版本依赖
|--laokou-service
   |--laokou-admin -- 管理模块
   |--laokou-redis -- 缓存模块
~~~
### 项目配置
* 安装jdk1.8、mysql5.7、redis、apollo
* 创建数据库
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
```
