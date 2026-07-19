## Context

`laokou-network` 是一个独立的 Vertx 微服务（应用名 `laokou-mqtt`，端口 9995，`WebApplicationType.SERVLET`），与 `laokou-iot`、`laokou-admin` 同属本平台微服务集群。它当前职责是承载 IoT 设备/网关接入：

- **已存在**：`infrastructure` 层有 `VertxConfig`（`Vertx` Bean）、`MqttServerConfig`/`MqttClientConfig`/`HttpServerConfig`（纯 POJO 参数，字段硬编码默认值）、`VertxMqttServer`/`VertxMqttClient`/`VertxHttpServer` 等 Verticle，以及 `AbstractVertxService`（继承 `AbstractVerticle` 并实现 `VertxService` 的 `deploy/undeploy`）、`VertxService` 接口、`VertxServiceManager`（内部持有 `Map<Long, VertxService>`，目前未被使用）。`domain` 层只有消息模型（`Message`/`DeviceMessage`/`mqtt`/`http` 子包），没有任何业务实体。
- **空缺**：`laokou-network-adapter` / `laokou-network-app` / `laokou-network-client` 三个模块的 `src` 目录为空（无 Java 文件），即该服务没有任何 Web 控制器、应用服务、对外 DTO；`infrastructure` 也没有 MyBatis-Plus / 数据源 / Spring Security 依赖；`application.yml` 无数据源与安全配置。
- **连接参数不可维护**：MQTT/HTTP 的 host/port/账号等全部写死在 POJO 默认值里，运维要改连接必须改代码重启；Kafka / RabbitMQ 连接尚无任何配置载体。

参照实现：`laokou-iot` 的「产品管理」（commits `e6f7eb2`、`1e518a0`）是本平台 DDD COLA 全链路 + Ant Design Pro 前端的完整闭环范例。本设计以其为蓝本，把同构骨架移植到 `laokou-network` 并适配「统一连接实体 + 类型字段」这一建模差异。

约束（来自用户确认）：
1. 数据建模采用**统一单实体 + `type` 字段**，而非每类连接一张表。
2. 后端本次只做**连接配置的 CRUD 持久化**，运行时 deploy/undeploy（接入 `VertxServiceManager`）与 Kafka/RabbitMQ Verticle 的真正部署留作后续。
3. 管理用 Web API（鉴权 + DB CRUD）**落在 `laokou-network` 服务自身**，不复用 `laokou-iot`/`laokou-admin`。

## Goals / Non-Goals

**Goals:**

- 为 `laokou-network` 服务补齐 DDD COLA 全链路（client/adapter/app/domain/infrastructure），首次引入 Web + MyBatis-Plus + Spring Security，技术栈与 `laokou-iot` 对齐。
- 用统一实体 `network_connection`（`type` 区分 5 类连接，通用字段落列、类型特有参数存 JSON `config`）支撑 5 类连接配置的增删改查与按条件检索。
- 前端「网络管理」页面：列表按 名称/类型/启用状态 检索；新增/修改抽屉**按 `type` 动态渲染**该类型的特有字段；中英文国际化、按权限显隐。
- 引入 `vertx-kafka-client`、`vertx-rabbitmq-client` 依赖，并补充 Kafka/RabbitMQ 的 Vertx 配置 POJO（与 `MqttServerConfig` 同构），为后续运行时部署预留。
- 数据库与权限种子通过幂等迁移 + 初始化 SQL 维护；补充前后端测试覆盖成功/失败/权限不足场景。

**Non-Goals:**

- 不实现连接的运行时启停：本次不把 CRUD 与 `VertxServiceManager.deploy/undeploy` 联动，不真正建立 Kafka/RabbitMQ 连接（仅持久化配置 + 预留 POJO/依赖）。
- 不改造既有 MQTT Server / HTTP Server / MQTT Client 的硬编码 Verticle 行为（不强制让它们改读 DB 配置）。
- 不引入连接连通性探测 / 健康检查 / 实时状态回显。
- 不改动 `laokou-iot`、`edge-gateway-device-management`、`product-management` 等既有能力。

