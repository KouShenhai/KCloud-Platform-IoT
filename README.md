### 项目备注
项目：KCloud-Platform-Alibaba  
作者：老寇  
语言：Java  
职位：Java工程师   
时间：2020.06.08 ~ 至今  

### 项目介绍
KCloud-Platform-Alibaba（老寇云平台）是一款企业级微服务架构的云服务平台。基于Spring Boot 3.0.2、Spring Cloud 2022.0.1、Spring Cloud Alibaba 2022.0.0.0-RC1 最新版本开发，
遵循SpringBoot编程思想，高度模块化和可配置化。具备服务注册&发现、配置中心、服务限流、熔断降级、监控报警、多数据源、工作流、高亮搜索、定时任务、分布式缓存、分布式事务、分布式存储等功能，用于快速构建微服务项目。目前支持Shell、Docker等多种部署方式，实现RBAC权限、其中包含系统管理、系统监控、工作流程、数据分析等几大模块。
遵循阿里代码规范，代码简洁、架构清晰，非常适合作为基础框架使用。
<p align="center">
	<a target="_blank" href="https://gitee.com/laokouyun/KCloud-Platform-Alibaba/stargazers"><img src="https://gitee.com/laokouyun/KCloud-Platform-Alibaba/badge/star.svg?theme=dark" alt="Gitee Star"></a>
    <a target="_blank" href="https://gitee.com/laokouyun/KCloud-Platform-Alibaba"><img src="https://gitee.com/laokouyun/KCloud-Platform-Alibaba/badge/fork.svg?theme=dark"  alt="Gitee Fork"></a>
    <a target="_blank" href="https://github.com/KouShenhai/KCloud-Platform-Alibaba"><img alt="GitHub Stars" src="https://img.shields.io/github/stars/KouShenhai/KCloud-Platform-Alibaba?logo=github"></a>
    <a target="_blank" href="https://github.com/KouShenhai/KCloud-Platform-Alibaba"><img alt="GitHub Forks" src="https://img.shields.io/github/forks/KouShenhai/KCloud-Platform-Alibaba?logo=github"></a>
    <a target="_blank" href="https://github.com/KouShenhai/KCloud-Platform-Alibaba"><img alt="GitHub watchers" src="https://img.shields.io/github/watchers/KouShenhai/KCloud-Platform-Alibaba?logo=github"></a>
    <a target="_blank" href="https://github.com/KouShenhai/KCloud-Platform-Alibaba"><img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/KouShenhai/KCloud-Platform-Alibaba"></a>
    <a target="_blank" href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/static/v1?label=Spring Boot&message=3.0.2&color=green" alt="SpringBoot"></a>
    <a target="_blank" href="https://spring.io/projects/spring-cloud"><img alt="Spring Cloud" src="https://img.shields.io/static/v1?label=Spring Cloud&message=2022.0.1&color=green"></a>
    <a target="_blank" href="https://github.com/alibaba/spring-cloud-alibaba"><img alt="Spring Cloud" src="https://img.shields.io/static/v1?label=Spring Cloud Alibaba&message=2022.0.0.0-RC1&color=orange"></a>
    <a target="_blank" href="https://spring.io/projects/spring-authorization-server"><img alt="OAuth 2.1" src="https://img.shields.io/static/v1?label=OAuth 2.1&message=1.0.0&color=blue"></a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/downloads/#java17"><img alt="JDK" src="https://img.shields.io/badge/JDK-17.0.4.1-blue.svg"/></a>
    <a target="_blank" href="https://qm.qq.com/cgi-bin/qm/qr?k=WFANTXDEjrDw6UxsrRFCv_rQsEu6LTxH&jump_from=webapi"><img src="https://img.shields.io/badge/Q群-218686225-blue.svg" alt="Java技术交流QQ群"></a>
</p>

### 功能介绍
- [x] 用户管理：用户信息管理，可重置用户密码
- [x] 角色管理：基于部门的数据权限、角色权限  
- [x] 菜单管理：自定义菜单图标、菜单信息管理
- [x] 部门管理：部门信息管理
- [x] 日志管理：操作日志、登录日志
- [x] 字典管理：字典信息管理
- [x] 消息管理：消息提醒和消息通知
- [x] 搜索管理：通过关键字搜索并高亮显示
- [x] 资源管理：视频、图片、音频管理，资源审批（分布式事务AT模式），审批日志
- [x] 流程定义：流程定义、挂起、激活、查看、删除
- [x] 接口文档：Swagger2升级为Open Api Doc 3
- [x] 服务监控：服务内存监控
- [x] 缓存监控：Redis内存监控
- [x] 主机监控：服务器内存或磁盘监控
- [ ] 存储管理：存储信息管理，兼容Amazon S3
- [ ] 邮件管理：邮件信息管理
- [ ] 模板管理：模板信息管理
- [ ] 短信管理：短信信息管理
- [ ] 微信公众号管理：微信公众号一体化管理

### 系统架构
![](doc/image/老寇云平台架构图-阿里巴巴.png)

