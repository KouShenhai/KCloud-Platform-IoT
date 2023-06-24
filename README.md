### 🎉 项目备注
项目：KCloud-Platform-Alibaba  
作者：老寇  
语言：Java  
时间：2022.06.15 ~ 至今  

### 📣 项目介绍
<p align="center"><img src="doc/image/logo.png" width="625" height="205" alt="图标"/></p>
KCloud-Platform-Alibaba（老寇云平台）是一款企业级微服务架构的云服务平台。基于Spring Boot 3.1.1、Spring Cloud 2022.0.3、Spring Cloud Alibaba 2022.0.0.0-RC2 最新版本开发的多租户SaaS系统。
遵循SpringBoot编程思想，高度模块化和可配置化。具备服务注册&发现、配置中心、服务限流、熔断降级、监控报警、多数据源、工作流、高亮搜索、定时任务、分布式链路、分布式缓存、分布式事务、分布式存储等功能，用于快速构建微服务项目。目前支持Shell、Docker等多种部署方式，实现RBAC权限、其中包含系统管理、系统监控、工作流程、数据分析等几大模块。
遵循阿里代码规范，代码简洁、架构清晰，非常适合作为基础框架使用。
<p align="center">
    <a target="_blank" href="https://github.com/KouShenhai/KCloud-Platform-Alibaba"><img alt="GitHub stars" src="https://img.shields.io/github/stars/KouShenhai/KCloud-Platform-Alibaba?logo=github"></a>
    <a target="_blank" href="https://github.com/KouShenhai/KCloud-Platform-Alibaba"><img alt="GitHub forks" src="https://img.shields.io/github/forks/KouShenhai/KCloud-Platform-Alibaba?logo=github"></a>
    <a target="_blank" href="https://github.com/KouShenhai/KCloud-Platform-Alibaba"><img alt="GitHub watchers" src="https://img.shields.io/github/watchers/KouShenhai/KCloud-Platform-Alibaba?logo=github"></a>
    <a target="_blank" href="https://github.com/KouShenhai/KCloud-Platform-Alibaba"><img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/KouShenhai/KCloud-Platform-Alibaba"></a>
	<a target="_blank" href="https://gitee.com/laokouyun/KCloud-Platform-Alibaba/stargazers"><img src="https://gitee.com/laokouyun/KCloud-Platform-Alibaba/badge/star.svg?theme=dark" alt="Gitee Star"></a>
    <a target="_blank" href="https://gitee.com/laokouyun/KCloud-Platform-Alibaba"><img src="https://gitee.com/laokouyun/KCloud-Platform-Alibaba/badge/fork.svg?theme=dark"  alt="Gitee Fork"></a>
    <a target="_blank" href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/static/v1?label=Spring Boot&message=3.1.1&color=green" alt="SpringBoot"></a>
    <a target="_blank" href="https://spring.io/projects/spring-cloud"><img alt="Spring Cloud" src="https://img.shields.io/static/v1?label=Spring Cloud&message=2022.0.3&color=green"></a>
    <a target="_blank" href="https://github.com/alibaba/spring-cloud-alibaba"><img alt="Spring Cloud" src="https://img.shields.io/static/v1?label=Spring Cloud Alibaba&message=2022.0.0.0-RC2&color=orange"></a>
    <a target="_blank" href="https://spring.io/projects/spring-authorization-server"><img alt="OAuth 2.1" src="https://img.shields.io/static/v1?label=OAuth 2.1&message=1.0.1&color=blue"></a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/downloads/#java17"><img alt="JDK" src="https://img.shields.io/badge/JDK-17.0.4.1-blue.svg"/></a>
    <a target="_blank" href="https://jq.qq.com/?_wv=1027&k=Ec8T76dR"><img src="https://img.shields.io/badge/Q群-465450496-blue.svg" alt="KCloud-Platform开源交流"></a>
</p>