## Decisions

### 决策 1：统一连接实体 + `type` + JSON `config`

建一张 `network_connection` 表承载 5 类连接。通用可检索字段落普通列，类型特有参数（如 MQTT Server 的 `maxMessageSize`、Kafka 的 `bootstrapServers`/`groupId`、RabbitMQ 的 `virtualHost` 等）序列化为 JSON 存入 `config` 文本列。

表结构（继承 `BaseDO`：含 `id`/`version`/`del_flag`/`tenant_id`/`create_time` 等公共列）：

| 列 | 类型 | 说明 |
| --- | --- | --- |
| `name` | varchar | 连接名称（租户内可检索，建议唯一） |
| `type` | int | 连接类型：1 MQTT Server / 2 HTTP Server / 3 MQTT Client / 4 Kafka / 5 RabbitMQ |
| `host` | varchar | 主机地址（HTTP/Kafka 等可空或复用） |
| `port` | int | 端口 |
| `enabled` | int | 是否启用：0 启用 / 1 停用（对齐项目 `del_flag`/`status` 0/1 约定） |
| `config` | text | 类型特有参数 JSON |
| `remark` | varchar | 备注 |

- **理由**：与 `VertxServiceManager` 的 `Map<Long, VertxService>` 天然对齐（一条记录 = 一个可被 deploy 的连接，`id` 即 key）；5 类连接通用字段高度重合，特有字段差异大且会演进，JSON 列避免频繁加列与稀疏宽表；前端单页面 + 动态表单即可覆盖，符合一致性优先。
- **`config` 的解析**：领域层只把 `config` 当不透明字符串持久化；类型特有字段的结构由前端表单 + 后端 Vertx 配置 POJO（`MqttServerConfig` 等）约定。本次不在保存时强校验 JSON schema（Non-Goal：运行时部署），仅做合法 JSON 校验。
- **备选**：每类连接一张表 + 一套 DDD 层 + 一个页面 —— 否决：代码量约 5 倍、5 套高度相似的 CRUD 重复维护，且与单一 `VertxServiceManager` 模型不契合。

### 决策 2：在 `laokou-network` 移植 product 的 COLA 骨架

按 `laokou-iot` product 包逐层对齐，新增 `connection` 包：

- `laokou-network-client`：`connection/api/ConnectionsServiceI`、`connection/dto/{ConnectionSaveCmd,ConnectionModifyCmd,ConnectionRemoveCmd,ConnectionGetQry,ConnectionPageQry}`、`connection/dto/clientobject/ConnectionCO`。`ConnectionPageQry` 的 `setName` 套用 `StringExtUtils.like(trim(...))`，对齐项目搜索约定。
- `laokou-network-adapter`：`ConnectionsController`，端点 `/v1/connections`(POST/PUT/DELETE)、`/v1/connections/page`(POST)、`/v1/connections/{id}`(GET)；注解对齐 product：`@Idempotent`（save）、`@PreAuthorize("hasAuthority('write'/'read') and hasAuthority('network:connection:xxx')")`、`@OperateLog`、`@TraceLog`、`@Operation`。本次不做 import/export（product 有，但连接配置非批量场景，YAGNI）。
- `laokou-network-app`：`connection/service/ConnectionsServiceImpl` + `connection/command/{ConnectionSaveCmdExe,ConnectionModifyCmdExe,ConnectionRemoveCmdExe}` + `connection/command/query/{ConnectionGetQryExe,ConnectionPageQryExe}`，事务走 `TransactionalUtils`，`@CommandLog`。
- `laokou-network-domain`：`connection/model/ConnectionE`、`connection/gateway/ConnectionGateway`、`connection/ability/ConnectionDomainService`（必填校验：`name`、`type` 非空，`type` ∈ [1,5]，`config` 为合法 JSON；按项目 `ParamValidator` 约定抛 `ParamException`，对齐 `DeviceDomainService`）。
- `laokou-network-infrastructure`：`connection/gatewayimpl/database/dataobject/ConnectionDO`（`@TableName("network_connection")` extends `BaseDO`）、`connection/gatewayimpl/database/ConnectionMapper`(extends `CrudMapper<Long,Integer,ConnectionDO>`) + `mapper/connection/ConnectionMapper.xml`、`connection/gatewayimpl/ConnectionGatewayImpl`（修改走 `selectVersion` 乐观锁，对齐 `ProductGatewayImpl`）、`connection/convertor/ConnectionConvertor`（注意补齐 `id`/`createTime`，避免 device 曾出现的漏设缺陷）。

