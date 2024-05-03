/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : PostgreSQL
 Source Server Version : 160002 (160002)
 Source Host           : 127.0.0.1:5432
 Source Catalog        : kcloud_platform_iot_nacos
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 160002 (160002)
 File Encoding         : 65001

 Date: 02/05/2024 22:42:21
*/


-- ----------------------------
-- Sequence structure for config_info_aggr_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."config_info_aggr_id_seq";
CREATE SEQUENCE "public"."config_info_aggr_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for config_info_aggr_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."config_info_aggr_id_seq1";
CREATE SEQUENCE "public"."config_info_aggr_id_seq1"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for config_info_beta_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."config_info_beta_id_seq";
CREATE SEQUENCE "public"."config_info_beta_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for config_info_beta_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."config_info_beta_id_seq1";
CREATE SEQUENCE "public"."config_info_beta_id_seq1"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for config_info_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."config_info_id_seq";
CREATE SEQUENCE "public"."config_info_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for config_info_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."config_info_id_seq1";
CREATE SEQUENCE "public"."config_info_id_seq1"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for config_info_tag_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."config_info_tag_id_seq";
CREATE SEQUENCE "public"."config_info_tag_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for config_info_tag_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."config_info_tag_id_seq1";
CREATE SEQUENCE "public"."config_info_tag_id_seq1"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for config_tags_relation_nid_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."config_tags_relation_nid_seq";
CREATE SEQUENCE "public"."config_tags_relation_nid_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for config_tags_relation_nid_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."config_tags_relation_nid_seq1";
CREATE SEQUENCE "public"."config_tags_relation_nid_seq1"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for group_capacity_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."group_capacity_id_seq";
CREATE SEQUENCE "public"."group_capacity_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for group_capacity_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."group_capacity_id_seq1";
CREATE SEQUENCE "public"."group_capacity_id_seq1"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for his_config_info_nid_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."his_config_info_nid_seq";
CREATE SEQUENCE "public"."his_config_info_nid_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for his_config_info_nid_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."his_config_info_nid_seq1";
CREATE SEQUENCE "public"."his_config_info_nid_seq1"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for tenant_capacity_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."tenant_capacity_id_seq";
CREATE SEQUENCE "public"."tenant_capacity_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for tenant_capacity_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."tenant_capacity_id_seq1";
CREATE SEQUENCE "public"."tenant_capacity_id_seq1"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for tenant_info_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."tenant_info_id_seq";
CREATE SEQUENCE "public"."tenant_info_id_seq"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for tenant_info_id_seq1
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."tenant_info_id_seq1";
CREATE SEQUENCE "public"."tenant_info_id_seq1"
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."config_info";
CREATE TABLE "public"."config_info" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "data_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "group_id" varchar(128) COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "md5" varchar(32) COLLATE "pg_catalog"."default",
  "gmt_create" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "gmt_modified" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "src_user" text COLLATE "pg_catalog"."default",
  "src_ip" varchar(50) COLLATE "pg_catalog"."default",
  "app_name" varchar(128) COLLATE "pg_catalog"."default",
  "tenant_id" varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "c_desc" varchar(256) COLLATE "pg_catalog"."default",
  "c_use" varchar(64) COLLATE "pg_catalog"."default",
  "effect" varchar(64) COLLATE "pg_catalog"."default",
  "type" varchar(64) COLLATE "pg_catalog"."default",
  "c_schema" text COLLATE "pg_catalog"."default",
  "encrypted_data_key" text COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."config_info"."id" IS 'id';