### 🔖 在线文档
[https://koushenhai.github.io](https://koushenhai.github.io)

### 💪 版本号
特此说明，与Spring Boot版本保持一致

### 🔎 功能介绍
🚀 在线用户：强制踢出在线用户    
🚀 用户管理：用户信息增删改查（字段隔离&分库分表）（可重置密码）   
🚀 角色管理：角色信息增删改查，基于部门的数据权限、角色权限（字段隔离）     
🚀 菜单管理：菜单信息增删改查（字段隔离）（自定义菜单图标）   
🚀 部门管理：部门信息增删改查（字段隔离）    
🚀 日志管理：操作日志、登录日志查询、导出（字段隔离）       
🚀 字典管理：字典信息管理（数据库隔离）    
🚀 消息管理：消息提醒和消息通知（数据库隔离）    
🚀 搜索管理：通过关键字搜索并高亮显示（默认ES）    
🚀 流程定义：流程定义、挂起、激活、查看、删除（默认数据库）    
🚀 接口文档：Open Api Doc 3    
🚀 服务监控：服务监控及报警     
🚀 缓存监控：Redis内存监控    
🚀 主机监控：服务器监控    
🚀 存储管理：存储信息增删改查，兼容Amazon S3（数据库隔离）    
🚀 租户管理：租户信息增删改查增删改查（默认数据库）       
🚀 套餐管理：自定义租户菜单增删改查（默认数据库）    
🚀 数据源管理：数据源信息增删改查（默认数据库）         
🚀 资源管理：视频、图片、音频信息增删改查，资源审批（Seata AT模式）、处理、转办、委派，审批日志，数据同步（批量同步到ES）（默认数据库）          
🚀 用户登录：账号密码登录（多租户）、授权码登录（默认数据库）、手机号或邮件登录（请运行认证模式测试脚本.http）       

### 💡 系统架构
![](doc/image/老寇云平台架构图-阿里巴巴.png)

### ✂ 技术体系
#### 🎯 Spring全家桶及核心技术版本
| 组件                          | 版本             |
|:----------------------------|:---------------|
| Spring Boot                 | 3.1.1          |
| Spring Cloud                | 2022.0.3       |
| Spring Cloud Alibaba        | 2022.0.0.0-RC2 |
| Spring Boot Admin           | 3.0.4          |
| Spring Authorization Server | 1.1.1          |
| Mybatis Plus                | 3.5.3.1        |
| Nacos                       | 2.2.0.1        |
| Seata                       | 1.6.1          |
| Sentinel                    | 1.8.6          |
| Mysql                       | 8.0.33         |
| Redis                       | 7.0.11         |
| Elasticsearch               | 8.6.2          |
| RocketMQ                    | 5.1.1          |
| Flowable                    | 7.0.0.M1       |
| ShardingSphere              | 5.3.2          |
| OpenResty                   | 1.21.4.1       |
| Netty                       | 4.1.92.Final   |
| HBase                       | 2.5.3          |

#### 🍺 相关技术
- 配置中心&服务注册&发现：Nacos
- API网关：Spring Cloud Gateway
- 认证授权：Spring Security OAuth2 Authorization Server
- 服务消费：Spring Cloud OpenFeign & OkHttp & HttpClient & WebClient
- 负载均衡：Spring Cloud Loadbalancer
- 服务熔断&降级&限流：Sentinel
- 分库分表&读写分离：ShardingSphere
- 分布式事务：Seata & RocketMQ
- 消息队列：RabbitMQ & RocketMQ
- 分布式数据库：HBase
- 服务监控：Spring Boot Admin & Prometheus
- 链路跟踪：SkyWalking
- 任务调度：Power Job
- 日志分析：EFK
- 负载均衡：OpenResty
- 多级缓存：Caffeine & Redis
- 统计报表：MongoDB
- 对象存储：Amazon S3
- 自动化部署：Docker
- 网络通讯：Netty
- 持续集成&交付：Jenkins
- 持久层框架：Mybatis Plus
- JSON序列化：Jackson
- 数据库：Mysql
- 工作流：Flowable

#### 🌴 项目结构
~~~
├── laokou-common  
        └── laokou-common-log                      --- 日志组件  
        └── laokou-common-sms                      --- 短信组件  
        └── laokou-common-mail                     --- 邮箱组件  
        └── laokou-common-core                     --- 核心组件  
        └── laokou-common-cors                     --- 跨域组件  
        └── laokou-common-redis                    --- 缓存组件  
        └── laokou-common-p6spy                    --- 日志组件  
        └── laokou-common-kafka                    --- 消息组件  
        └── laokou-common-wechat                   --- 微信组件  
        └── laokou-common-pulsar                   --- 消息组件  
        └── laokou-common-swagger                  --- 文档组件  
        └── laokou-common-reptile                  --- 爬虫组件  
        └── laokou-common-mongodb                  --- 报表组件  
        └── laokou-common-graphql                  --- 查询组件  
        └── laokou-common-rocketmq                 --- 消息组件  
        └── laokou-common-algorithm                --- 算法组件  
        └── laokou-common-power-job                --- 工作组件  
        └── laokou-common-freemarker               --- 模板组件  
        └── laokou-common-prometheus               --- 监控组件  
        └── laokou-common-elasticsearch            --- 搜索组件  
        └── laokou-common-bom                      --- 依赖版本库  
        └── laokou-common-i18n                     --- 国际化组件  
        └── laokou-common-tenant                   --- 多租户组件  
        └── laokou-common-easy-captcha             --- 验证码组件  
        └── laokou-common-banner                   --- Banner组件  
        └── laokou-common-oss                      --- 对象存储组件  
        └── laokou-common-lock                     --- 分布式锁组件  
        └── laokou-common-trace                    --- 链路跟踪组件  
        └── laokou-common-nacos                    --- 注册发现组件  
        └── laokou-common-hbase                    --- 海量存储组件  
        └── laokou-common-dubbo                    --- 远程调用组件  
        └── laokou-common-netty                    --- 网络通讯组件  
        └── laokou-common-jasypt                   --- 加密解密组件  
        └── laokou-common-secret                   --- 接口验签组件  
        └── laokou-common-logback                  --- 日志收集组件  
        └── laokou-common-xxl-job                  --- 任务调度组件  
        └── laokou-common-security                 --- 认证授权组件  
        └── laokou-common-openfeign                --- 远程调用组件   
        └── laokou-common-easy-excel               --- 导入导出组件   
        └── laokou-common-clickhouse               --- 列式存储组件  
        └── laokou-common-data-cache               --- 数据缓存组件  
        └── laokou-common-data-filter              --- 数据过滤组件  
        └── laokou-common-redis-search             --- 高效搜索组件  
        └── laokou-common-mybatis-plus             --- 对象映射组件  
        └── laokou-common-dynamic-router           --- 动态路由组件  
        └── laokou-common-shardingsphere           --- 分库分表组件  
        └── laokou-common-ip-region                --- IP归属地组件  
        └── laokou-common-neo4j                    --- 图形数据库组件  
        └── laokou-common-seata                    --- 分布式事务组件  
        └── laokou-common-r2dbc                    --- 响应式数据库组件  
        └── laokou-common-sentinel                 --- 服务限流&熔断降级组件  
├── laokou-cloud  
        └── laokou-gateway                         --- API网关  
        └── laokou-monitor                         --- 服务监控  
        └── laokou-xxl-job                         --- 任务调度  
        └── laokou-register                        --- 服务治理  
        └── laokou-sentinel                        --- 流量治理  
        └── laokou-power-job                       --- 任务调度  
        └── laokou-seata                           --- 分布式事务  
├── laokou-service         
        └── laokou-auth                            --- 认证授权模块  
        └── laokou-admin                           --- 后台管理模块  
        └── laokou-generate                        --- 模板生成模块  
        └── laokou-modlule  
                └── laokou-api                     --- API模块  
                └── laokou-test                    --- 测试模块  
                └── laokou-im                      --- 即时通讯模块  
                └── laokou-flowable                --- 工作流程模块  
                └── laokou-logstash                --- 日志收集模块  
~~~

### 🎵 项目截图
<table>
    <tr>
        <td><img alt="暂无图片" src="doc/image/1.png"/></td>
        <td><img alt="暂无图片" src="doc/image/2.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/3.png"/></td>
        <td><img alt="暂无图片" src="doc/image/4.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/5.png"/></td>
        <td><img alt="暂无图片" src="doc/image/6.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/7.png"/></td>
        <td><img alt="暂无图片" src="doc/image/8.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/9.png"/></td>
        <td><img alt="暂无图片" src="doc/image/10.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/11.png"/></td>
        <td><img alt="暂无图片" src="doc/image/12.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/13.png"/></td>
        <td><img alt="暂无图片" src="doc/image/14.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/15.png"/></td>
        <td><img alt="暂无图片" src="doc/image/16.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/17.png"/></td>
        <td><img alt="暂无图片" src="doc/image/18.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/19.png"/></td>
        <td><img alt="暂无图片" src="doc/image/20.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/21.png"/></td>
        <td><img alt="暂无图片" src="doc/image/22.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/23.png"/></td>
        <td><img alt="暂无图片" src="doc/image/24.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/25.png"/></td>
        <td><img alt="暂无图片" src="doc/image/26.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/29.png"/></td>
        <td><img alt="暂无图片" src="doc/image/30.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/31.png"/></td>
        <td><img alt="暂无图片" src="doc/image/32.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/33.png"/></td>
        <td><img alt="暂无图片" src="doc/image/34.png"/></td>
    </tr>
    <tr>
        <td><img alt="暂无图片" src="doc/image/27.png"/></td>
        <td><img alt="暂无图片" src="doc/image/28.png"/></td>
    </tr>
</table>

### 😛 用户权益
- 采用Apache2.0开源协议，并且承诺永不参与商业用途，仅供大家无偿使用（点个star，拜托啦~🙏）
- 采用Apache2.0开源协议，并且承诺永不参与商业用途，仅供大家无偿使用（点个star，拜托啦~🙏）
- 采用Apache2.0开源协议，并且承诺永不参与商业用途，仅供大家无偿使用（点个star，拜托啦~🙏）

### 😻 开源协议
KCloud-Platform-Alibaba 开源软件遵循 [Apache 2.0 协议](https://www.apache.org/licenses/LICENSE-2.0.html) 请务必保留作者、Copyright信息  

### 👀 项目地址
Github 后端地址：[KCloud-Platform-Alibaba](https://github.com/KouShenhai/KCloud-Platform-Alibaba)  
Github 前端地址：[KCloud-Antdv-Alibaba](https://github.com/KouShenhai/KCloud-Antdv-Alibaba)

Gtiee 后端地址：[KCloud-Platform-Alibaba](https://gitee.com/laokouyun/KCloud-Platform-Alibaba)  
Gitee 前端地址：[KCloud-Antdv-Alibaba](https://gitee.com/laokouyun/KCloud-Antdv-Alibaba)  

GitLab 后端地址：[KCloud-Platform-Alibaba](https://gitlab.com/KouShenhai/KCloud-Platform-Alibaba)  
GitLab 前端地址：[KCloud-Antdv-Alibaba](https://gitlab.com/KouShenhai/KCloud-Antdv-Alibaba)

### 🐭 鸣谢组织
[Spring官网](https://spring.io)  
[阿里巴巴社区](https://github.com/alibaba/spring-cloud-alibaba)  
[人人社区](https://www.renren.io)   
[若依社区](https://www.ruoyi.vip)  
[苞米豆社区](https://baomidou.com/)  
[livk-cloud社区](https://gitter.im/livk-cloud/community)  
[laokouyun社区](https://github.com/laokouyun)  

### 🐼 鸣谢个人
<a href="https://github.com/KouShenhai" class="mr-2" data-hovercard-type="user" data-hovercard-url="/users/KouShenhai/hovercard" data-octo-click="hovercard-link-click" data-octo-dimensions="link_type:self">
        <img src="https://avatars.githubusercontent.com/u/48756217?s=64&amp;v=4" alt="@KouShenhai" height="32" width="32" data-view-component="true" class="avatar circle">
</a>
<a href="https://github.com/liang99" class="mr-2" data-hovercard-type="user" data-hovercard-url="/users/liang99/hovercard" data-octo-click="hovercard-link-click" data-octo-dimensions="link_type:self">
        <img src="https://avatars.githubusercontent.com/u/26246537?s=64&amp;v=4" alt="@liang99" height="32" width="32" data-view-component="true" class="avatar circle">
</a>
<a href="https://github.com/livk-cloud" class="mr-2" data-hovercard-type="user" data-hovercard-url="/users/livk-cloud/hovercard" data-octo-click="hovercard-link-click" data-octo-dimensions="link_type:self">
        <img src="https://avatars.githubusercontent.com/u/50291874?s=64&amp;v=4" alt="@livk-cloud" height="32" width="32" data-view-component="true" class="avatar circle">
</a>

### 🐸 联系
博客：[https://kcloud.blog.csdn.net](https://kcloud.blog.csdn.net)    

邮箱：[2413176044@qq.com](https://mail.qq.com)  

QQ：[2413176044]( http://wpa.qq.com/msgrd?v=3&uin=2413176044&Site=gitee&Menu=yes)  

[![加入QQ群](https://img.shields.io/badge/Q群-465450496-blue.svg)](https://jq.qq.com/?_wv=1027&k=Ec8T76dR)