- **理由**：保持与 `laokou-iot` 完全同构，降低认知成本与评审成本；复用 `laokou-common-mybatis-plus` 的 `CrudMapper`/`BaseDO`、`TransactionalUtils`、`StringExtUtils`、`Result`/`Page`/`PageQuery`。
- **备选**：抽象通用 CRUD 基类 —— 否决，项目惯例是每实体独立分层，一致性优先。

### 决策 3：为 `laokou-network` 引入 Web/DB/Security 依赖栈

- `laokou-network-infrastructure` 新增：`laokou-common-mybatis-plus`（数据源 + CrudMapper）、`laokou-common-security`/`laokou-common-web`（`@PreAuthorize`、统一返回、异常处理）、`laokou-common-i18n`（`Result`/`Page`，client 也需要）、`laokou-common-log`（`@OperateLog`/`@CommandLog`）、`laokou-common-idempotent`（`@Idempotent`）—— 具体 artifactId 以 `laokou-iot-infrastructure` 实际依赖为准，逐项对齐。
- `laokou-network-start/application.yml` 增加数据源（指向 IoT 业务库）、MyBatis-Plus、Spring Security/OAuth2 资源服务器相关配置，复用 `laokou-iot-start` 的同名配置块。
- 网关（`laokou-cloud` / gateway）放行 `laokou-network` 的 `/network/api/**` 前缀（对齐 `/iot/api/**` 的转发与鉴权策略）。

- **理由**：用户明确要求管理 API 落在本服务；与 iot 对齐可最大化复用既有安全/数据源基建。
- **风险点**：该服务原本以 Vertx 接入为主，引入 Servlet Web + 安全栈后需确认与 `WebApplicationType.SERVLET`、虚拟线程、Pulsar 自动配置不冲突（见 Risks）。

### 决策 4：Kafka / RabbitMQ 基于 Vertx，仅补依赖与配置 POJO

- `infrastructure` `pom.xml` 新增 `io.vertx:vertx-kafka-client`、`io.vertx:vertx-rabbitmq-client`（版本由 Vertx BOM 统一管理，不写死版本号）。
- 新增配置 POJO：`config/kafka/KafkaConfig`、`config/rabbitmq/RabbitmqConfig`（`@Data` 纯 POJO，字段对齐 Vertx `KafkaClientOptions`/`RabbitMQOptions` 常用项，默认值风格同 `MqttServerConfig`），作为 `config` JSON 的反序列化目标与前端表单字段契约。
- **理由**：与现有 MQTT/HTTP「POJO 描述连接参数」的模式一致；本次不创建 Verticle、不建立真实连接（Non-Goal），POJO 仅作配置载体与契约。
- **备选**：直接用 spring-kafka / spring-amqp —— 否决，用户明确要求 Kafka/RabbitMQ 基于 Vertx 实现，且与本服务 Vertx 体系一致。

### 决策 5：前端单页面 + 类型驱动的动态抽屉