### 技术体系

#### Spring全家桶及核心技术版本
| 组件                          | 版本             |
|:----------------------------|:---------------|
| Spring Boot                 | 3.0.2          |
| Spring Cloud                | 2022.0.1       |
| Spring Cloud Alibaba        | 2022.0.0.0-RC1 |
| Spring Boot Admin           | 3.0.0          |
| Spring Authorization Server | 1.0.0          |
| Mybatis Plus                | 3.5.3.1        |
| Nacos                       | 2.2.0          |
| Sentinel                    | 1.8.6          |
| Seata                       | 1.6.1          |
| Mysql                       | 5.7.9          |
| Redis                       | 6.0.6          |
| Elasticsearch               | 7.6.2          |
| RocketMQ                    | 5.0.0          |
| Kafka                       | 2.8.1          |
| Flowable                    | 7.0.0.M1       |

#### 相关技术

- API 网关：Spring Cloud Gateway
- 配置中心：Nacos
- 服务注册&发现：Nacos
- 认证授权：Spring Security OAuth2 Authorization Server
- 服务消费：Spring Cloud OpenFeign & HttpClient & WebClient
- 负载均衡：Spring Cloud Loadbalancer
- 服务熔断&降级&限流：Sentinel
- 分布式事务：Seata
- 服务监控：Spring Boot Admin & Prometheus
- 消息队列：RocketMQ & Kafka
- 日志分析：EFK
- 数据缓存：Caffeine & Redis
- 统计报表：MongoDB
- 对象存储：Amazon S3
- 服务部署：Docker
- 持续交付：Jenkins
- 持久层框架：Mybatis Plus
- JSON 序列化：Jackson
- 数据库：Mysql
- 工作流：Flowable

#### 项目结构
~~~
├── laokou-common  
        └── laokou-common-log               --- 日志组件  
        └── laokou-common-core              --- 核心组件  
        └── laokou-common-swagger           --- 文档组件  
        └── laokou-common-bom               --- 依赖版本库  
        └── laokou-common-security          --- 认证授权组件  
        └── laokou-common-data-cache        --- 数据缓存组件
        └── laokou-common-data-filter       --- 数据过滤组件
        └── laokou-common-mybatis-plus      --- 对象映射组件  
├── laokou-cloud  
        └── laokou-gateway                  --- API网关  
        └── laokou-monitor                  --- 服务监控  
        └── laokou-sntinel                  --- 服务限流 & 熔断降级  
├── laokou-service  
        └── laokou-api                      --- API模块  
        └── laokou-generator                --- 模板模块  
        └── laokou-auth                     --- 认证授权模块  
        └── laokou-admin                    --- 后台管理模块  
        └── laokou-report                   --- 数据分析模块  
        └── laokou-modlule  
                └── laokou-sms              --- 短信模块  
                └── laokou-mail             --- 邮件模块  
                └── laokou-redis            --- 缓存模块  
                └── laokou-kafka            --- 消息模块  
                └── laokou-xxl-job          --- 工作模块  
                └── laokou-mongodb          --- 报表模块  
                └── laokou-rocketmq         --- 消息模块  
                └── laokou-elasticsearch    --- 搜索模块  
                └── laokou-im               --- 即时通讯模块  
                └── laokou-oss              --- 对象存储模块  
                └── laokou-flowable         --- 工作流程模块  
~~~