COMMENT ON COLUMN "public"."config_info"."data_id" IS 'data_id';
COMMENT ON COLUMN "public"."config_info"."content" IS 'content';
COMMENT ON COLUMN "public"."config_info"."md5" IS 'md5';
COMMENT ON COLUMN "public"."config_info"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."config_info"."gmt_modified" IS '修改时间';
COMMENT ON COLUMN "public"."config_info"."src_user" IS 'source user';
COMMENT ON COLUMN "public"."config_info"."src_ip" IS 'source ip';
COMMENT ON COLUMN "public"."config_info"."tenant_id" IS '租户字段';
COMMENT ON COLUMN "public"."config_info"."encrypted_data_key" IS '秘钥';
COMMENT ON TABLE "public"."config_info" IS 'config_info';

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO "public"."config_info" VALUES (16, 'application-common-redis.yaml', 'LAOKOU_GROUP', '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  data:
    # redis
    redis:
      client-type: lettuce
      host: redis.laokou.org
      port: 6379
      password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
      connect-timeout: 60000ms #连接超时时长（毫秒）
      timeout: 60000ms #超时时长（毫秒）
      lettuce:
        pool:
          max-active: 20 #连接池最大连接数（使用负值表示无极限）
          max-wait: -1 #连接池最大阻塞等待时间（使用负值表示没有限制）
          max-idle: 10 #连接池最大空闲连接
          min-idle: 5 #连接池最小空间连接', 'c8c0e7fbaa49086163b00c1c8e1fa454', '2023-01-13 12:15:59', '2023-11-06 18:02:43', 'nacos', '0:0:0:0:0:0:0:1', '', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'redis公共配置', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (17, 'application-common.yaml', 'LAOKOU_GROUP', '# spring
spring:
  # security
  security:
    oauth2:
      resource-server:
        enabled: true
        request-matcher:
          ignore-patterns:
            GET:
              - /**/v3/api-docs/**=laokou-gateway
              - /v3/api-docs/**=laokou-auth,laokou-admin,laokou-flowable
              - /swagger-ui.html=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /swagger-ui/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /actuator/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /error=laokou-admin,laokou-auth
              - /v1/tenants/option-list=laokou-auth,laokou-gateway
              - /v1/tenants/id=laokou-auth,laokou-gateway
              - /favicon.ico=laokou-gateway
              - /v1/captchas/{uuid}=laokou-gateway,laokou-auth
              - /v1/secrets=laokou-gateway,laokou-auth
              - /graceful-shutdown=laokou-auth
              - /ws=laokou-gateway
            DELETE:
              - /v1/logouts=laokou-auth,laokou-gateway
  # task
  task-execution:
    thread-name-prefix: laokou-ttl-task-
    pool:
      core-size: 17
      keep-alive: 180s
  cloud:
    # 解决集成sentinel，openfeign启动报错，官方下个版本修复
    openfeign:
      compression:
        response:
          enabled: true
        request:
          enabled: true
      # FeignAutoConfiguration、OkHttpFeignLoadBalancerConfiguration、OkHttpClient#getClient、FeignClientProperties、OptionsFactoryBean#getObject
      # 在BeanFactory调用getBean()时，不是调用getBean，是调用getObject(),因此，getObject()相当于代理了getBean(),而且getObject()对Options初始化，是直接从openfeign.default获取配置值的
      okhttp:
        enabled: true
      circuitbreaker:
        enabled: true
      httpclient:
        enabled: false
        hc5:
          enabled: false
        disable-ssl-validation: true
      client:
        config:
          default:
            connectTimeout: 120000 #连接超时
            readTimeout: 120000 #读取超时
            logger-level: full
      lazy-attributes-resolution: true
    # sentinel
    sentinel:
      eager: true #开启饥饿加载，直接初始化
      transport:
        port: 8769
        dashboard: sentinel.laokou.org:8972

# actuator
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always

# springdoc
springdoc:
  swagger-ui:
    path: /swagger-ui.html

# server
server:
  servlet:
    encoding:
      charset: UTF-8
  undertow:
    threads:
      # 设置IO线程数，来执行非阻塞任务，负责多个连接数
      io: 16
      # 工作线程数
      worker: 256
    # 每块buffer的空间大小
    buffer-size: 1024
    # 分配堆外内存
    direct-buffers: true

# feign
feign:
  sentinel:
    enabled: true
    default-rule: default
    rules:
      # https://sentinelguard.io/zh-cn/docs/circuit-breaking.html
      default:
        - grade: 2 # 异常数策略
          count: 1 # 异常数模式下为对应的阈值
          timeWindow: 30 # 熔断时长，单位为 s（经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。ERROR_COUNT）
          statIntervalMs: 1000 # 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
          minRequestAmount: 5 # 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
tenant:
  domain-names:
    - laokou.org
    - laokouyun.org
    - laokou.org.cn', '221f193df273602fcd6e728e9bb98956', '2023-01-13 12:16:46', '2024-04-28 23:05:43', 'nacos', '0:0:0:0:0:0:0:1', '', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (26, 'router.json', 'LAOKOU_GROUP', '[
  {
    "id": "laokou-auth",
    "uri": "lb://laokou-auth",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/auth/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "auth",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "1"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/auth/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "2.0"
    },
    "order": 1
  },
  {
    "id": "laokou-admin",
    "uri": "lb://laokou-admin",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/admin/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "admin",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "1"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/admin/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "2.0"
    },
    "order": 1
  },
  {
    "id": "open-api",
    "uri": "http://127.0.0.1:5555",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/v3/api-docs/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "open-api",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/v3/api-docs/(?<path>.*)",
          "_genkey_1": "/$\\{path}/v3/api-docs"
        }
      }
    ],
    "metadata": {},
    "order": 1
  },
  {
    "id": "laokou-im",
    "uri": "lb:wss://laokou-im",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/im/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "im",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "1"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/im/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "2.0"
    },
    "order": 1
  },
  {
    "id": "laokou-flowable",
    "uri": "lb://laokou-flowable",
    "predicates": [
      {
        "name": "Path",
        "args": {
          "pattern": "/flowable/**"
        }
      },
      {
        "name": "Weight",
        "args": {
          "_genkey_0": "flowable",
          "_genkey_1": "100"
        }
      }
    ],
    "filters": [
      {
        "name": "StripPrefix",
        "args": {
          "parts": "1"
        }
      },
      {
        "name": "RewritePath",
        "args": {
          "_genkey_0": "/flowable/(?<path>.*)",
          "_genkey_1": "/$\\{path}"
        }
      }
    ],
    "metadata": {
      "version": "2.0"
    },
    "order": 1
  }
]', 'b773792ee46cfa7c4e87356c577820f4', '2023-01-13 15:44:25', '2023-12-21 17:49:19', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-gateway', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '动态路由配置', '', '', 'json', '', '');
INSERT INTO "public"."config_info" VALUES (82, 'application-monitor.yaml', 'LAOKOU_GROUP', '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  boot:
    # admin
    admin:
      discovery:
        ignored-services:
          - laokou-im
      notify:
        mail:
          from: 2413176044@qq.com
          to: 2413176044@qq.com
          template: META-INF/spring-boot-admin-server/mail/status-changed.html
  # security
  security:
    user:
      # root
      name: ENC(esZnNM2DrSxZhgTOzu11W2fVsJDDZ1b12aPopMMHCS7lF5+BJun9ri6y5pTUdj6L)
      # laokou123
      password: ENC(mHjKcITM5U60bq7M4fxh4yUQ9L3PPWPskvnWRE0PVxIqQ34Ztx7zOESwWCdjeWPW)
  # mail
  mail:
    host: smtp.qq.com
    password: ENC(JDMVbM278SLa9qbk5oMeJUs6St0dpLRSQyI7lPKGUFFzxSMZcfps2+vXBVyAXXB34Hj2GHzgRhEdDR1bX3eweA==)
    default-encoding: UTF-8
    username: 2413176044@qq.com

# actuator
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always', 'de1c37757329b0ad75488bf39024489c', '2023-01-16 12:01:23', '2023-07-21 12:37:26', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-monitor', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'spring boot admin monitor', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (103, 'application-common-elasticsearch.yaml', 'LAOKOU_GROUP', '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

spring:
  # elasticsearch
  elasticsearch:
    uris:
     - https://elasticsearch.laokou.org:9200
    username: ENC(svQedUe/LhX4+kE58LA73GTbkn0xR1Nz4P9hIalcloHMkQ8BCur8LiptBZ9DI78f)
    password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
    connection-timeout: 30s
    socket-timeout: 30s', '54cf98fa1e36856f525336acf7a5d20d', '2023-01-17 10:22:15', '2024-04-07 20:52:38', 'nacos', '0:0:0:0:0:0:0:1', '', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'elasticsearch公共配置', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (1254, 'application-common-seata.yaml', 'LAOKOU_GROUP', '# seata
seata:
  saga:
    enabled: true
    state-machine:
      enable-async: true
      table-prefix: seata_
      async-thread-pool:
        core-pool-size: 1
        max-pool-size: 20
        keep-alive-time: 60
      trans-operation-timeout: 1800000
      service-invoke-timeout: 300000
      auto-register-resources: true
      resources:
        - classpath*:seata/saga/statelang/**/*.json
      default-tenant-id: 0
      charset: UTF-8
  client:
    tm:
      default-global-transaction-timeout: 30000
  config:
    type: nacos
    nacos:
      server-addr: nacos.laokou.org
      namespace: a61abd4c-ef96-42a5-99a1-616adee531f3
      username: nacos
      password: nacos
      group: SEATA_GROUP
      data-id: seataServer.properties
  registry:
    type: nacos
    nacos:
      namespace: a61abd4c-ef96-42a5-99a1-616adee531f3
      group: SEATA_GROUP
      username: nacos
      password: nacos
      server-addr: nacos.laokou.org
  enabled: true
  tx-service-group: default_tx_group
  data-source-proxy-mode: AT', '32215c266a24f57c2adfa11ded3a6bbc', '2023-01-18 13:53:56', '2024-02-20 11:40:38', 'nacos', '0:0:0:0:0:0:0:1', '', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'seata公共配置', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (1270, 'application-common-rocketmq.yaml', 'LAOKOU_GROUP', 'rocketmq:
  producer:
    group: laokou_producer_group
  name-server: rocketmq.laokou.org:9876
  consumer:
    pull-batch-size: 16', '540e67a6edad605be9959a9652d383e2', '2023-01-21 10:43:04', '2023-11-06 18:10:30', 'nacos', '0:0:0:0:0:0:0:1', '', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'rocketmq公共配置', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (1273, 'application-gateway.yaml', 'LAOKOU_GROUP', '# springdoc
spring:
  cloud:
    gateway:
      ip:
        enabled: true
        label: black
springdoc:
  swagger-ui:
    urls:
      - name: admin
        url: /v3/api-docs/admin
      - name: auth
        url: /v3/api-docs/auth', 'fe482cd291193a1bd85295c5dd363221', '2023-01-22 13:16:13', '2024-05-01 00:44:08', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-gateway', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'gateway配置', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (1475, 'application-sms.yaml', 'LAOKOU_GROUP', 'sms:
  # 0：国阳云
  type: 0
  gyy:
    templateId: 908e94ccf08b4476ba6c876d13f084ad
    signId: 3f45af8aa12f4d59be8b1f18b650ad81
    appcode: 6b3e98d5f39848cba9615d576ce78d9e', 'c5d95dadb7e5b2fe9db27ce8cae73118', '2023-02-13 19:59:35', '2023-03-12 17:14:13', 'nacos', '8.8.8.8', 'laokou-sms', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'sms配置', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (1477, 'application-mail.yaml', 'LAOKOU_GROUP', 'spring:
  # mail
  mail:
    host: smtp.qq.com
    username: 2413176044@qq.com
    password: hhqkeodvfywfebaf
    default-encoding: UTF-8', '665c8cf5a8522b5f64ae86f9e816e89e', '2023-02-13 20:00:32', '2023-02-13 20:00:47', 'nacos', '192.168.62.1', 'laokou-mail', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'mail配置', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (1567, 'gateway-flow.json', 'LAOKOU_GROUP', '[
  {
    "resource": "laokou-auth",
    "grade": 1,
    "count": 300,
    "intervalSec": 1,
    "burst": 1000,
    "controlBehavior": 0
  },
  {
    "resource": "laokou-admin",
    "grade": 1,
    "count": 300,
    "intervalSec": 1,
    "burst": 1000,
    "controlBehavior": 0
  }
]', 'c5fbcf8031ce039bb97bd44cbfbca16b', '2023-02-26 14:59:20', '2023-04-01 14:42:04', 'nacos', '127.0.0.1', 'laokou-gateway', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'gateway sentinel flow rule', '', '', 'json', '', '');
INSERT INTO "public"."config_info" VALUES (1568, 'auth-flow.json', 'LAOKOU_GROUP', '[
  {
    "resource": "/v1/captchas/{uuid}",
    "limitApp": "default",
    "count": 300,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  },
  {
    "resource": "/v1/secrets",
    "limitApp": "default",
    "count": 300,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  }
]', '095ca881af8089664d3852771a743894', '2023-02-26 15:01:51', '2023-09-12 02:26:56', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-auth', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'auth sentinel  flow rule', '', '', 'json', '', '');
INSERT INTO "public"."config_info" VALUES (1569, 'admin-flow.json', 'LAOKOU_GROUP', '[
  {
    "resource": "/v1/users/profile",
    "limitApp": "default",
    "count": 300,
    "grade": 1,
    "strategy": 0,
    "controlBehavior": 0
  }
]', '9860e7087b9cf7707d78c2fedb740cab', '2023-02-26 15:03:07', '2023-09-12 02:27:49', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-admin', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'admin sentinel flow rule', '', '', 'json', '', '');
INSERT INTO "public"."config_info" VALUES (1570, 'admin-degrade.json', 'LAOKOU_GROUP', '[
  {
    "resource": "POST:https://laokou-flowable/work/task/api/query",
    "count": 200,
    "grade": 0,
    "slowRatioThreshold": 0.1,
    "minRequestAmount": 5,
    "timeWindow": 30
  },
  {
    "resource": "POST:https://laokou-flowable/work/definition/api/query",
    "count": 200,
    "grade": 0,
    "slowRatioThreshold": 0.1,
    "minRequestAmount": 5,
    "timeWindow": 30
  }
]', '2c0d4de0716f94bd6878b0a68d3faa0f', '2023-02-26 15:55:49', '2023-09-20 11:02:30', NULL, '127.0.0.1', 'laokou-admin', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'admin sentinel degrade rule', '', '', 'json', '', '');
INSERT INTO "public"."config_info" VALUES (1799, 'application-common-monitor.yaml', 'LAOKOU_GROUP', '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  # https://codecentric.github.io/spring-boot-admin/current
  boot:
    # admin
    admin:
      client:
        url: https://monitor.laokou.org:5000
        # root
        username: ENC(esZnNM2DrSxZhgTOzu11W2fVsJDDZ1b12aPopMMHCS7lF5+BJun9ri6y5pTUdj6L)
        # laokou123
        password: ENC(mHjKcITM5U60bq7M4fxh4yUQ9L3PPWPskvnWRE0PVxIqQ34Ztx7zOESwWCdjeWPW)', '7d934a9e4b0ebe9803a17ccd520dca3e', '2023-07-18 16:59:45', '2023-11-06 18:09:16', 'nacos', '0:0:0:0:0:0:0:1', '', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'monitor公共配置', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (2118, 'application-report.yaml', 'LAOKOU_GROUP', 'server:
  port: 8088', '89f7b26715cf760d099a258200381fe0', '2023-10-01 04:56:06', '2024-05-01 00:45:57', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-report', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (2159, 'application-common-kafka.yaml', 'LAOKOU_GROUP', 'spring:
  kafka:
    bootstrap-servers: kafka.laokou.org:9092
    consumer:
      # 禁用自动提交（按周期）已消费offset
      enable-auto-commit: false
      # 单次poll()调用返回的记录数
      max-poll-records: 50
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      # 发生错误后，消息重发的次数。
      retries: 5
      #当有多个消息需要被发送到同一个分区时，生产者会把它们放在同一个批次里。该参数指定了一个批次可以使用的内存大小，按照字节数计算。
      batch-size: 16384
      # 设置生产者内存缓冲区的大小。
      buffer-memory: 33554432
      # 键的序列化方式
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      # 值的序列化方式
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      # acks=0 ： 生产者在成功写入消息之前不会等待任何来自服务器的响应。
      # acks=1 ： 只要集群的首领节点收到消息，生产者就会收到一个来自服务器成功响应。
      # acks=all ：只有当所有参与复制的节点全部收到消息时，生产者才会收到一个来自服务器的成功响应。
      acks: 0
    listener:
      # 在侦听器容器中运行的线程数。
      concurrency: 5
      # listner负责ack，每调用一次，就立即commit
      ack-mode: manual
      # 批量batch类型
      type: batch
      # topic不存在报错
      missing-topics-fatal: false', '157c7aaa53329ababa1aab96b9878fad', '2023-10-26 08:54:23', '2023-11-06 18:14:00', 'nacos', '0:0:0:0:0:0:0:1', '', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'kafka公共配置', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (2178, 'application-logstash.yaml', 'LAOKOU_GROUP', 'spring:
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-logstash
      port: -1
      log-path: ./logs/xxl-job/laokou-logstash
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7', '81c3de6319db8560c68935977f2d141f', '2023-11-03 07:37:45', '2024-01-30 14:46:47', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-logstash', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (2260, 'seataServer.properties', 'SEATA_GROUP', '#For details about configuration items, see https://seata.io/zh-cn/docs/user/configurations.html
#Transport configuration, for client and server
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enableTmClientBatchSendRequest=false
transport.enableRmClientBatchSendRequest=true
transport.enableTcServerBatchSendResponse=false
transport.rpcRmRequestTimeout=30000
transport.rpcTmRequestTimeout=30000
transport.rpcTcRequestTimeout=30000
transport.threadFactory.bossThreadPrefix=NettyBoss
transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
transport.threadFactory.shareBossWorker=false
transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
transport.threadFactory.clientSelectorThreadSize=1
transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
transport.threadFactory.bossThreadSize=1
transport.threadFactory.workerThreadSize=default
transport.shutdown.wait=3
transport.serialization=seata
transport.compressor=none

#Transaction routing rules configuration, only for the client
service.vgroupMapping.default_tx_group=default
#If you use a registry, you can ignore it
service.default.grouplist=seata.laokou.org:8091
service.enableDegrade=false
service.disableGlobalTransaction=false

client.metadataMaxAgeMs=30000
#Transaction rule configuration, only for the client
client.rm.asyncCommitBufferLimit=10000
client.rm.lock.retryInterval=10
client.rm.lock.retryTimes=30
client.rm.lock.retryPolicyBranchRollbackOnConflict=true
client.rm.reportRetryCount=5
client.rm.tableMetaCheckEnable=true
client.rm.tableMetaCheckerInterval=60000
client.rm.sqlParserType=hikari
client.rm.reportSuccessEnable=false
client.rm.sagaBranchRegisterEnable=false
client.rm.sagaJsonParser=jackson
client.rm.tccActionInterceptorOrder=-2147482648
client.tm.commitRetryCount=5
client.tm.rollbackRetryCount=5
client.tm.defaultGlobalTransactionTimeout=60000
client.tm.degradeCheck=false
client.tm.degradeCheckAllowTimes=10
client.tm.degradeCheckPeriod=2000
client.tm.interceptorOrder=-2147482648
client.undo.dataValidation=true
client.undo.logSerialization=jackson
client.undo.onlyCareUpdateColumns=true
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
client.undo.logTable=undo_log
client.undo.compress.enable=true
client.undo.compress.type=zip
client.undo.compress.threshold=64k
#For TCC transaction mode
tcc.fence.logTableName=tcc_fence_log
tcc.fence.cleanPeriod=1h
# You can choose from the following options: fastjson, jackson, gson
tcc.contextJsonParserType=fastjson

#Log rule configuration, for client and server
log.exceptionRate=100

#Transaction storage configuration, only for the server. The file, db, and redis configuration values are optional.
store.mode=redis
store.lock.mode=redis
store.session.mode=redis
#Used for password encryption
store.publicKey=

#If `store.mode,store.lock.mode,store.session.mode` are not equal to `file`, you can remove the configuration block.
store.file.dir=file_store/data
store.file.maxBranchSessionSize=16384
store.file.maxGlobalSessionSize=512
store.file.fileWriteBufferCacheSize=16384
store.file.flushDiskMode=async
store.file.sessionReloadReadSize=100

#These configurations are required if the `store mode` is `db`. If `store.mode,store.lock.mode,store.session.mode` are not equal to `db`, you can remove the configuration block.
store.db.datasource=hikari
store.db.dbType=mysql
store.db.driverClassName=com.mysql.cj.jdbc.Driver
store.db.url=jdbc:mysql://mysql.laokou.org:3306/kcloud_platform_alibaba_seata?useUnicode=true&rewriteBatchedStatements=true
store.db.user=root
store.db.password=laokou123
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.distributedLockTable=distributed_lock
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000

#These configurations are required if the `store mode` is `redis`. If `store.mode,store.lock.mode,store.session.mode` are not equal to `redis`, you can remove the configuration block.
store.redis.mode=single
store.redis.type=pipeline
store.redis.single.host=redis.laokou.org
store.redis.single.port=6379
store.redis.sentinel.masterName=
store.redis.sentinel.sentinelHosts=
store.redis.sentinel.sentinelPassword=
store.redis.maxConn=10
store.redis.minConn=1
store.redis.maxTotal=100
store.redis.database=0
store.redis.password=laokou123
store.redis.queryLimit=100

#Transaction rule configuration, only for the server
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
server.distributedLockExpireTime=10000
server.session.branchAsyncQueueSize=5000
server.session.enableBranchAsyncRemove=false
server.enableParallelRequestHandle=true
server.enableParallelHandleBranch=false

server.raft.cluster=seata.laokou.org:7091,seata.laokou.org:7092,seata.laokou.org:7093
server.raft.snapshotInterval=600
server.raft.applyBatch=32
server.raft.maxAppendBufferSize=262144
server.raft.maxReplicatorInflightMsgs=256
server.raft.disruptorBufferSize=16384
server.raft.electionTimeoutMs=2000
server.raft.reporterEnabled=false
server.raft.reporterInitialDelay=60
server.raft.serialization=jackson
server.raft.compressor=none
server.raft.sync=true

#Metrics configuration, only for the server
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898', '26217408b5ee26c2fec37325ea785e2f', '2023-11-27 16:36:34', '2024-03-04 11:55:27', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-seata', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'seata配置', '', '', 'properties', '', '');
INSERT INTO "public"."config_info" VALUES (2307, 'application-common-flyway.yaml', 'LAOKOU_GROUP', 'spring:
  flyway:
    # 起始版本
    baseline-version: 0
    # 历史记录
    baseline-on-migrate: true
    # 避免数据被意外清空
    clean-disabled: false', 'd3ec18f7c8327f45c48a7783f6d48b64', '2024-01-03 07:54:41', '2024-01-03 07:54:41', 'nacos', '0:0:0:0:0:0:0:1', '', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'flyway公共配置', NULL, NULL, 'yaml', NULL, '');
INSERT INTO "public"."config_info" VALUES (2022, 'application-admin.yaml', 'LAOKOU_GROUP', '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  datasource:
    dynamic:
      # 默认false,建议线上关闭
      p6spy: false
      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: false
      datasource:
        master:
          type: com.zaxxer.hikari.HikariDataSource
          driver-class-name: org.postgresql.Driver
          url: jdbc:postgresql://postgresql.laokou.org:5432/kcloud_platform_alibaba?tcpKeepAlive=true&reWriteBatchedInserts=true&ApplicationName=flyway-postgresql&useSSL=false&reWriteBatchedInserts=true
          username: ENC(OuDQnY2CK0z2t+sq1ihFaFHWve1KjJoRo1aPyAghuRAf9ad3BO6AjcJRA+1b/nZw)
          password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
          # https://blog.csdn.net/u014644574/article/details/123680515
          hikari:
            pool-name: HikariCP
            connection-timeout: 180000
            validation-timeout: 3000
            idle-timeout: 180000
            max-lifetime: 1800000
            maximum-pool-size: 60
            minimum-idle: 10
            is-read-only: false
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-admin
      port: -1
      log-path: ./logs/xxl-job/laokou-admin
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7

# mybatis-plus
mybatis-plus:
  # 全局处理
  global-config:
    db-config:
      column-format: "`%s`"
  tenant:
    ignore-tables:
      - boot_sys_tenant
      - boot_sys_source
      - boot_sys_package_menu
      - boot_sys_package
      - boot_sys_login_log
    enabled: true
  slow-sql:
    enabled: true
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl', 'f76bc2377d5e6d17a93abaacc7acaf3b', '2023-09-28 11:37:33', '2024-05-02 22:04:32.901', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-admin', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '', '', '', 'yaml', '', '');
INSERT INTO "public"."config_info" VALUES (2025, 'application-auth.yaml', 'LAOKOU_GROUP', '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  datasource:
    dynamic:
      # 默认false,建议线上关闭
      p6spy: false
      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: false
      datasource:
        master:
          type: com.zaxxer.hikari.HikariDataSource
          driver-class-name: org.postgresql.Driver
          url: jdbc:postgresql://postgresql.laokou.org:5432/kcloud_platform_alibaba?tcpKeepAlive=true&reWriteBatchedInserts=true&ApplicationName=flyway-postgresql&useSSL=false&reWriteBatchedInserts=true
          username: ENC(OuDQnY2CK0z2t+sq1ihFaFHWve1KjJoRo1aPyAghuRAf9ad3BO6AjcJRA+1b/nZw)
          password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
          # https://blog.csdn.net/u014644574/article/details/123680515
          hikari:
            connection-timeout: 60000
            validation-timeout: 3000
            idle-timeout: 60000
            max-lifetime: 60000
            maximum-pool-size: 30
            minimum-idle: 10
            is-read-only: false
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-auth
      port: -1
      log-path: ./logs/xxl-job/laokou-auth
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7
# mybatis-plus
mybatis-plus:
  # 全局处理
  global-config:
    db-config:
      column-format: "`%s`"
  tenant:
    enabled: true
    ignore-tables:
      - boot_sys_source
      - boot_sys_tenant
  slow-sql:
    enabled: false
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl', '353f00bf9da6492ec4a97057de2bb332', '2023-09-28 11:51:44', '2024-05-02 22:05:32.256', 'nacos', '0:0:0:0:0:0:0:1', 'laokou-auth', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '', '', '', 'yaml', '', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS "public"."config_info_aggr";
CREATE TABLE "public"."config_info_aggr" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "data_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "group_id" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "datum_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "gmt_modified" timestamp(6) NOT NULL,
  "app_name" varchar(128) COLLATE "pg_catalog"."default",
  "tenant_id" varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying
)
;
COMMENT ON COLUMN "public"."config_info_aggr"."id" IS 'id';
COMMENT ON COLUMN "public"."config_info_aggr"."data_id" IS 'data_id';
COMMENT ON COLUMN "public"."config_info_aggr"."group_id" IS 'group_id';
COMMENT ON COLUMN "public"."config_info_aggr"."datum_id" IS 'datum_id';
COMMENT ON COLUMN "public"."config_info_aggr"."content" IS '内容';
COMMENT ON COLUMN "public"."config_info_aggr"."gmt_modified" IS '修改时间';
COMMENT ON COLUMN "public"."config_info_aggr"."tenant_id" IS '租户字段';
COMMENT ON TABLE "public"."config_info_aggr" IS '增加租户字段';

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS "public"."config_info_beta";
CREATE TABLE "public"."config_info_beta" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "data_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "group_id" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(128) COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "beta_ips" varchar(1024) COLLATE "pg_catalog"."default",
  "md5" varchar(32) COLLATE "pg_catalog"."default",
  "gmt_create" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "gmt_modified" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "src_user" text COLLATE "pg_catalog"."default",
  "src_ip" varchar(50) COLLATE "pg_catalog"."default",
  "tenant_id" varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "encrypted_data_key" text COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."config_info_beta"."id" IS 'id';
COMMENT ON COLUMN "public"."config_info_beta"."data_id" IS 'data_id';
COMMENT ON COLUMN "public"."config_info_beta"."group_id" IS 'group_id';
COMMENT ON COLUMN "public"."config_info_beta"."app_name" IS 'app_name';
COMMENT ON COLUMN "public"."config_info_beta"."content" IS 'content';
COMMENT ON COLUMN "public"."config_info_beta"."beta_ips" IS 'betaIps';
COMMENT ON COLUMN "public"."config_info_beta"."md5" IS 'md5';
COMMENT ON COLUMN "public"."config_info_beta"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."config_info_beta"."gmt_modified" IS '修改时间';
COMMENT ON COLUMN "public"."config_info_beta"."src_user" IS 'source user';
COMMENT ON COLUMN "public"."config_info_beta"."src_ip" IS 'source ip';
COMMENT ON COLUMN "public"."config_info_beta"."tenant_id" IS '租户字段';
COMMENT ON COLUMN "public"."config_info_beta"."encrypted_data_key" IS '秘钥';
COMMENT ON TABLE "public"."config_info_beta" IS 'config_info_beta';

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS "public"."config_info_tag";
CREATE TABLE "public"."config_info_tag" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "data_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "group_id" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "tenant_id" varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "tag_id" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(128) COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "md5" varchar(32) COLLATE "pg_catalog"."default",
  "gmt_create" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "gmt_modified" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "src_user" text COLLATE "pg_catalog"."default",
  "src_ip" varchar(50) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."config_info_tag"."id" IS 'id';
COMMENT ON COLUMN "public"."config_info_tag"."data_id" IS 'data_id';
COMMENT ON COLUMN "public"."config_info_tag"."group_id" IS 'group_id';
COMMENT ON COLUMN "public"."config_info_tag"."tenant_id" IS 'tenant_id';
COMMENT ON COLUMN "public"."config_info_tag"."tag_id" IS 'tag_id';
COMMENT ON COLUMN "public"."config_info_tag"."app_name" IS 'app_name';
COMMENT ON COLUMN "public"."config_info_tag"."content" IS 'content';
COMMENT ON COLUMN "public"."config_info_tag"."md5" IS 'md5';
COMMENT ON COLUMN "public"."config_info_tag"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."config_info_tag"."gmt_modified" IS '修改时间';
COMMENT ON COLUMN "public"."config_info_tag"."src_user" IS 'source user';
COMMENT ON COLUMN "public"."config_info_tag"."src_ip" IS 'source ip';
COMMENT ON TABLE "public"."config_info_tag" IS 'config_info_tag';

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS "public"."config_tags_relation";
CREATE TABLE "public"."config_tags_relation" (
  "id" int8 NOT NULL,
  "tag_name" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "tag_type" varchar(64) COLLATE "pg_catalog"."default",
  "data_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "group_id" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "tenant_id" varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "nid" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
)
)
;
COMMENT ON COLUMN "public"."config_tags_relation"."id" IS 'id';
COMMENT ON COLUMN "public"."config_tags_relation"."tag_name" IS 'tag_name';
COMMENT ON COLUMN "public"."config_tags_relation"."tag_type" IS 'tag_type';
COMMENT ON COLUMN "public"."config_tags_relation"."data_id" IS 'data_id';
COMMENT ON COLUMN "public"."config_tags_relation"."group_id" IS 'group_id';
COMMENT ON COLUMN "public"."config_tags_relation"."tenant_id" IS 'tenant_id';
COMMENT ON TABLE "public"."config_tags_relation" IS 'config_tag_relation';

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------
INSERT INTO "public"."config_tags_relation" VALUES (1477, 'mail', '', 'application-mail.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 172);
INSERT INTO "public"."config_tags_relation" VALUES (1475, 'sms', '', 'application-sms.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 346);
INSERT INTO "public"."config_tags_relation" VALUES (1567, 'gateway', '', 'gateway-flow.json', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 358);
INSERT INTO "public"."config_tags_relation" VALUES (82, 'monitor', '', 'application-monitor.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 469);
INSERT INTO "public"."config_tags_relation" VALUES (1570, 'admin', '', 'admin-degrade.json', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 496);
INSERT INTO "public"."config_tags_relation" VALUES (16, 'redis', '', 'application-common-redis.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 672);
INSERT INTO "public"."config_tags_relation" VALUES (1254, 'seata', '', 'application-common-seata.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 675);
INSERT INTO "public"."config_tags_relation" VALUES (1270, 'rocketmq', '', 'application-common-rocketmq.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 678);
INSERT INTO "public"."config_tags_relation" VALUES (2159, 'kafka', '', 'application-common-kafka.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 684);
INSERT INTO "public"."config_tags_relation" VALUES (2178, 'logstash', '', 'application-logstash.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 685);
INSERT INTO "public"."config_tags_relation" VALUES (103, 'elasticsearch', '', 'application-common-elasticsearch.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 689);
INSERT INTO "public"."config_tags_relation" VALUES (26, 'gateway', '', 'router.json', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 698);
INSERT INTO "public"."config_tags_relation" VALUES (17, 'common', '', 'application-common.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 714);
INSERT INTO "public"."config_tags_relation" VALUES (1273, 'gateway', '', 'application-gateway.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 717);
INSERT INTO "public"."config_tags_relation" VALUES (2118, 'report', '', 'application-report.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 720);
INSERT INTO "public"."config_tags_relation" VALUES (2022, 'admin', '', 'application-admin.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 1);
INSERT INTO "public"."config_tags_relation" VALUES (2025, 'auth', '', 'application-auth.yaml', 'LAOKOU_GROUP', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 2);

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS "public"."group_capacity";
CREATE TABLE "public"."group_capacity" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "group_id" varchar(128) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "quota" int8 NOT NULL DEFAULT 0,
  "usage" int8 NOT NULL DEFAULT 0,
  "max_size" int8 NOT NULL DEFAULT 0,
  "max_aggr_count" int8 NOT NULL DEFAULT 0,
  "max_aggr_size" int8 NOT NULL DEFAULT 0,
  "max_history_count" int8 NOT NULL DEFAULT 0,
  "gmt_create" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "gmt_modified" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."group_capacity"."id" IS '主键ID';
COMMENT ON COLUMN "public"."group_capacity"."group_id" IS 'Group ID，空字符表示整个集群';
COMMENT ON COLUMN "public"."group_capacity"."quota" IS '配额，0表示使用默认值';
COMMENT ON COLUMN "public"."group_capacity"."usage" IS '使用量';
COMMENT ON COLUMN "public"."group_capacity"."max_size" IS '单个配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN "public"."group_capacity"."max_aggr_count" IS '聚合子配置最大个数，，0表示使用默认值';
COMMENT ON COLUMN "public"."group_capacity"."max_aggr_size" IS '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN "public"."group_capacity"."max_history_count" IS '最大变更历史数量';
COMMENT ON COLUMN "public"."group_capacity"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."group_capacity"."gmt_modified" IS '修改时间';
COMMENT ON TABLE "public"."group_capacity" IS '集群、各Group容量信息表';

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."his_config_info";
CREATE TABLE "public"."his_config_info" (
  "id" int8 NOT NULL,
  "nid" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "data_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "group_id" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(128) COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "md5" varchar(32) COLLATE "pg_catalog"."default",
  "gmt_create" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "gmt_modified" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "src_user" text COLLATE "pg_catalog"."default",
  "src_ip" varchar(50) COLLATE "pg_catalog"."default",
  "op_type" char(10) COLLATE "pg_catalog"."default",
  "tenant_id" varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "encrypted_data_key" text COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."his_config_info"."app_name" IS 'app_name';
COMMENT ON COLUMN "public"."his_config_info"."tenant_id" IS '租户字段';
COMMENT ON COLUMN "public"."his_config_info"."encrypted_data_key" IS '秘钥';
COMMENT ON TABLE "public"."his_config_info" IS '多租户改造';

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO "public"."his_config_info" VALUES (17, 3092, 'application-common.yaml', 'LAOKOU_GROUP', '', '# spring
spring:
  # security
  security:
    oauth2:
      resource-server:
        enabled: true
        request-matcher:
          ignore-patterns:
            GET:
              - /**/v3/api-docs/**=laokou-gateway
              - /v3/api-docs/**=laokou-auth,laokou-admin,laokou-flowable
              - /swagger-ui.html=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /swagger-ui/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /actuator/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /error=laokou-admin,laokou-auth
              - /v1/tenants/option-list=laokou-admin,laokou-gateway
              - /v1/tenants/id=laokou-admin,laokou-gateway
              - /favicon.ico=laokou-gateway
              - /v1/captchas/{uuid}=laokou-gateway,laokou-auth
              - /v1/secrets=laokou-gateway,laokou-auth
              - /graceful-shutdown=laokou-auth
              - /ws=laokou-gateway
            DELETE:
              - /v1/logouts=laokou-admin,laokou-gateway
  # task
  task-execution:
    thread-name-prefix: laokou-ttl-task-
    pool:
      core-size: 17
      keep-alive: 180s
  cloud:
    # 解决集成sentinel，openfeign启动报错，官方下个版本修复
    openfeign:
      compression:
        response:
          enabled: true
        request:
          enabled: true
      # FeignAutoConfiguration、OkHttpFeignLoadBalancerConfiguration、OkHttpClient#getClient、FeignClientProperties、OptionsFactoryBean#getObject
      # 在BeanFactory调用getBean()时，不是调用getBean，是调用getObject(),因此，getObject()相当于代理了getBean(),而且getObject()对Options初始化，是直接从openfeign.default获取配置值的
      okhttp:
        enabled: true
      circuitbreaker:
        enabled: true
      httpclient:
        enabled: false
        hc5:
          enabled: false
        disable-ssl-validation: true
      client:
        config:
          default:
            connectTimeout: 120000 #连接超时
            readTimeout: 120000 #读取超时
            logger-level: full
      lazy-attributes-resolution: true
    # sentinel
    sentinel:
      eager: true #开启饥饿加载，直接初始化
      transport:
        port: 8769
        dashboard: sentinel.laokou.org:8972

# actuator
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always

# springdoc
springdoc:
  swagger-ui:
    path: /swagger-ui.html

# server
server:
  servlet:
    encoding:
      charset: UTF-8
  undertow:
    threads:
      # 设置IO线程数，来执行非阻塞任务，负责多个连接数
      io: 16
      # 工作线程数
      worker: 256
    # 每块buffer的空间大小
    buffer-size: 1024
    # 分配堆外内存
    direct-buffers: true

# feign
feign:
  sentinel:
    enabled: true
    default-rule: default
    rules:
      # https://sentinelguard.io/zh-cn/docs/circuit-breaking.html
      default:
        - grade: 2 # 异常数策略
          count: 1 # 异常数模式下为对应的阈值
          timeWindow: 30 # 熔断时长，单位为 s（经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。ERROR_COUNT）
          statIntervalMs: 1000 # 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
          minRequestAmount: 5 # 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断', '6c81d0baeaca6326a99e676137525dac', '2024-04-24 00:27:02', '2024-04-24 00:27:02', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (17, 3093, 'application-common.yaml', 'LAOKOU_GROUP', '', '# spring
spring:
  # security
  security:
    oauth2:
      resource-server:
        enabled: true
        request-matcher:
          ignore-patterns:
            GET:
              - /**/v3/api-docs/**=laokou-gateway
              - /v3/api-docs/**=laokou-auth,laokou-admin,laokou-flowable
              - /swagger-ui.html=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /swagger-ui/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /actuator/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /error=laokou-admin,laokou-auth
              - /tenants/v1/option-list=laokou-admin,laokou-gateway
              - /tenants/v1/id=laokou-admin,laokou-gateway
              - /favicon.ico=laokou-gateway
              - /captchas/v1/{uuid}=laokou-gateway,laokou-auth
              - /secrets/v1=laokou-gateway,laokou-auth
              - /graceful-shutdown=laokou-auth
              - /ws=laokou-gateway
            DELETE:
              - /logouts/v1=laokou-admin,laokou-gateway
  # task
  task-execution:
    thread-name-prefix: laokou-ttl-task-
    pool:
      core-size: 17
      keep-alive: 180s
  cloud:
    # 解决集成sentinel，openfeign启动报错，官方下个版本修复
    openfeign:
      compression:
        response:
          enabled: true
        request:
          enabled: true
      # FeignAutoConfiguration、OkHttpFeignLoadBalancerConfiguration、OkHttpClient#getClient、FeignClientProperties、OptionsFactoryBean#getObject
      # 在BeanFactory调用getBean()时，不是调用getBean，是调用getObject(),因此，getObject()相当于代理了getBean(),而且getObject()对Options初始化，是直接从openfeign.default获取配置值的
      okhttp:
        enabled: true
      circuitbreaker:
        enabled: true
      httpclient:
        enabled: false
        hc5:
          enabled: false
        disable-ssl-validation: true
      client:
        config:
          default:
            connectTimeout: 120000 #连接超时
            readTimeout: 120000 #读取超时
            logger-level: full
      lazy-attributes-resolution: true
    # sentinel
    sentinel:
      eager: true #开启饥饿加载，直接初始化
      transport:
        port: 8769
        dashboard: sentinel.laokou.org:8972

# actuator
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always

# springdoc
springdoc:
  swagger-ui:
    path: /swagger-ui.html

# server
server:
  servlet:
    encoding:
      charset: UTF-8
  undertow:
    threads:
      # 设置IO线程数，来执行非阻塞任务，负责多个连接数
      io: 16
      # 工作线程数
      worker: 256
    # 每块buffer的空间大小
    buffer-size: 1024
    # 分配堆外内存
    direct-buffers: true

# feign
feign:
  sentinel:
    enabled: true
    default-rule: default
    rules:
      # https://sentinelguard.io/zh-cn/docs/circuit-breaking.html
      default:
        - grade: 2 # 异常数策略
          count: 1 # 异常数模式下为对应的阈值
          timeWindow: 30 # 熔断时长，单位为 s（经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。ERROR_COUNT）
          statIntervalMs: 1000 # 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
          minRequestAmount: 5 # 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断', 'bb2c613f325f0bfdba75386ae79ce82a', '2024-04-24 00:29:36', '2024-04-24 00:29:36', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (17, 3094, 'application-common.yaml', 'LAOKOU_GROUP', '', '# spring
spring:
  # security
  security:
    oauth2:
      resource-server:
        enabled: true
        request-matcher:
          ignore-patterns:
            GET:
              - /**/v3/api-docs/**=laokou-gateway
              - /v3/api-docs/**=laokou-auth,laokou-admin,laokou-flowable
              - /swagger-ui.html=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /swagger-ui/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /actuator/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /error=laokou-admin,laokou-auth
              - /tenants/v1/option-list=laokou-admin,laokou-gateway
              - /tenants/v1/id=laokou-admin,laokou-gateway
              - /favicon.ico=laokou-gateway
              - /captchas/v1/{uuid}=laokou-gateway,laokou-auth
              - /secrets/v1=laokou-gateway,laokou-auth
              - /graceful-shutdown=laokou-auth
              - /ws=laokou-gateway
            DELETE:
              - /logouts/v1=laokou-admin,laokou-gateway
  # task
  task-execution:
    thread-name-prefix: laokou-ttl-task-
    pool:
      core-size: 17
      keep-alive: 180s
  cloud:
    # 解决集成sentinel，openfeign启动报错，官方下个版本修复
    openfeign:
      compression:
        response:
          enabled: true
        request:
          enabled: true
      # FeignAutoConfiguration、OkHttpFeignLoadBalancerConfiguration、OkHttpClient#getClient、FeignClientProperties、OptionsFactoryBean#getObject
      # 在BeanFactory调用getBean()时，不是调用getBean，是调用getObject(),因此，getObject()相当于代理了getBean(),而且getObject()对Options初始化，是直接从openfeign.default获取配置值的
      okhttp:
        enabled: true
      circuitbreaker:
        enabled: true
      httpclient:
        enabled: false
        hc5:
          enabled: false
        disable-ssl-validation: true
      client:
        config:
          default:
            connectTimeout: 120000 #连接超时
            readTimeout: 120000 #读取超时
            logger-level: full
      lazy-attributes-resolution: true
    # sentinel
    sentinel:
      eager: true #开启饥饿加载，直接初始化
      transport:
        port: 8769
        dashboard: sentinel.laokou.org:8972

# actuator
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always

# springdoc
springdoc:
  swagger-ui:
    path: /swagger-ui.html

# server
server:
  servlet:
    encoding:
      charset: UTF-8
  undertow:
    threads:
      # 设置IO线程数，来执行非阻塞任务，负责多个连接数
      io: 16
      # 工作线程数
      worker: 256
    # 每块buffer的空间大小
    buffer-size: 1024
    # 分配堆外内存
    direct-buffers: true

# feign
feign:
  sentinel:
    enabled: true
    default-rule: default
    rules:
      # https://sentinelguard.io/zh-cn/docs/circuit-breaking.html
      default:
        - grade: 2 # 异常数策略
          count: 1 # 异常数模式下为对应的阈值
          timeWindow: 30 # 熔断时长，单位为 s（经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。ERROR_COUNT）
          statIntervalMs: 1000 # 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
          minRequestAmount: 5 # 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断', 'bb2c613f325f0bfdba75386ae79ce82a', '2024-04-25 23:39:17', '2024-04-25 23:39:18', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (2022, 3095, 'laokou-admin.yaml', 'LAOKOU_GROUP', 'laokou-admin', '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  datasource:
    dynamic:
      # 默认false,建议线上关闭
      p6spy: false
      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: false
      datasource:
        # user:
        #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
        #   url: jdbc:shardingsphere:nacos:application-admin.yaml
        # login_log:
        #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
        #   url: jdbc:shardingsphere:nacos:application-auth.yaml
        master:
          type: com.zaxxer.hikari.HikariDataSource
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://mysql.laokou.org:3306/kcloud_platform_alibaba?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&nullCatalogMeansCurrent=true
          username: ENC(OuDQnY2CK0z2t+sq1ihFaFHWve1KjJoRo1aPyAghuRAf9ad3BO6AjcJRA+1b/nZw)
          password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
          # https://blog.csdn.net/u014644574/article/details/123680515
          hikari:
            pool-name: HikariCP
            connection-timeout: 180000
            validation-timeout: 3000
            idle-timeout: 180000
            max-lifetime: 1800000
            maximum-pool-size: 60
            minimum-idle: 10
            is-read-only: false
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-admin
      port: -1
      log-path: ./logs/xxl-job/laokou-admin
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7
  default-config:
    # 流程KEY
    definition-key: "Process_88888888"
    # 租户表
    tenant-tables:
      - boot_sys_audit_log
      - boot_sys_dept
      - boot_sys_dict
      - boot_sys_menu
      - boot_sys_message
      - boot_sys_message_detail
      - boot_sys_operate_log
      - boot_sys_oss
      - boot_sys_oss_log
      - boot_sys_resource
      - boot_sys_resource_audit
      - boot_sys_role
      - boot_sys_role_dept
      - boot_sys_role_menu
      - boot_sys_user
      - boot_sys_user_role
      - undo_log
      - boot_sys_domain_event
    domain-names:
      - laokou.org
      - laokouyun.org
      - laokou.org.cn
    tenant-prefix: "tenant"
    graceful-shutdown-services:
      - "laokou-flowable"
      - "laokou-admin"
      - "laokou-auth"
# mybatis-plus
mybatis-plus:
  # 全局处理
  global-config:
    db-config:
      column-format: "`%s`"
  tenant:
    ignore-tables:
      - boot_sys_tenant
      - boot_sys_source
      - boot_sys_package_menu
      - boot_sys_package
      - boot_sys_login_log
    enabled: true
  slow-sql:
    enabled: true
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl

dubbo:
  application:
    id: laokou-admin
    name: laokou-admin
    qos-port: 33333
  protocol:
    id: dubbo
    name: dubbo
    port: -1
  registry:
    address: nacos://nacos.laokou.org:8848
    username: nacos
    group: DUBBO_GROUP
    password: nacos
    parameters:
      namespace: a61abd4c-ef96-42a5-99a1-616adee531f3
      register-consumer-url: true
    protocol: nacos
  metadata-report:
    address: nacos://nacos.laokou.org:8848
    username: nacos
    group: DUBBO_GROUP
    password: nacos
    parameters:
      namespace: a61abd4c-ef96-42a5-99a1-616adee531f3
  config-center:
    address: nacos://nacos.laokou.org:8848
    username: nacos
    group: DUBBO_GROUP
    password: nacos
    parameters:
      namespace: a61abd4c-ef96-42a5-99a1-616adee531f3
  consumer:
    check: false
    timeout: 5000', '27e831d95ed0f2d69b3821d2dfa9e76d', '2024-04-28 22:56:04', '2024-04-28 22:56:05', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (17, 3096, 'application-common.yaml', 'LAOKOU_GROUP', '', '# spring
spring:
  # security
  security:
    oauth2:
      resource-server:
        enabled: true
        request-matcher:
          ignore-patterns:
            GET:
              - /**/v3/api-docs/**=laokou-gateway
              - /v3/api-docs/**=laokou-auth,laokou-admin,laokou-flowable
              - /swagger-ui.html=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /swagger-ui/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /actuator/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /error=laokou-admin,laokou-auth
              - /v1/tenants/option-list=laokou-auth,laokou-gateway
              - /v1/tenants/id=laokou-auth,laokou-gateway
              - /favicon.ico=laokou-gateway
              - /v1/captchas/{uuid}=laokou-gateway,laokou-auth
              - /v1/secrets=laokou-gateway,laokou-auth
              - /graceful-shutdown=laokou-auth
              - /ws=laokou-gateway
            DELETE:
              - /v1/logouts=laokou-auth,laokou-gateway
  # task
  task-execution:
    thread-name-prefix: laokou-ttl-task-
    pool:
      core-size: 17
      keep-alive: 180s
  cloud:
    # 解决集成sentinel，openfeign启动报错，官方下个版本修复
    openfeign:
      compression:
        response:
          enabled: true
        request:
          enabled: true
      # FeignAutoConfiguration、OkHttpFeignLoadBalancerConfiguration、OkHttpClient#getClient、FeignClientProperties、OptionsFactoryBean#getObject
      # 在BeanFactory调用getBean()时，不是调用getBean，是调用getObject(),因此，getObject()相当于代理了getBean(),而且getObject()对Options初始化，是直接从openfeign.default获取配置值的
      okhttp:
        enabled: true
      circuitbreaker:
        enabled: true
      httpclient:
        enabled: false
        hc5:
          enabled: false
        disable-ssl-validation: true
      client:
        config:
          default:
            connectTimeout: 120000 #连接超时
            readTimeout: 120000 #读取超时
            logger-level: full
      lazy-attributes-resolution: true
    # sentinel
    sentinel:
      eager: true #开启饥饿加载，直接初始化
      transport:
        port: 8769
        dashboard: sentinel.laokou.org:8972

# actuator
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always

# springdoc
springdoc:
  swagger-ui:
    path: /swagger-ui.html

# server
server:
  servlet:
    encoding:
      charset: UTF-8
  undertow:
    threads:
      # 设置IO线程数，来执行非阻塞任务，负责多个连接数
      io: 16
      # 工作线程数
      worker: 256
    # 每块buffer的空间大小
    buffer-size: 1024
    # 分配堆外内存
    direct-buffers: true

# feign
feign:
  sentinel:
    enabled: true
    default-rule: default
    rules:
      # https://sentinelguard.io/zh-cn/docs/circuit-breaking.html
      default:
        - grade: 2 # 异常数策略
          count: 1 # 异常数模式下为对应的阈值
          timeWindow: 30 # 熔断时长，单位为 s（经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。ERROR_COUNT）
          statIntervalMs: 1000 # 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
          minRequestAmount: 5 # 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断', '29935119ede975f71f6d9eaa6c8175d2', '2024-04-28 22:57:19', '2024-04-28 22:57:19', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (17, 3097, 'application-common.yaml', 'LAOKOU_GROUP', '', '# spring
spring:
  # security
  security:
    oauth2:
      resource-server:
        enabled: true
        request-matcher:
          ignore-patterns:
            GET:
              - /**/v3/api-docs/**=laokou-gateway
              - /v3/api-docs/**=laokou-auth,laokou-admin,laokou-flowable
              - /swagger-ui.html=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /swagger-ui/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /actuator/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /error=laokou-admin,laokou-auth
              - /v1/tenants/option-list=laokou-auth,laokou-gateway
              - /v1/tenants/id=laokou-auth,laokou-gateway
              - /favicon.ico=laokou-gateway
              - /v1/captchas/{uuid}=laokou-gateway,laokou-auth
              - /v1/secrets=laokou-gateway,laokou-auth
              - /graceful-shutdown=laokou-auth
              - /ws=laokou-gateway
            DELETE:
              - /v1/logouts=laokou-auth,laokou-gateway
  # task
  task-execution:
    thread-name-prefix: laokou-ttl-task-
    pool:
      core-size: 17
      keep-alive: 180s
  cloud:
    # 解决集成sentinel，openfeign启动报错，官方下个版本修复
    openfeign:
      compression:
        response:
          enabled: true
        request:
          enabled: true
      # FeignAutoConfiguration、OkHttpFeignLoadBalancerConfiguration、OkHttpClient#getClient、FeignClientProperties、OptionsFactoryBean#getObject
      # 在BeanFactory调用getBean()时，不是调用getBean，是调用getObject(),因此，getObject()相当于代理了getBean(),而且getObject()对Options初始化，是直接从openfeign.default获取配置值的
      okhttp:
        enabled: true
      circuitbreaker:
        enabled: true
      httpclient:
        enabled: false
        hc5:
          enabled: false
        disable-ssl-validation: true
      client:
        config:
          default:
            connectTimeout: 120000 #连接超时
            readTimeout: 120000 #读取超时
            logger-level: full
      lazy-attributes-resolution: true
    # sentinel
    sentinel:
      eager: true #开启饥饿加载，直接初始化
      transport:
        port: 8769
        dashboard: sentinel.laokou.org:8972

# actuator
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always

# springdoc
springdoc:
  swagger-ui:
    path: /swagger-ui.html

# server
server:
  servlet:
    encoding:
      charset: UTF-8
  undertow:
    threads:
      # 设置IO线程数，来执行非阻塞任务，负责多个连接数
      io: 16
      # 工作线程数
      worker: 256
    # 每块buffer的空间大小
    buffer-size: 1024
    # 分配堆外内存
    direct-buffers: true

# feign
feign:
  sentinel:
    enabled: true
    default-rule: default
    rules:
      # https://sentinelguard.io/zh-cn/docs/circuit-breaking.html
      default:
        - grade: 2 # 异常数策略
          count: 1 # 异常数模式下为对应的阈值
          timeWindow: 30 # 熔断时长，单位为 s（经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。ERROR_COUNT）
          statIntervalMs: 1000 # 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
          minRequestAmount: 5 # 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
tenant:
  domain-name:
    - laokou.org
    - laokouyun.org
    - laokou.org.cn', '2c16be020e834b1e4730ec9c123b86cd', '2024-04-28 22:57:36', '2024-04-28 22:57:36', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (17, 3098, 'application-common.yaml', 'LAOKOU_GROUP', '', '# spring
spring:
  # security
  security:
    oauth2:
      resource-server:
        enabled: true
        request-matcher:
          ignore-patterns:
            GET:
              - /**/v3/api-docs/**=laokou-gateway
              - /v3/api-docs/**=laokou-auth,laokou-admin,laokou-flowable
              - /swagger-ui.html=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /swagger-ui/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /actuator/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /error=laokou-admin,laokou-auth
              - /v1/tenants/option-list=laokou-auth,laokou-gateway
              - /v1/tenants/id=laokou-auth,laokou-gateway
              - /favicon.ico=laokou-gateway
              - /v1/captchas/{uuid}=laokou-gateway,laokou-auth
              - /v1/secrets=laokou-gateway,laokou-auth
              - /graceful-shutdown=laokou-auth
              - /ws=laokou-gateway
            DELETE:
              - /v1/logouts=laokou-auth,laokou-gateway
  # task
  task-execution:
    thread-name-prefix: laokou-ttl-task-
    pool:
      core-size: 17
      keep-alive: 180s
  cloud:
    # 解决集成sentinel，openfeign启动报错，官方下个版本修复
    openfeign:
      compression:
        response:
          enabled: true
        request:
          enabled: true
      # FeignAutoConfiguration、OkHttpFeignLoadBalancerConfiguration、OkHttpClient#getClient、FeignClientProperties、OptionsFactoryBean#getObject
      # 在BeanFactory调用getBean()时，不是调用getBean，是调用getObject(),因此，getObject()相当于代理了getBean(),而且getObject()对Options初始化，是直接从openfeign.default获取配置值的
      okhttp:
        enabled: true
      circuitbreaker:
        enabled: true
      httpclient:
        enabled: false
        hc5:
          enabled: false
        disable-ssl-validation: true
      client:
        config:
          default:
            connectTimeout: 120000 #连接超时
            readTimeout: 120000 #读取超时
            logger-level: full
      lazy-attributes-resolution: true
    # sentinel
    sentinel:
      eager: true #开启饥饿加载，直接初始化
      transport:
        port: 8769
        dashboard: sentinel.laokou.org:8972

# actuator
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always

# springdoc
springdoc:
  swagger-ui:
    path: /swagger-ui.html

# server
server:
  servlet:
    encoding:
      charset: UTF-8
  undertow:
    threads:
      # 设置IO线程数，来执行非阻塞任务，负责多个连接数
      io: 16
      # 工作线程数
      worker: 256
    # 每块buffer的空间大小
    buffer-size: 1024
    # 分配堆外内存
    direct-buffers: true

# feign
feign:
  sentinel:
    enabled: true
    default-rule: default
    rules:
      # https://sentinelguard.io/zh-cn/docs/circuit-breaking.html
      default:
        - grade: 2 # 异常数策略
          count: 1 # 异常数模式下为对应的阈值
          timeWindow: 30 # 熔断时长，单位为 s（经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。ERROR_COUNT）
          statIntervalMs: 1000 # 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
          minRequestAmount: 5 # 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
tenant:
  domain-name:
    - laokou.org
    - laokouyun.org
    - laokou.org.cn', '2c16be020e834b1e4730ec9c123b86cd', '2024-04-28 22:58:55', '2024-04-28 22:58:56', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (17, 3099, 'application-common.yaml', 'LAOKOU_GROUP', '', '# spring
spring:
  # security
  security:
    oauth2:
      resource-server:
        enabled: true
        request-matcher:
          ignore-patterns:
            GET:
              - /**/v3/api-docs/**=laokou-gateway
              - /v3/api-docs/**=laokou-auth,laokou-admin,laokou-flowable
              - /swagger-ui.html=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /swagger-ui/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /actuator/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /error=laokou-admin,laokou-auth
              - /v1/tenants/option-list=laokou-auth,laokou-gateway
              - /v1/tenants/id=laokou-auth,laokou-gateway
              - /favicon.ico=laokou-gateway
              - /v1/captchas/{uuid}=laokou-gateway,laokou-auth
              - /v1/secrets=laokou-gateway,laokou-auth
              - /graceful-shutdown=laokou-auth
              - /ws=laokou-gateway
            DELETE:
              - /v1/logouts=laokou-auth,laokou-gateway
  # task
  task-execution:
    thread-name-prefix: laokou-ttl-task-
    pool:
      core-size: 17
      keep-alive: 180s
  cloud:
    # 解决集成sentinel，openfeign启动报错，官方下个版本修复
    openfeign:
      compression:
        response:
          enabled: true
        request:
          enabled: true
      # FeignAutoConfiguration、OkHttpFeignLoadBalancerConfiguration、OkHttpClient#getClient、FeignClientProperties、OptionsFactoryBean#getObject
      # 在BeanFactory调用getBean()时，不是调用getBean，是调用getObject(),因此，getObject()相当于代理了getBean(),而且getObject()对Options初始化，是直接从openfeign.default获取配置值的
      okhttp:
        enabled: true
      circuitbreaker:
        enabled: true
      httpclient:
        enabled: false
        hc5:
          enabled: false
        disable-ssl-validation: true
      client:
        config:
          default:
            connectTimeout: 120000 #连接超时
            readTimeout: 120000 #读取超时
            logger-level: full
      lazy-attributes-resolution: true
    # sentinel
    sentinel:
      eager: true #开启饥饿加载，直接初始化
      transport:
        port: 8769
        dashboard: sentinel.laokou.org:8972

# actuator
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always

# springdoc
springdoc:
  swagger-ui:
    path: /swagger-ui.html

# server
server:
  servlet:
    encoding:
      charset: UTF-8
  undertow:
    threads:
      # 设置IO线程数，来执行非阻塞任务，负责多个连接数
      io: 16
      # 工作线程数
      worker: 256
    # 每块buffer的空间大小
    buffer-size: 1024
    # 分配堆外内存
    direct-buffers: true

# feign
feign:
  sentinel:
    enabled: true
    default-rule: default
    rules:
      # https://sentinelguard.io/zh-cn/docs/circuit-breaking.html
      default:
        - grade: 2 # 异常数策略
          count: 1 # 异常数模式下为对应的阈值
          timeWindow: 30 # 熔断时长，单位为 s（经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。ERROR_COUNT）
          statIntervalMs: 1000 # 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
          minRequestAmount: 5 # 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
tenant:
  domain-name:
    - laokou.org
    - laokouyun.org
    - laokou.org.cn', '2c16be020e834b1e4730ec9c123b86cd', '2024-04-28 23:05:11', '2024-04-28 23:05:11', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (17, 3100, 'application-common.yaml', 'LAOKOU_GROUP', '', '# spring
spring:
  # security
  security:
    oauth2:
      resource-server:
        enabled: true
        request-matcher:
          ignore-patterns:
            GET:
              - /**/v3/api-docs/**=laokou-gateway
              - /v3/api-docs/**=laokou-auth,laokou-admin,laokou-flowable
              - /swagger-ui.html=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /swagger-ui/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /actuator/**=laokou-admin,laokou-gateway,laokou-auth,laokou-flowable
              - /error=laokou-admin,laokou-auth
              - /v1/tenants/option-list=laokou-auth,laokou-gateway
              - /v1/tenants/id=laokou-auth,laokou-gateway
              - /favicon.ico=laokou-gateway
              - /v1/captchas/{uuid}=laokou-gateway,laokou-auth
              - /v1/secrets=laokou-gateway,laokou-auth
              - /graceful-shutdown=laokou-auth
              - /ws=laokou-gateway
            DELETE:
              - /v1/logouts=laokou-auth,laokou-gateway
  # task
  task-execution:
    thread-name-prefix: laokou-ttl-task-
    pool:
      core-size: 17
      keep-alive: 180s
  cloud:
    # 解决集成sentinel，openfeign启动报错，官方下个版本修复
    openfeign:
      compression:
        response:
          enabled: true
        request:
          enabled: true
      # FeignAutoConfiguration、OkHttpFeignLoadBalancerConfiguration、OkHttpClient#getClient、FeignClientProperties、OptionsFactoryBean#getObject
      # 在BeanFactory调用getBean()时，不是调用getBean，是调用getObject(),因此，getObject()相当于代理了getBean(),而且getObject()对Options初始化，是直接从openfeign.default获取配置值的
      okhttp:
        enabled: true
      circuitbreaker:
        enabled: true
      httpclient:
        enabled: false
        hc5:
          enabled: false
        disable-ssl-validation: true
      client:
        config:
          default:
            connectTimeout: 120000 #连接超时
            readTimeout: 120000 #读取超时
            logger-level: full
      lazy-attributes-resolution: true
    # sentinel
    sentinel:
      eager: true #开启饥饿加载，直接初始化
      transport:
        port: 8769
        dashboard: sentinel.laokou.org:8972

# actuator
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always

# springdoc
springdoc:
  swagger-ui:
    path: /swagger-ui.html

# server
server:
  servlet:
    encoding:
      charset: UTF-8
  undertow:
    threads:
      # 设置IO线程数，来执行非阻塞任务，负责多个连接数
      io: 16
      # 工作线程数
      worker: 256
    # 每块buffer的空间大小
    buffer-size: 1024
    # 分配堆外内存
    direct-buffers: true

# feign
feign:
  sentinel:
    enabled: true
    default-rule: default
    rules:
      # https://sentinelguard.io/zh-cn/docs/circuit-breaking.html
      default:
        - grade: 2 # 异常数策略
          count: 1 # 异常数模式下为对应的阈值
          timeWindow: 30 # 熔断时长，单位为 s（经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。ERROR_COUNT）
          statIntervalMs: 1000 # 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
          minRequestAmount: 5 # 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
tenant:
  domain-names:
    - laokou.org
    - laokouyun.org
    - laokou.org.cn', '221f193df273602fcd6e728e9bb98956', '2024-04-28 23:05:42', '2024-04-28 23:05:43', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (2019, 3101, 'application-admin.yaml', 'LAOKOU_GROUP', 'laokou-admin', 'dataSources:
  master:
    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://mysql.laokou.org:3306/kcloud_platform_alibaba_user?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false
    username: ENC(S4jBojv0XuQainIWrctlEwIfdx8hyaW1sUObBtFl9hNYw/SKRtggkwSK9HzCu79HP4ozdAHBsMbA26dJvjAwhQs2vScxpIqIo7CkT7+1OMqj2nb5QmwnimayyBJ+ywcMnNqD9aHQMt7RsbuDf+O5rTWBcT4nCIYU8v/dGh4TqYVSGhHg9/7N6EQoRleh0v3fJVK7SPYqO3sgD4N87G5ZhsLrk+eKLy3RQeReOof8+dB75nhwoF8nTWa9kVu9EWFvmzPb/nvAiPtw2Vj95AjZy3tMJA4NpML5U/AEUJsydaK4Snez0boBHO1/1suOIRa8xkGvuiIwYf6KKjdXL9WQ8g==)
    password: ENC(NWHFvL+KeMdcxkj2FMy0frxi9IScZ91WlH81NcDu8PA0mEvIM4CHdIC0LjsGOzrBVRcB69MdqCe8gTBPAtHUdvMaczjQKNSL4t0bvSf0Qamq3e2EgPHEpr5z6+K4XgBtUEw3VH/pWvL45C6/KAm7C6cSvUUFW53ju/mhD9tVacPGhHk004QgH9WmUwGdsMT9tckKQ2z7BuaXXoFVkGl9TsgPEYrSdySGKjFt12bMElaf8ANwoo3DlZ8tzGvEnLm2tGFxP1pvaheU1K3jUwQsFx0gUmWEW/rbrZngfSk07NDl4XrnnpOuTlknnoys3epIz71niqNFjAhE0gCTocAhIw==)
  slave:
    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://mysql.laokou.org:3306/kcloud_platform_alibaba_user?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false
    username: ENC(S4jBojv0XuQainIWrctlEwIfdx8hyaW1sUObBtFl9hNYw/SKRtggkwSK9HzCu79HP4ozdAHBsMbA26dJvjAwhQs2vScxpIqIo7CkT7+1OMqj2nb5QmwnimayyBJ+ywcMnNqD9aHQMt7RsbuDf+O5rTWBcT4nCIYU8v/dGh4TqYVSGhHg9/7N6EQoRleh0v3fJVK7SPYqO3sgD4N87G5ZhsLrk+eKLy3RQeReOof8+dB75nhwoF8nTWa9kVu9EWFvmzPb/nvAiPtw2Vj95AjZy3tMJA4NpML5U/AEUJsydaK4Snez0boBHO1/1suOIRa8xkGvuiIwYf6KKjdXL9WQ8g==)
    password: ENC(NWHFvL+KeMdcxkj2FMy0frxi9IScZ91WlH81NcDu8PA0mEvIM4CHdIC0LjsGOzrBVRcB69MdqCe8gTBPAtHUdvMaczjQKNSL4t0bvSf0Qamq3e2EgPHEpr5z6+K4XgBtUEw3VH/pWvL45C6/KAm7C6cSvUUFW53ju/mhD9tVacPGhHk004QgH9WmUwGdsMT9tckKQ2z7BuaXXoFVkGl9TsgPEYrSdySGKjFt12bMElaf8ANwoo3DlZ8tzGvEnLm2tGFxP1pvaheU1K3jUwQsFx0gUmWEW/rbrZngfSk07NDl4XrnnpOuTlknnoys3epIz71niqNFjAhE0gCTocAhIw==)
rules:
- !SHARDING
  tables:
    boot_sys_user:
      actualDataNodes: master.boot_sys_user_$->{2022..2099}0$->{1..9},master.boot_sys_user_$->{2022..2099}$->{10..12}
      tableStrategy:
        standard:
          shardingColumn: create_date
          shardingAlgorithmName: laokou_table_inline
      keyGenerateStrategy:
        column: id
        keyGeneratorName: snowflake
  shardingAlgorithms:
    laokou_table_inline:
      # 时间范围分片算法
      type: INTERVAL
      props:
          # 分片键的时间戳格式
        datetime-pattern: "yyyy-MM-dd HH:mm:ss"
        # 真实表的后缀格式
        sharding-suffix-pattern: "yyyyMM"
        # 时间分片下界值
        datetime-lower: "2022-01-01 00:00:00"
        # 时间分片上界值
        datetime-upper: "2099-12-31 23:59:59"
        # 分片间隔
        datetime-interval-amount: 1
        # 按月分表
        datetime-interval-unit: "months"
  keyGenerators:
    snowflake:
      type: SNOWFLAKE
      props:
        work-id: 123
- !READWRITE_SPLITTING
  dataSources:
    laokou_readwrite_data_sources:
      writeDataSourceName: master
      readDataSourceNames:
        - slave
      loadBalancerName: laokou_load_balance_algorithm
  loadBalancers:
    laokou_load_balance_algorithm:
      type: ROUND_ROBIN
props:
  sql-show: true', '4e7db6504ca86b9cd654ad2f340239c4', '2024-05-01 00:30:54', '2024-05-01 00:30:55', 'nacos', '0:0:0:0:0:0:0:1', 'D         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (2024, 3102, 'application-auth.yaml', 'LAOKOU_GROUP', 'laokou-auth', 'dataSources:
  master:
    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://mysql.laokou.org:3306/kcloud_platform_alibaba_login_log?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false
    username: ENC(S4jBojv0XuQainIWrctlEwIfdx8hyaW1sUObBtFl9hNYw/SKRtggkwSK9HzCu79HP4ozdAHBsMbA26dJvjAwhQs2vScxpIqIo7CkT7+1OMqj2nb5QmwnimayyBJ+ywcMnNqD9aHQMt7RsbuDf+O5rTWBcT4nCIYU8v/dGh4TqYVSGhHg9/7N6EQoRleh0v3fJVK7SPYqO3sgD4N87G5ZhsLrk+eKLy3RQeReOof8+dB75nhwoF8nTWa9kVu9EWFvmzPb/nvAiPtw2Vj95AjZy3tMJA4NpML5U/AEUJsydaK4Snez0boBHO1/1suOIRa8xkGvuiIwYf6KKjdXL9WQ8g==)
    password: ENC(NWHFvL+KeMdcxkj2FMy0frxi9IScZ91WlH81NcDu8PA0mEvIM4CHdIC0LjsGOzrBVRcB69MdqCe8gTBPAtHUdvMaczjQKNSL4t0bvSf0Qamq3e2EgPHEpr5z6+K4XgBtUEw3VH/pWvL45C6/KAm7C6cSvUUFW53ju/mhD9tVacPGhHk004QgH9WmUwGdsMT9tckKQ2z7BuaXXoFVkGl9TsgPEYrSdySGKjFt12bMElaf8ANwoo3DlZ8tzGvEnLm2tGFxP1pvaheU1K3jUwQsFx0gUmWEW/rbrZngfSk07NDl4XrnnpOuTlknnoys3epIz71niqNFjAhE0gCTocAhIw==)
  slave:
    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://mysql.laokou.org:3306/kcloud_platform_alibaba_login_log?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false
    username: ENC(S4jBojv0XuQainIWrctlEwIfdx8hyaW1sUObBtFl9hNYw/SKRtggkwSK9HzCu79HP4ozdAHBsMbA26dJvjAwhQs2vScxpIqIo7CkT7+1OMqj2nb5QmwnimayyBJ+ywcMnNqD9aHQMt7RsbuDf+O5rTWBcT4nCIYU8v/dGh4TqYVSGhHg9/7N6EQoRleh0v3fJVK7SPYqO3sgD4N87G5ZhsLrk+eKLy3RQeReOof8+dB75nhwoF8nTWa9kVu9EWFvmzPb/nvAiPtw2Vj95AjZy3tMJA4NpML5U/AEUJsydaK4Snez0boBHO1/1suOIRa8xkGvuiIwYf6KKjdXL9WQ8g==)
    password: ENC(NWHFvL+KeMdcxkj2FMy0frxi9IScZ91WlH81NcDu8PA0mEvIM4CHdIC0LjsGOzrBVRcB69MdqCe8gTBPAtHUdvMaczjQKNSL4t0bvSf0Qamq3e2EgPHEpr5z6+K4XgBtUEw3VH/pWvL45C6/KAm7C6cSvUUFW53ju/mhD9tVacPGhHk004QgH9WmUwGdsMT9tckKQ2z7BuaXXoFVkGl9TsgPEYrSdySGKjFt12bMElaf8ANwoo3DlZ8tzGvEnLm2tGFxP1pvaheU1K3jUwQsFx0gUmWEW/rbrZngfSk07NDl4XrnnpOuTlknnoys3epIz71niqNFjAhE0gCTocAhIw==)
rules:
- !SHARDING
  tables:
    boot_sys_login_log:
      actualDataNodes: master.boot_sys_login_log_$->{2022..2099}0$->{1..9},master.boot_sys_login_log_$->{2022..2099}$->{10..12}
      tableStrategy:
        standard:
          shardingColumn: create_date
          shardingAlgorithmName: laokou_table_inline
      keyGenerateStrategy:
        column: id
        keyGeneratorName: snowflake
  shardingAlgorithms:
    laokou_table_inline:
      # 时间范围分片算法
      type: INTERVAL
      props:
        # 分片键的时间戳格式
        datetime-pattern: "yyyy-MM-dd HH:mm:ss"
        # 真实表的后缀格式
        sharding-suffix-pattern: "yyyyMM"
        # 时间分片下界值
        datetime-lower: "2022-01-01 00:00:00"
        # 时间分片上界值
        datetime-upper: "2099-12-31 23:59:59"
        # 分片间隔
        datetime-interval-amount: 1
        # 按月分表
        datetime-interval-unit: "months"
  keyGenerators:
    snowflake:
      type: SNOWFLAKE
- !READWRITE_SPLITTING
  dataSources:
    laokou_readwrite_data_sources:
      writeDataSourceName: master
      readDataSourceNames:
        - slave
      loadBalancerName: laokou_load_balance_algorithm
  loadBalancers:
    laokou_load_balance_algorithm:
      type: ROUND_ROBIN
props:
  sql-show: true', '9f432a8f73ee3da06e1d33333f9919e3', '2024-05-01 00:33:22', '2024-05-01 00:33:22', 'nacos', '0:0:0:0:0:0:0:1', 'D         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (2022, 3103, 'application-admin.yaml', 'LAOKOU_GROUP', 'laokou-admin', '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  datasource:
    dynamic:
      # 默认false,建议线上关闭
      p6spy: false
      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: false
      datasource:
        # user:
        #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
        #   url: jdbc:shardingsphere:nacos:application-admin.yaml
        # login_log:
        #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
        #   url: jdbc:shardingsphere:nacos:application-auth.yaml
        master:
          type: com.zaxxer.hikari.HikariDataSource
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://mysql.laokou.org:3306/kcloud_platform_alibaba?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&nullCatalogMeansCurrent=true
          username: ENC(OuDQnY2CK0z2t+sq1ihFaFHWve1KjJoRo1aPyAghuRAf9ad3BO6AjcJRA+1b/nZw)
          password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
          # https://blog.csdn.net/u014644574/article/details/123680515
          hikari:
            pool-name: HikariCP
            connection-timeout: 180000
            validation-timeout: 3000
            idle-timeout: 180000
            max-lifetime: 1800000
            maximum-pool-size: 60
            minimum-idle: 10
            is-read-only: false
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-admin
      port: -1
      log-path: ./logs/xxl-job/laokou-admin
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7

# mybatis-plus
mybatis-plus:
  # 全局处理
  global-config:
    db-config:
      column-format: "`%s`"
  tenant:
    ignore-tables:
      - boot_sys_tenant
      - boot_sys_source
      - boot_sys_package_menu
      - boot_sys_package
      - boot_sys_login_log
    enabled: true
  slow-sql:
    enabled: true
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl

dubbo:
  application:
    id: laokou-admin
    name: laokou-admin
    qos-port: 33333
  protocol:
    id: dubbo
    name: dubbo
    port: -1
  registry:
    address: nacos://nacos.laokou.org:8848
    username: nacos
    group: DUBBO_GROUP
    password: nacos
    parameters:
      namespace: a61abd4c-ef96-42a5-99a1-616adee531f3
      register-consumer-url: true
    protocol: nacos
  metadata-report:
    address: nacos://nacos.laokou.org:8848
    username: nacos
    group: DUBBO_GROUP
    password: nacos
    parameters:
      namespace: a61abd4c-ef96-42a5-99a1-616adee531f3
  config-center:
    address: nacos://nacos.laokou.org:8848
    username: nacos
    group: DUBBO_GROUP
    password: nacos
    parameters:
      namespace: a61abd4c-ef96-42a5-99a1-616adee531f3
  consumer:
    check: false
    timeout: 5000', 'ca28ada7d25f9453cdff023df2264c52', '2024-05-01 00:37:36', '2024-05-01 00:37:36', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (1273, 3104, 'application-gateway.yaml', 'LAOKOU_GROUP', 'laokou-gateway', '# springdoc
spring:
  cloud:
    gateway:
      ip:
        enabled: true
        label: black
springdoc:
  swagger-ui:
    urls:
      - name: admin
        url: /v3/api-docs/admin
      - name: auth
        url: /v3/api-docs/auth
      - name: flowable
        url: /v3/api-docs/flowable', 'dfe0bf056bc6b23ff650195d96a2af9a', '2024-05-01 00:43:07', '2024-05-01 00:43:08', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (1273, 3105, 'application-gateway.yaml', 'LAOKOU_GROUP', 'laokou-gateway', '# springdoc
spring:
  cloud:
    gateway:
      ip:
        enabled: true
        label: black
springdoc:
  swagger-ui:
    urls:
      - name: admin
        url: /v3/api-docs/admin
      - name: auth
        url: /v3/api-docs/auth', 'fe482cd291193a1bd85295c5dd363221', '2024-05-01 00:44:07', '2024-05-01 00:44:08', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (2022, 3106, 'application-admin.yaml', 'LAOKOU_GROUP', 'laokou-admin', '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  datasource:
    dynamic:
      # 默认false,建议线上关闭
      p6spy: false
      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: false
      datasource:
        # user:
        #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
        #   url: jdbc:shardingsphere:nacos:application-admin.yaml
        # login_log:
        #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
        #   url: jdbc:shardingsphere:nacos:application-auth.yaml
        master:
          type: com.zaxxer.hikari.HikariDataSource
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://mysql.laokou.org:3306/kcloud_platform_alibaba?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&nullCatalogMeansCurrent=true
          username: ENC(OuDQnY2CK0z2t+sq1ihFaFHWve1KjJoRo1aPyAghuRAf9ad3BO6AjcJRA+1b/nZw)
          password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
          # https://blog.csdn.net/u014644574/article/details/123680515
          hikari:
            pool-name: HikariCP
            connection-timeout: 180000
            validation-timeout: 3000
            idle-timeout: 180000
            max-lifetime: 1800000
            maximum-pool-size: 60
            minimum-idle: 10
            is-read-only: false
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-admin
      port: -1
      log-path: ./logs/xxl-job/laokou-admin
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7

# mybatis-plus
mybatis-plus:
  # 全局处理
  global-config:
    db-config:
      column-format: "`%s`"
  tenant:
    ignore-tables:
      - boot_sys_tenant
      - boot_sys_source
      - boot_sys_package_menu
      - boot_sys_package
      - boot_sys_login_log
    enabled: true
  slow-sql:
    enabled: true
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl', 'eea3569acf6c717f2ccce3ee06e5f88f', '2024-05-01 00:45:11', '2024-05-01 00:45:11', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (2025, 3107, 'application-auth.yaml', 'LAOKOU_GROUP', 'laokou-auth', '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  datasource:
    dynamic:
      # 默认false,建议线上关闭
      p6spy: false
      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: false
      datasource:
        # user:
        #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
        #   url: jdbc:shardingsphere:nacos:application-admin.yaml
        # login_log:
        #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
        #   url: jdbc:shardingsphere:nacos:application-auth.yaml
        master:
          type: com.zaxxer.hikari.HikariDataSource
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://mysql.laokou.org:3306/kcloud_platform_alibaba?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true
          username: ENC(OuDQnY2CK0z2t+sq1ihFaFHWve1KjJoRo1aPyAghuRAf9ad3BO6AjcJRA+1b/nZw)
          password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
          # https://blog.csdn.net/u014644574/article/details/123680515
          hikari:
            connection-timeout: 60000
            validation-timeout: 3000
            idle-timeout: 60000
            max-lifetime: 60000
            maximum-pool-size: 30
            minimum-idle: 10
            is-read-only: false
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-auth
      port: -1
      log-path: ./logs/xxl-job/laokou-auth
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7
# mybatis-plus
mybatis-plus:
  # 全局处理
  global-config:
    db-config:
      column-format: "`%s`"
  tenant:
    enabled: true
    ignore-tables:
      - boot_sys_source
      - boot_sys_tenant
  slow-sql:
    enabled: false
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl', '876fb86d698399263367c44eebf27939', '2024-05-01 00:45:23', '2024-05-01 00:45:23', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (2118, 3108, 'application-report.yaml', 'LAOKOU_GROUP', 'laokou-report', 'dubbo:
  application:
    id: laokou-report
    name: laokou-report
    qos-port: 33334
  registry:
    address: nacos://nacos.laokou.org:8848
    username: nacos
    group: DUBBO_GROUP
    password: nacos
    parameters:
      namespace: a61abd4c-ef96-42a5-99a1-616adee531f3
  metadata-report:
    address: nacos://nacos.laokou.org:8848
    username: nacos
    group: DUBBO_GROUP
    password: nacos
    parameters:
      namespace: a61abd4c-ef96-42a5-99a1-616adee531f3
  config-center:
    address: nacos://nacos.laokou.org:8848
    username: nacos
    group: DUBBO_GROUP
    password: nacos
    parameters:
      namespace: a61abd4c-ef96-42a5-99a1-616adee531f3
  protocol:
    id: dubbo
    name: dubbo
    port: -1
  provider:
    threads: 20000
    threadpool: fixed
    timeout: 5000
  scan:
    base-packages: org.laokou.report.api
  consumer:
    check: false
    timeout: 5000', 'c47794c8c0ea33ae0a745b1fbef0530a', '2024-05-01 00:45:56', '2024-05-01 00:45:57', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (2022, 1, 'application-admin.yaml', 'LAOKOU_GROUP', 'laokou-admin', '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  datasource:
    dynamic:
      # 默认false,建议线上关闭
      p6spy: false
      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: false
      datasource:
        master:
          type: com.zaxxer.hikari.HikariDataSource
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://mysql.laokou.org:3306/kcloud_platform_alibaba?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&nullCatalogMeansCurrent=true
          username: ENC(OuDQnY2CK0z2t+sq1ihFaFHWve1KjJoRo1aPyAghuRAf9ad3BO6AjcJRA+1b/nZw)
          password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
          # https://blog.csdn.net/u014644574/article/details/123680515
          hikari:
            pool-name: HikariCP
            connection-timeout: 180000
            validation-timeout: 3000
            idle-timeout: 180000
            max-lifetime: 1800000
            maximum-pool-size: 60
            minimum-idle: 10
            is-read-only: false
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-admin
      port: -1
      log-path: ./logs/xxl-job/laokou-admin
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7

# mybatis-plus
mybatis-plus:
  # 全局处理
  global-config:
    db-config:
      column-format: "`%s`"
  tenant:
    ignore-tables:
      - boot_sys_tenant
      - boot_sys_source
      - boot_sys_package_menu
      - boot_sys_package
      - boot_sys_login_log
    enabled: true
  slow-sql:
    enabled: true
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl', 'abf8b80ecc7a316ef9a33914660d89c6', '2024-05-02 22:04:32.900232', '2024-05-02 22:04:32.911', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');
INSERT INTO "public"."his_config_info" VALUES (2025, 2, 'application-auth.yaml', 'LAOKOU_GROUP', 'laokou-auth', '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  datasource:
    dynamic:
      # 默认false,建议线上关闭
      p6spy: false
      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: false
      datasource:
        master:
          type: com.zaxxer.hikari.HikariDataSource
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://mysql.laokou.org:3306/kcloud_platform_alibaba?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false&nullCatalogMeansCurrent=true
          username: ENC(OuDQnY2CK0z2t+sq1ihFaFHWve1KjJoRo1aPyAghuRAf9ad3BO6AjcJRA+1b/nZw)
          password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
          # https://blog.csdn.net/u014644574/article/details/123680515
          hikari:
            connection-timeout: 60000
            validation-timeout: 3000
            idle-timeout: 60000
            max-lifetime: 60000
            maximum-pool-size: 30
            minimum-idle: 10
            is-read-only: false
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-auth
      port: -1
      log-path: ./logs/xxl-job/laokou-auth
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7
# mybatis-plus
mybatis-plus:
  # 全局处理
  global-config:
    db-config:
      column-format: "`%s`"
  tenant:
    enabled: true
    ignore-tables:
      - boot_sys_source
      - boot_sys_tenant
  slow-sql:
    enabled: false
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl', 'c5e285742492d088cfbd6b3e8f9c0a0b', '2024-05-02 22:05:32.256382', '2024-05-02 22:05:32.261', 'nacos', '0:0:0:0:0:0:0:1', 'U         ', 'a61abd4c-ef96-42a5-99a1-616adee531f3', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS "public"."permissions";
CREATE TABLE "public"."permissions" (
  "role" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "resource" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "action" varchar(8) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Records of permissions
-- ----------------------------
INSERT INTO "public"."permissions" VALUES ('ADMIN', ':*:*', 'rw');
INSERT INTO "public"."permissions" VALUES ('ADMIN', 'a61abd4c-ef96-42a5-99a1-616adee531f3:*:*', 'rw');
INSERT INTO "public"."permissions" VALUES ('ROLE_ADMIN', ':*:*', 'rw');
INSERT INTO "public"."permissions" VALUES ('ROLE_ADMIN', 'a61abd4c-ef96-42a5-99a1-616adee531f3:*:*', 'rw');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS "public"."roles";
CREATE TABLE "public"."roles" (
  "username" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "role" varchar(50) COLLATE "pg_catalog"."default" NOT NULL
)
;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO "public"."roles" VALUES ('laokou', 'ADMIN');
INSERT INTO "public"."roles" VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS "public"."tenant_capacity";
CREATE TABLE "public"."tenant_capacity" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "tenant_id" varchar(128) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "quota" int8 NOT NULL DEFAULT 0,
  "usage" int8 NOT NULL DEFAULT 0,
  "max_size" int8 NOT NULL DEFAULT 0,
  "max_aggr_count" int8 NOT NULL DEFAULT 0,
  "max_aggr_size" int8 NOT NULL DEFAULT 0,
  "max_history_count" int8 NOT NULL DEFAULT 0,
  "gmt_create" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "gmt_modified" timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."tenant_capacity"."id" IS '主键ID';
COMMENT ON COLUMN "public"."tenant_capacity"."tenant_id" IS 'Tenant ID';
COMMENT ON COLUMN "public"."tenant_capacity"."quota" IS '配额，0表示使用默认值';
COMMENT ON COLUMN "public"."tenant_capacity"."usage" IS '使用量';
COMMENT ON COLUMN "public"."tenant_capacity"."max_size" IS '单个配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN "public"."tenant_capacity"."max_aggr_count" IS '聚合子配置最大个数';
COMMENT ON COLUMN "public"."tenant_capacity"."max_aggr_size" IS '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';
COMMENT ON COLUMN "public"."tenant_capacity"."max_history_count" IS '最大变更历史数量';
COMMENT ON COLUMN "public"."tenant_capacity"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."tenant_capacity"."gmt_modified" IS '修改时间';
COMMENT ON TABLE "public"."tenant_capacity" IS '租户容量信息表';

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."tenant_info";
CREATE TABLE "public"."tenant_info" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
  "kp" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "tenant_id" varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "tenant_name" varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "tenant_desc" varchar(256) COLLATE "pg_catalog"."default",
  "create_source" varchar(32) COLLATE "pg_catalog"."default",
  "gmt_create" int8 NOT NULL,
  "gmt_modified" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."tenant_info"."id" IS 'id';
COMMENT ON COLUMN "public"."tenant_info"."kp" IS 'kp';
COMMENT ON COLUMN "public"."tenant_info"."tenant_id" IS 'tenant_id';
COMMENT ON COLUMN "public"."tenant_info"."tenant_name" IS 'tenant_name';
COMMENT ON COLUMN "public"."tenant_info"."tenant_desc" IS 'tenant_desc';
COMMENT ON COLUMN "public"."tenant_info"."create_source" IS 'create_source';
COMMENT ON COLUMN "public"."tenant_info"."gmt_create" IS '创建时间';
COMMENT ON COLUMN "public"."tenant_info"."gmt_modified" IS '修改时间';
COMMENT ON TABLE "public"."tenant_info" IS 'tenant_info';

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO "public"."tenant_info" VALUES (2, '1', 'a61abd4c-ef96-42a5-99a1-616adee531f3', 'laokou', 'laokou', 'nacos', 1673556960289, 1673556960289);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS "public"."users";
CREATE TABLE "public"."users" (
  "username" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "enabled" int2 NOT NULL
)
;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO "public"."users" VALUES ('laokou', '$2a$10$75WIn2J5FoX9F5wEBdFsL.0cKdv5h8QqBMKMWBABhWAxKB4TO8WZq', 1);
INSERT INTO "public"."users" VALUES ('nacos', '$2a$10$oVX1zRtaql9Jbsyzaaovx.TU2M6Bw0ZpCbPYWOIED58d1ougzaFRm', 1);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."config_info_aggr_id_seq"
OWNED BY "public"."config_info_aggr"."id";
SELECT setval('"public"."config_info_aggr_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."config_info_aggr_id_seq1"
OWNED BY "public"."config_info_aggr"."id";
SELECT setval('"public"."config_info_aggr_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."config_info_beta_id_seq"
OWNED BY "public"."config_info_beta"."id";
SELECT setval('"public"."config_info_beta_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."config_info_beta_id_seq1"
OWNED BY "public"."config_info_beta"."id";
SELECT setval('"public"."config_info_beta_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."config_info_id_seq"
OWNED BY "public"."config_info"."id";
SELECT setval('"public"."config_info_id_seq"', 2307, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."config_info_id_seq1"
OWNED BY "public"."config_info"."id";
SELECT setval('"public"."config_info_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."config_info_tag_id_seq"
OWNED BY "public"."config_info_tag"."id";
SELECT setval('"public"."config_info_tag_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."config_info_tag_id_seq1"
OWNED BY "public"."config_info_tag"."id";
SELECT setval('"public"."config_info_tag_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."config_tags_relation_nid_seq"
OWNED BY "public"."config_tags_relation"."nid";
SELECT setval('"public"."config_tags_relation_nid_seq"', 720, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."config_tags_relation_nid_seq1"
OWNED BY "public"."config_tags_relation"."nid";
SELECT setval('"public"."config_tags_relation_nid_seq1"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."group_capacity_id_seq"
OWNED BY "public"."group_capacity"."id";
SELECT setval('"public"."group_capacity_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."group_capacity_id_seq1"
OWNED BY "public"."group_capacity"."id";
SELECT setval('"public"."group_capacity_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."his_config_info_nid_seq"
OWNED BY "public"."his_config_info"."nid";
SELECT setval('"public"."his_config_info_nid_seq"', 3108, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."his_config_info_nid_seq1"
OWNED BY "public"."his_config_info"."nid";
SELECT setval('"public"."his_config_info_nid_seq1"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."tenant_capacity_id_seq"
OWNED BY "public"."tenant_capacity"."id";
SELECT setval('"public"."tenant_capacity_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."tenant_capacity_id_seq1"
OWNED BY "public"."tenant_capacity"."id";
SELECT setval('"public"."tenant_capacity_id_seq1"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."tenant_info_id_seq"
OWNED BY "public"."tenant_info"."id";
SELECT setval('"public"."tenant_info_id_seq"', 2, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."tenant_info_id_seq1"
OWNED BY "public"."tenant_info"."id";
SELECT setval('"public"."tenant_info_id_seq1"', 1, false);

-- ----------------------------
-- Auto increment value for config_info
-- ----------------------------
SELECT setval('"public"."config_info_id_seq1"', 1, false);

-- ----------------------------
-- Indexes structure for table config_info
-- ----------------------------
CREATE UNIQUE INDEX "config_info_data_id_group_id_tenant_id_idx" ON "public"."config_info" USING btree (
  "data_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "tenant_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table config_info
-- ----------------------------
ALTER TABLE "public"."config_info" ADD CONSTRAINT "config_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for config_info_aggr
-- ----------------------------
SELECT setval('"public"."config_info_aggr_id_seq1"', 1, false);

-- ----------------------------
-- Indexes structure for table config_info_aggr
-- ----------------------------
CREATE UNIQUE INDEX "config_info_aggr_data_id_group_id_tenant_id_datum_id_idx" ON "public"."config_info_aggr" USING btree (
  "data_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "tenant_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "datum_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table config_info_aggr
-- ----------------------------
ALTER TABLE "public"."config_info_aggr" ADD CONSTRAINT "config_info_aggr_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for config_info_beta
-- ----------------------------
SELECT setval('"public"."config_info_beta_id_seq1"', 1, false);

-- ----------------------------
-- Indexes structure for table config_info_beta
-- ----------------------------
CREATE UNIQUE INDEX "config_info_beta_data_id_group_id_tenant_id_idx" ON "public"."config_info_beta" USING btree (
  "data_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "tenant_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table config_info_beta
-- ----------------------------
ALTER TABLE "public"."config_info_beta" ADD CONSTRAINT "config_info_beta_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for config_info_tag
-- ----------------------------
SELECT setval('"public"."config_info_tag_id_seq1"', 1, false);

-- ----------------------------
-- Indexes structure for table config_info_tag
-- ----------------------------
CREATE UNIQUE INDEX "config_info_tag_data_id_group_id_tenant_id_tag_id_idx" ON "public"."config_info_tag" USING btree (
  "data_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "group_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "tenant_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "tag_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table config_info_tag
-- ----------------------------
ALTER TABLE "public"."config_info_tag" ADD CONSTRAINT "config_info_tag_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for config_tags_relation
-- ----------------------------
SELECT setval('"public"."config_tags_relation_nid_seq1"', 2, true);

-- ----------------------------
-- Indexes structure for table config_tags_relation
-- ----------------------------
CREATE UNIQUE INDEX "config_tags_relation_id_tag_name_tag_type_idx" ON "public"."config_tags_relation" USING btree (
  "id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "tag_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "tag_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "config_tags_relation_tenant_id_idx" ON "public"."config_tags_relation" USING btree (
  "tenant_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table config_tags_relation
-- ----------------------------
ALTER TABLE "public"."config_tags_relation" ADD CONSTRAINT "config_tags_relation_pkey" PRIMARY KEY ("nid");

-- ----------------------------
-- Auto increment value for group_capacity
-- ----------------------------
SELECT setval('"public"."group_capacity_id_seq1"', 1, false);

-- ----------------------------
-- Indexes structure for table group_capacity
-- ----------------------------
CREATE UNIQUE INDEX "group_capacity_group_id_idx" ON "public"."group_capacity" USING btree (
  "group_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table group_capacity
-- ----------------------------
ALTER TABLE "public"."group_capacity" ADD CONSTRAINT "group_capacity_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for his_config_info
-- ----------------------------
SELECT setval('"public"."his_config_info_nid_seq1"', 2, true);

-- ----------------------------
-- Indexes structure for table his_config_info
-- ----------------------------
CREATE INDEX "his_config_info_data_id_idx" ON "public"."his_config_info" USING btree (
  "data_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "his_config_info_gmt_create_idx" ON "public"."his_config_info" USING btree (
  "gmt_create" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);
CREATE INDEX "his_config_info_gmt_modified_idx" ON "public"."his_config_info" USING btree (
  "gmt_modified" "pg_catalog"."timestamp_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table his_config_info
-- ----------------------------
ALTER TABLE "public"."his_config_info" ADD CONSTRAINT "his_config_info_pkey" PRIMARY KEY ("nid");

-- ----------------------------
-- Indexes structure for table permissions
-- ----------------------------
CREATE UNIQUE INDEX "permissions_role_resource_action_idx" ON "public"."permissions" USING btree (
  "role" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "resource" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "action" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Indexes structure for table roles
-- ----------------------------
CREATE UNIQUE INDEX "roles_username_role_idx" ON "public"."roles" USING btree (
  "username" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "role" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Auto increment value for tenant_capacity
-- ----------------------------
SELECT setval('"public"."tenant_capacity_id_seq1"', 1, false);

-- ----------------------------
-- Indexes structure for table tenant_capacity
-- ----------------------------
CREATE UNIQUE INDEX "tenant_capacity_tenant_id_idx" ON "public"."tenant_capacity" USING btree (
  "tenant_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table tenant_capacity
-- ----------------------------
ALTER TABLE "public"."tenant_capacity" ADD CONSTRAINT "tenant_capacity_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for tenant_info
-- ----------------------------
SELECT setval('"public"."tenant_info_id_seq1"', 1, false);

-- ----------------------------
-- Indexes structure for table tenant_info
-- ----------------------------
CREATE UNIQUE INDEX "tenant_info_kp_tenant_id_idx" ON "public"."tenant_info" USING btree (
  "kp" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "tenant_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "tenant_info_tenant_id_idx" ON "public"."tenant_info" USING btree (
  "tenant_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table tenant_info
-- ----------------------------
ALTER TABLE "public"."tenant_info" ADD CONSTRAINT "tenant_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table users
-- ----------------------------
ALTER TABLE "public"."users" ADD CONSTRAINT "users_pkey" PRIMARY KEY ("username");

UPDATE "public"."config_info" SET "content" = '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  datasource:
    dynamic:
      # 默认false,建议线上关闭
      p6spy: false
      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: false
      datasource:
        master:
          type: com.zaxxer.hikari.HikariDataSource
          driver-class-name: org.postgresql.Driver
          url: jdbc:postgresql://postgresql.laokou.org:5432/kcloud_platform_iot?tcpKeepAlive=true&reWriteBatchedInserts=true&ApplicationName=flyway-postgresql&useSSL=false&reWriteBatchedInserts=true
          username: ENC(OuDQnY2CK0z2t+sq1ihFaFHWve1KjJoRo1aPyAghuRAf9ad3BO6AjcJRA+1b/nZw)
          password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
          # https://blog.csdn.net/u014644574/article/details/123680515
          hikari:
            connection-timeout: 60000
            validation-timeout: 3000
            idle-timeout: 60000
            max-lifetime: 60000
            maximum-pool-size: 30
            minimum-idle: 10
            is-read-only: false
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-auth
      port: -1
      log-path: ./logs/xxl-job/laokou-auth
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7
# mybatis-plus
mybatis-plus:
  # 全局处理
  global-config:
    db-config:
      column-format: "`%s`"
  tenant:
    enabled: true
    ignore-tables:
      - boot_sys_source
      - boot_sys_tenant
  slow-sql:
    enabled: false
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl' WHERE "id" = 2025;

UPDATE "public"."config_info" SET "content" = '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  datasource:
    dynamic:
      # 默认false,建议线上关闭
      p6spy: false
      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: false
      datasource:
        master:
          type: com.zaxxer.hikari.HikariDataSource
          driver-class-name: org.postgresql.Driver
          url: jdbc:postgresql://postgresql.laokou.org:5432/kcloud_platform_iot?tcpKeepAlive=true&reWriteBatchedInserts=true&ApplicationName=flyway-postgresql&useSSL=false&reWriteBatchedInserts=true
          username: ENC(OuDQnY2CK0z2t+sq1ihFaFHWve1KjJoRo1aPyAghuRAf9ad3BO6AjcJRA+1b/nZw)
          password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
          # https://blog.csdn.net/u014644574/article/details/123680515
          hikari:
            pool-name: HikariCP
            connection-timeout: 180000
            validation-timeout: 3000
            idle-timeout: 180000
            max-lifetime: 1800000
            maximum-pool-size: 60
            minimum-idle: 10
            is-read-only: false
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-admin
      port: -1
      log-path: ./logs/xxl-job/laokou-admin
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7

# mybatis-plus
mybatis-plus:
  # 全局处理
  global-config:
    db-config:
      column-format: "`%s`"
  tenant:
    ignore-tables:
      - boot_sys_tenant
      - boot_sys_source
      - boot_sys_package_menu
      - boot_sys_package
      - boot_sys_login_log
    enabled: true
  slow-sql:
    enabled: true
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl' WHERE "id" = 2022;

UPDATE "public"."config_info" SET "content" = '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  datasource:
    dynamic:
      # 默认false,建议线上关闭
      p6spy: false
      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: false
      datasource:
        master:
          type: com.zaxxer.hikari.HikariDataSource
          driver-class-name: org.postgresql.Driver
          url: jdbc:postgresql://postgresql.laokou.org:5432/kcloud_platform_iot?tcpKeepAlive=true&reWriteBatchedInserts=true&ApplicationName=flyway-postgresql&useSSL=false&reWriteBatchedInserts=true
          username: ENC(OuDQnY2CK0z2t+sq1ihFaFHWve1KjJoRo1aPyAghuRAf9ad3BO6AjcJRA+1b/nZw)
          password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
          # https://blog.csdn.net/u014644574/article/details/123680515
          hikari:
            pool-name: HikariCP
            connection-timeout: 180000
            validation-timeout: 3000
            idle-timeout: 180000
            max-lifetime: 1800000
            maximum-pool-size: 60
            minimum-idle: 10
            is-read-only: false
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-admin
      port: -1
      log-path: ./logs/xxl-job/laokou-admin
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7

# mybatis-plus
mybatis-plus:
  # 全局处理
  global-config:
    db-config:
      column-format: "\"%s\""
  tenant:
    ignore-tables:
      - boot_sys_tenant
      - boot_sys_source
      - boot_sys_package_menu
      - boot_sys_package
      - boot_sys_login_log
    enabled: true
  slow-sql:
    enabled: true
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl' WHERE "id" = 2022;

UPDATE "public"."config_info" SET  "content" = '# jasypt
jasypt:
  encryptor:
    password: 5201314wumeihua

# spring
spring:
  datasource:
    dynamic:
      # 默认false,建议线上关闭
      p6spy: false
      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候会抛出异常,不启动则使用默认数据源
      strict: false
      datasource:
        master:
          type: com.zaxxer.hikari.HikariDataSource
          driver-class-name: org.postgresql.Driver
          url: jdbc:postgresql://postgresql.laokou.org:5432/kcloud_platform_iot?tcpKeepAlive=true&reWriteBatchedInserts=true&ApplicationName=flyway-postgresql&useSSL=false&reWriteBatchedInserts=true
          username: ENC(OuDQnY2CK0z2t+sq1ihFaFHWve1KjJoRo1aPyAghuRAf9ad3BO6AjcJRA+1b/nZw)
          password: ENC(XVR9OF604T3+2BINpvvCohjr7/KM/vuP3ZgYpu+FX/h3uogFI3sh26h8wHPBE+rj)
          # https://blog.csdn.net/u014644574/article/details/123680515
          hikari:
            connection-timeout: 60000
            validation-timeout: 3000
            idle-timeout: 60000
            max-lifetime: 60000
            maximum-pool-size: 30
            minimum-idle: 10
            is-read-only: false
  xxl-job:
    admin:
      address: http://xxl.job.laokou.org:9095/xxl-job-admin
    executor:
      app-name: laokou-auth
      port: -1
      log-path: ./logs/xxl-job/laokou-auth
      access-token: yRagfkAddGXdTySYTFzhvMguinulMIMSCcXUbljWDhe
      intentionalities: 7
# mybatis-plus
mybatis-plus:
  # 全局处理
  global-config:
    db-config:
      column-format: "\"%s\""
  tenant:
    enabled: true
    ignore-tables:
      - boot_sys_source
      - boot_sys_tenant
  slow-sql:
    enabled: true
  mapper-locations: classpath*:/mapper/**/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl' WHERE "id" = 2025;