- 新增 `ui/src/pages/Network/Connection/index.tsx`（复刻 `product.tsx` 的 `ProTable` 结构）：列含 名称、类型（select，5 枚举，列表以可读文案渲染）、host、port、启用状态（select 0/1）、创建时间 + 时间区间搜索；行操作 查看/修改/删除，工具栏 新增/批量删除，按 `access.canConnection*` 显隐。
- 新增 `SessionDrawer.tsx`（复刻 `ProductDrawer.tsx`）：通用字段固定渲染；**监听 `type` 字段**（`ProFormSelect` + `dependency` / `ProFormDependency`），按所选类型渲染该类型特有字段（MQTT Server / HTTP Server / MQTT Client / Kafka / RabbitMQ 各一组）；提交时把特有字段聚合为 `config` JSON 字符串，回显时反序列化。保存调 `saveConnection`(带 `requestId`)、修改调 `modifyConnection`。
- 新增 `ui/src/services/network/connection.ts`（复刻 `services/iot/product.ts`，URL 前缀 `/api-proxy/network/api/v1/connections`）+ `services/network/typings.d.ts`。
- `routes.ts` 新增 `menu.network` 顶级菜单 + `/network/connection` 子路由；`access.ts` 新增 `canConnectionPage/GetDetail/Save/Modify/Remove`；`zh-CN.ts`/`en-US.ts` 新增 `menu.network.*` 与 `network.connection.*`（含各类型字段标签/占位/校验）文案。

- **理由**：单页面 + 动态表单契合「统一实体」建模；`ProFormDependency` 是 Ant Design Pro 的标准类型联动方案，无需自造。
- **备选**：5 个 Tab / 5 个页面 —— 否决，与单实体模型不一致且重复。

## Risks / Trade-offs

- **[在 Vertx 服务中引入 Servlet Web + Security 栈可能与现有自动配置冲突]** → 先在 `laokou-network-infrastructure` 增量加依赖并本地编译/启动验证；逐项对齐 `laokou-iot` 的依赖与 `application.yml`；保留 `@SpringBootApplication(scanBasePackages = "org.laokou")` 扫描范围，确认 Pulsar/虚拟线程配置不被破坏；若安全栈影响 Vertx 接入端口（9995 vs Web 端口），明确 Web API 与 Vertx 监听端口分离。
- **[`config` JSON 弱类型，错误配置在运行时部署阶段才暴露]** → 本次为 Non-Goal（不运行时部署），保存时仅校验合法 JSON；类型特有字段由前端表单约束 + 后端 POJO 契约兜底；后续接入部署时再补 schema 校验。
- **[新服务首次落 DB，迁移/权限种子在无 DB 环境无法实测]** → 迁移脚本采用 `INSERT ... WHERE NOT EXISTS` 与 `CREATE TABLE IF NOT EXISTS` 保证幂等，对齐已验证的 device/dict 迁移写法；同步更新 `doc/db` 初始化 SQL，待部署环境执行。
- **[网关未放行导致前端 404]** → 在 tasks 中显式包含网关路由放行项，并核对 `/api-proxy` → `/network/api` 的 proxy 配置（`ui/config/proxy.ts`）。
- **[与 product 不同：本次无 import/export]** → 有意收窄（YAGNI），spec 不包含导入导出场景，避免无用端点。

## Migration Plan

1. 后端先落 `domain` → `client` → `infrastructure` → `app` → `adapter` 分层（编译驱动，逐模块 `mvn -q compile`）。
2. 引入 Web/DB/Security 依赖后单独验证服务可启动、Web API 可鉴权。
3. 数据库：执行 `network_connection` 建表与 `network:connection:*` 权限/菜单幂等迁移脚本（admin-start migration + `doc/db` 初始化 SQL）。
4. 网关放行 `/network/api/**`、前端 proxy 配置核对。
5. 前端落 service → access/routes/locale → 页面与抽屉，`tsc --noEmit` 校验。
6. **回滚**：移除 `connection` 包与 pom 依赖、还原 `application.yml`/`routes.ts`/`access.ts`/locale；执行迁移脚本反向 SQL（软删权限种子 + `DROP TABLE network_connection`）。因全为新增对象，回滚无既有数据风险。

## Open Questions

- Web API 监听端口：复用 9995（与 Vertx 接入是否同端口）还是另开 Web 端口？建议落地时对齐 `laokou-iot-start` 端口约定确认。
- `network_connection` 落库归属：复用 IoT 业务库还是新建 network 库？建议复用 IoT 业务库（与 `iot_*` 表同库）以简化数据源配置，最终以 `laokou-network-start` 数据源配置确认。
- `name` 是否需要租户内唯一索引？倾向需要（便于按名检索与避免重复），但运行时部署才强依赖；本次可先做应用层校验，唯一索引随表创建一并加。
