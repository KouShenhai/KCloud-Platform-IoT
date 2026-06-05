## Why

`laokou-network` 服务（应用名 `laokou-mqtt`，端口 9995）已具备 Vertx 的 MQTT Server、HTTP Server、MQTT Client 等接入能力，但这些连接的参数目前全部以**硬编码 POJO**（`MqttServerConfig`、`MqttClientConfig`、`HttpServerConfig`）写死在 `infrastructure` 层，无法在控制台维护，更无 Kafka / RabbitMQ 连接的统一管理。同时该服务的 `adapter` / `app` / `client` 三层为空壳，没有任何 Web 控制器、持久化与鉴权，运维只能改代码重启来调整连接。

本次新增「网络管理」能力，把 5 类网络连接（MQTT Server / HTTP Server / MQTT Client / Kafka / RabbitMQ）的配置抽象为可在前端控制台增删改查的统一实体，让连接配置可视化、可检索、可维护。

## What Changes

- 在 `laokou-network` 服务补齐 DDD COLA 全链路（client / adapter / app / domain / infrastructure），首次为该服务引入 Web 控制器、MyBatis-Plus 持久化与 Spring Security 鉴权（技术栈对齐 `laokou-iot`）。
- 新增统一连接实体 `network_connection`：以 `type` 字段区分 5 类连接（1 MQTT Server / 2 HTTP Server / 3 MQTT Client / 4 Kafka / 5 RabbitMQ），通用字段（name / type / host / port / enabled / remark）落库为列，各类型特有参数以 JSON 文本存入 `config` 列。
- 新增后端连接管理接口：分页查询、按条件检索、查看详情、新增、修改、删除（单条/批量），路径 `/network/api/v1/connections*`，权限点 `network:connection:*`。
- 新增前端「网络管理」控制台页面（Ant Design Pro `ProTable` + `DrawerForm`，Umi Max）：列表支持按连接名称/类型/启用状态检索；新增/修改抽屉按所选 `type` 动态渲染该类型的特有配置字段；支持中英文国际化与按权限位显隐操作。
- 在前端注册 `/network/connection` 路由，补充 `access.ts` 权限位、`zh-CN` / `en-US` 国际化文案，新增 `services/network/connection.ts` 请求层（`saveConnection` 带 `request-id` 幂等头）。
- Kafka / RabbitMQ 连接基于 Vertx 实现：新增 `vertx-kafka-client`、`vertx-rabbitmq-client` 依赖，并补充对应的 Vertx 连接配置 POJO（与现有 `MqttServerConfig` 等同构），供后续运行时部署使用。
- 数据库新增 `network_connection` 表与 `network:connection:*` 权限/菜单种子（幂等迁移脚本 + 初始化 SQL）。

## Capabilities

### New Capabilities
- `network-management`: `laokou-network` 服务中 5 类网络连接（MQTT Server / HTTP Server / MQTT Client / Kafka / RabbitMQ）配置的全生命周期管理能力——统一连接实体的分页查询、按条件检索、查看详情、新增、修改、删除，前端控制台页面按连接类型动态渲染配置表单，后端按 DDD COLA 分层持久化并鉴权。

### Modified Capabilities
<!-- 无既有 spec 的需求级行为变更：本次为全新能力，不改动已存在的 edge-gateway-device-management / product-management 等 spec。 -->

## Impact

- 后端（`laokou-service/laokou-network`）：
  - 新增 `laokou-network-client`（`connection` 包：`api` / `dto` / `clientobject`）。
  - 新增 `laokou-network-adapter`（`ConnectionsController` Web 控制器）。
  - 新增 `laokou-network-app`（`connection` 包：命令/查询执行器、`ConnectionsServiceImpl`）。
  - 新增 `laokou-network-domain`（`connection` 包：`ConnectionE` 实体、`ConnectionGateway`、`ConnectionDomainService`）。
  - 新增 `laokou-network-infrastructure`（`connection` 包：`ConnectionDO`、`ConnectionMapper` + XML、`ConnectionGatewayImpl`、`ConnectionConvertor`；Kafka/RabbitMQ Vertx 配置 POJO）。
  - 修改 `laokou-network-*` 各 `pom.xml`：infrastructure 引入 `laokou-common-mybatis-plus`/security/web 依赖与 `vertx-kafka-client`、`vertx-rabbitmq-client`；client 引入 i18n；adapter 引入 web/security。
  - 修改 `laokou-network-start`：`application.yml` 增加数据源/MyBatis-Plus/安全相关配置，`NetworkApp` 视情况调整为可对外暴露 Web API。
- 前端（`ui/`）：
  - 新增：`src/pages/Network/Connection/index.tsx`、`ConnectionDrawer.tsx`、`src/services/network/connection.ts`、`src/services/network/typings.d.ts`。
  - 修改：`config/routes.ts`（新增 `menu.network` 菜单与 `/network/connection` 路由）、`src/access.ts`（`canConnection*` 权限位）、`src/locales/zh-CN.ts`、`src/locales/en-US.ts`（`menu.network.*`、`network.connection.*` 文案）。
- API：新增 `/network/api/v1/connections`（POST/PUT/DELETE）、`/connections/page`、`/connections/{id}`；网关需放行 `laokou-network` 的 API 前缀；鉴权前缀 `network:connection:*`。向后兼容（全新端点，不影响既有接口）。
- 数据库：新增 `network_connection` 表（IoT 业务库），新增 `network:connection:page/detail/save/modify/remove` 权限与菜单种子；通过幂等迁移脚本与初始化 SQL 维护，新环境一致。
- 依赖：新增 `io.vertx:vertx-kafka-client`、`io.vertx:vertx-rabbitmq-client`（版本由 Vertx BOM 管理）。
- 影响范围限于 `laokou-network` 微服务、`ui` 控制台与网关放行配置，不影响其他微服务既有能力。

## Rollback

- 前端：移除 `Network/Connection` 页面与 `services/network`，还原 `routes.ts` / `access.ts` / locale 新增条目即可，无数据副作用。
- 后端：移除 `laokou-network` 各模块新增的 `connection` 包与 pom 依赖、还原 `application.yml`；因仅新增端点与表，回滚不影响既有 MQTT/HTTP 接入链路。
- 数据库：迁移脚本提供反向回滚 SQL（软删除 `network:connection:*` 权限种子、`DROP TABLE network_connection`），仅触碰本能力新增对象，不影响业务数据。