### 环境配置
#### 安装教程
[centos7 安装jdk1.8](https://kcloud.blog.csdn.net/article/details/82184984)  
[centos7 安装mysql5.7](https://kcloud.blog.csdn.net/article/details/123628721)  
[centos7 安装maven](https://kcloud.blog.csdn.net/article/details/108459715)  
[centos7 安装redis](https://kcloud.blog.csdn.net/article/details/82589349)  
[centos7 安装中文字体](https://kcloud.blog.csdn.net/article/details/106575947)  
[centos7 安装jenkins](https://kcloud.blog.csdn.net/article/details/112171878)  
[centos7 安装nacos](https://kcloud.blog.csdn.net/article/details/82589017)  
[centos7 安装elasticsearch7.6.2](https://kcloud.blog.csdn.net/article/details/123123229)  
[centos7 安装kafka](https://kcloud.blog.csdn.net/article/details/123771040)  
[centos7 安装rocketmq](https://blog.csdn.net/qq_39893313/article/details/128223900)  

#### 安装包
[百度网盘](https://pan.baidu.com/s/1swrV9ffJnmz4S0mfkuBbIw) 提取码：1111

### 环境配置
#### 服务配置
```yaml
spring:
  # mysql
  datasource:
    # 连接地址
    url: jdbc:mysql://127.0.0.1:3306/kcloud_platform_alibaba?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false
    # 用户名
    username: root
    # 密码
    password: 123456
  # redis
  data:
    redis:
      #主机
      host: 127.0.0.1
      #端口
      port: 6379
      #连接超时时长（毫秒）
      timeout: 6000ms 
# elasticsearch
elasticsearch:
  #主机
  host: 127.0.0.1:9200
  #节点
  cluster-name: elasticsearch-node
```

### JDK版本兼容
##### VM options配置
```shell script
--add-opens=java.base/java.lang=ALL-UNNAMED
```

### 数据权限
##### 代码引入
```java
@Service
@RequiredArgsConstructor
public class SysUserApplicationServiceImpl implements SysUserApplicationService {
    
    private final SysUserService sysUserService;

    @Override
    @DataFilter(tableAlias = "boot_sys_user")
    public IPage<SysUserVO> queryUserPage(SysUserQO qo) {
        IPage<SysUserVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysUserService.getUserPage(page,qo);
    }
}
```

### 二级缓存
##### 代码引入
```java
public class SysUserApiController {
    @DataCache(key = "#id")
    public HttpResult<SysUserVO> detail(@RequestParam("id") Long id) {
        return new HttpResult<SysUserVO>().ok(sysUserApplicationService.getUserById(id));
    }
}
```

### 分布式事务AT
#### 服务配置
```yaml
# seata
seata:
  config:
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      namespace: xxx-xxx-xxx-xx
      group: SEATA_GROUP
  registry:
    type: nacos
    nacos:
      namespace: xxx-xxx-xxx-xx
      group: SEATA_GROUP
      server-addr: 127.0.0.1:8848A
  enabled: true
  tx-service-group: default_tx_group
  data-source-proxy-mode: AT
```

##### 代码引入
```shell
@Service
@Slf4j
@RequiredArgsConstructor
public class SysResourceApplicationServiceImpl implements SysResourceApplicationService {
    
    /**
     * 使用openfeign调用时，每个被调用服务都需要加 @Transactional
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    @GlobalTransactional
    public Boolean insertResource(SysResourceAuditDTO dto) {
        log.info("分布式事务 XID:{}", RootContext.getXID());
        SysResourceDO sysResourceDO = ConvertUtil.sourceToTarget(dto, SysResourceDO.class);
        sysResourceDO.setEditor(UserUtil.getUserId());
        sysResourceService.save(sysResourceDO);
        Long id = sysResourceDO.getId();
        String instanceId = startTask(id, sysResourceDO.getTitle());
        dto.setResourceId(id);
        return insertResourceAudit(dto,instanceId);
    }
    
}
```


    
### 高可用系统构建
- [x] 严格遵循阿里规范，注重代码质量
- [x] 集群部署，减少单点故障
- [x] 服务限流
- [x] 超时和重试机制
- [x] 熔断机制
- [x] 异步调用
- [x] 多级缓存
- [x] 服务监控报警

### 演示地址
[http://175.178.69.253](http://175.178.69.253)  
admin/admin123  
test/test123  
laok5/test123  

### 项目截图
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
</table>

### 用户权益
- 采用Apache2.0开源协议，并且承诺永不参与商业用途，仅供大家无偿使用
- 采用Apache2.0开源协议，并且承诺永不参与商业用途，仅供大家无偿使用
- 采用Apache2.0开源协议，并且承诺永不参与商业用途，仅供大家无偿使用

### 参与贡献
欢迎各路英雄好汉参与KCloud-Platform-Alibaba代码贡献，期待您的加入！Fork本仓库 新建 feat_xxx_环境_时间（如 feat_laokou_dev_20230116） 分支提交代码，新建Pull Request

### 开源协议
KCloud-Platform-Alibaba 开源软件遵循 [Apache 2.0 协议](https://www.apache.org/licenses/LICENSE-2.0.html) 请务必保留作者、Copyright信息  
![](doc/image/25.png)

### 项目地址
Github 后端地址：[KCloud-Platform-Alibaba](https://github.com/KouShenhai/KCloud-Platform-Alibaba)  
Github 前端地址：[KCloud-Antdv-Alibaba](https://github.com/KouShenhai/KCloud-Antdv-Alibaba)  

Gtiee 后端地址：[KCloud-Platform-Alibaba](https://gitee.com/laokouyun/KCloud-Platform-Alibaba)  
Gitee 前端地址：[KCloud-Antdv-Alibaba](https://gitee.com/laokouyun/KCloud-Antdv-Alibaba)  

### 致谢
[Spring官网](https://spring.io)  
[人人社区](https://www.renren.io)  
[若依社区](https://www.ruoyi.vip)  

### 联系
博客：[https://kcloud.blog.csdn.net](https://kcloud.blog.csdn.net)  
邮箱：[2413176044@qq.com](https://mail.qq.com)  
QQ：[2413176044]( http://wpa.qq.com/msgrd?v=3&uin=2413176044&Site=gitee&Menu=yes)  
后端技术交流群 [![加入QQ群](https://img.shields.io/badge/Q群-218686225-blue.svg)](https://qm.qq.com/cgi-bin/qm/qr?k=WFANTXDEjrDw6UxsrRFCv_rQsEu6LTxH&jump_from=webapi)