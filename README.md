### 项目备注
* 项目：LaoKou-Cloud
* 作者：老寇
* 语言：Java
* 职位：Java工程师
* 时间：2020.06.08 ~ 至今

### 项目介绍
老寇云，是基于SpringCloud开发，面向Java编程的学习者，用于技术进阶，技术知识体系架构的构建，用生动的代码来感受技术的魅力。

使用的中间件有redis、elasticsearch等等

...

...

...

### 功能介绍
* SSO登录(账号密码登录、微信公众号登录、手机号登录、授权码登录、邮箱登录、支付宝登录)
* 视频通话
* 视频直播
* 好友聊天
* 订单管理（支付\购买\取消）
* 消息记录（敏感词过滤，高亮显示）
* 数据爬虫
* 资源管理（OA工作流审核、静态化）
* 代码生成

### 技术体系
#### 基础框架
* SpringBoot
* SpringCloud
* Shiro
* SpringSecurity

#### 技术栈
* mysql/hbase
* rabbitmq/rocketmq
* elasticsearch
* redis
* fastdfs
* netty/websocket
* docker+docker-compose
* freemarker/thymeleaf/velocity
* mybatis+mybatis-plus
* webmagic
* mongodb
* apollo/nacos

#### 一键部署
* docker + jinkens + shell
* docker + kubernates

#### 项目结构
~~~
KCloud
|--doc -- 相关安装文档
|--db -- 数据库相关sql
|--env -- 服务启动包
|--im -- 前端页面
|--laokou-api -- api网关
|--laokou-cloud
    |--laokou-dubbo -- dubbo模块
    |--laokou-feign -- feign模块
    |--laokou-gateway -- 服务网关
    |--laokou-monitor -- 服务监控
    |--laokou-register -- 服务治理
    |--laokou-sentinel -- 服务监控
    |--laokou-skywalking -- 服务监控
    |--laokou-sleuth -- 服务调用链
    |--laokou-turbine -- 服务监控
|--laokou-common -- 常用工具类
|--laokou-service
    |--laokou-activiti -- 工作流模块
    |--laokou-chat -- IM模块
    |--laokou-data -- api调用模块
    |--laokou-elasticsearch -- 搜索模块
    |--laokou-flv -- 直播模块
    |--laokou-freemarker -- 模板模块
    |--laokou-hbase -- 分布式数据库
    |--laokou-lock -- 分布式锁
    |--laokou-order -- 订单模块
    |--laokou-oss -- oss配置
    |--laokou-query -- 数据查询
    |--laokou-rabbitmq -- rabbitmq消息模块
    |--laokou-redis-tools -- redis模块
    |--laokou-resource -- 资源模块
    |--laokou-rocketmq -- rocketmq消息模块
    |--laokou-sensitive-words -- 敏感词模块
    |--laokou-ip -- ip模块
    |--laokou-sso 
        |--laokou-sso-captcha -- 验证码模块
        |--laokou-sso-security-auth -- sso登录模块
        |--laokou-sso-security-server -- sso授权码模块
        |--laokou-sso-auth -- sso登录模块
    |--laokou-third-party
        |--laokou-third-party-email -- 邮件模块
        |--laokou-third-party-pay -- 支付模块
        |--laokou-third-party-sms -- 短信模块
        |--laokou-third-party-wechat -- 微信模块
    |--laokou-video -- 视频通话模块
    |--laokou-webmagic -- 爬虫模块
    |--laokou-webservice -- webservice模块
    |--laokou-xxl-job -- xxl-job定时任务模块
~~~
### 项目配置
* 安装jdk1.8、mysql5.7、elasticsearch7.6.2、fastdfs、rabbitmq、redis、rocketmq、nginx+openresty+lua、mongodb、apollo、nacos
* 创建数据库 > 见db文件夹
* 修改第三方相关配置
* 修改中间件相关配置

```yaml
  # rabbitmq
  rabbitmq:
    # mq连接地址
    addresses: 127.0.0.1:5672
    # mq账号
    username: root
    # mq密码
    password: XXXXXX
  # redis 
  redis:
    # 连接地址
    host: 127.0.0.1
    # 端口号
    port: 6379
  # mysql
  datasource:
    druid:
      # 连接地址
      url: jdbc:mysql://127.0.0.1:3306/kcloud?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false
      # 用户名
      username: root
      # 密码
      password: XXXXXX
  # es
  elasticsearch:
    # 节点名称
    cluster-name: laokou-elasticsearch
    # 地址
    host: 127.0.0.1:9200
    # 账号
    username: elastic
    # 密码
    password: XXXXXX
```

### 写到最后
我深知个人的力量是有限的，欢迎小伙伴们加入...
###### 项目地址：[码云仓库](https://gitee.com/tttt_wmh_cn/KCloud.git)
###### qq：2413176044
###### qq群：218686225