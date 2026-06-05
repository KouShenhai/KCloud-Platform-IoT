## 1. 后端模块依赖与基础设施接入（laokou-network）

- [ ] 1.1 在 `laokou-network-infrastructure/pom.xml` 增加 `laokou-common-mybatis-plus`、`laokou-common-security`、`laokou-common-web`、`laokou-common-i18n`、`laokou-common-log`、`laokou-common-idempotent` 等依赖（逐项对齐 `laokou-iot-infrastructure` 实际依赖清单），并新增 `io.vertx:vertx-kafka-client`、`io.vertx:vertx-rabbitmq-client`（版本由 Vertx BOM 管理，不写死）
- [ ] 1.2 在 `laokou-network-client/pom.xml` 增加 `laokou-common-i18n`（`Result`/`Page`/`PageQuery`/`ClientObject`）依赖
- [ ] 1.3 在 `laokou-network-adapter/pom.xml` 确认可传递获得 web/security 依赖（经 app→infrastructure），必要时显式补充
- [ ] 1.4 在 `laokou-network-start/src/main/resources/application.yml` 增加数据源、MyBatis-Plus、Spring Security/OAuth2 资源服务器配置块（对齐 `laokou-iot-start`，数据源指向 IoT 业务库）
- [ ] 1.5 核对 `NetworkApp` 的 `@SpringBootApplication(scanBasePackages="org.laokou")` 扫描范围覆盖 `connection` 包；确认引入 Web/安全栈后服务可正常启动、Pulsar 与虚拟线程配置不被破坏
- [ ] 1.6 离线编译 `laokou-network` 全模块（`mvn -q -pl ...network... -am compile`），确认依赖引入无冲突

## 2. 后端领域层（laokou-network-domain）

- [ ] 2.1 新增 `connection/model/ConnectionE` 领域实体（字段：id、name、type、host、port、enabled、config、remark、createTime；对齐 product 的 `ProductE`）
- [ ] 2.2 新增 `connection/gateway/ConnectionGateway` 网关接口（create/update/remove 等方法签名对齐 `ProductGateway`）
- [ ] 2.3 新增 `connection/ability/ConnectionDomainService`，按项目 `ParamValidator` 约定实现校验：`name` 非空、`type` 非空且 ∈ [1,5]、`config` 为合法 JSON（非法时抛 `ParamException`），对齐 `DeviceDomainService` 写法

## 3. 后端客户端层（laokou-network-client）

- [ ] 3.1 新增 `connection/dto/clientobject/ConnectionCO`（extends `ClientObject`，含 id/name/type/host/port/enabled/config/remark/createTime，`@Schema` 注解）
- [ ] 3.2 新增 `connection/dto/ConnectionSaveCmd`、`ConnectionModifyCmd`（extends `CommonCommand`，持有 `ConnectionCO co`）
- [ ] 3.3 新增 `connection/dto/ConnectionRemoveCmd`（`Long[] ids`）、`ConnectionGetQry`（`Long id`）
- [ ] 3.4 新增 `connection/dto/ConnectionPageQry`（extends `PageQuery`，字段 name/type/enabled；`setName` 套用 `StringExtUtils.like(StringExtUtils.trim(name))`）
- [ ] 3.5 新增 `connection/api/ConnectionsServiceI` 接口（save/modify/remove/page/getById 方法，对齐 `ProductsServiceI`，不含 import/export）

## 4. 后端基础设施层（laokou-network-infrastructure）

- [ ] 4.1 新增 `connection/gatewayimpl/database/dataobject/ConnectionDO`（`@TableName("network_connection")` extends `BaseDO`，字段 name/type/host/port/enabled/config/remark）
- [ ] 4.2 新增 `connection/gatewayimpl/database/ConnectionMapper`（extends `CrudMapper<Long,Integer,ConnectionDO>`，`@Mapper`/`@Repository`）
- [ ] 4.3 新增 `src/main/resources/mapper/connection/ConnectionMapper.xml`（`selectColumns`/`selectConditions`/`selectObjectPage`/`selectObjectCount`/`selectVersion`；条件含 name LIKE、type、enabled、create_time 区间，对齐 `ProductMapper.xml`）
- [ ] 4.4 新增 `connection/convertor/ConnectionConvertor`（toEntity/toDataObject/toClientObject，**确保补齐 id 与 createTime**，避免 device 曾出现的漏设缺陷）
- [ ] 4.5 新增 `connection/gatewayimpl/ConnectionGatewayImpl`（create/update/remove；update 走 `selectVersion` 乐观锁，对齐 `ProductGatewayImpl`）
- [ ] 4.6 新增 Kafka/RabbitMQ Vertx 配置 POJO：`config/kafka/KafkaConfig`、`config/rabbitmq/RabbitmqConfig`（`@Data` 纯 POJO，字段对齐 Vertx `KafkaClientOptions`/`RabbitMQOptions` 常用项，默认值风格同 `MqttServerConfig`），作为 `config` JSON 契约

## 5. 后端应用层（laokou-network-app）

- [ ] 5.1 新增 `connection/command/ConnectionSaveCmdExe`（`@CommandLog`，`TransactionalUtils` 事务，调用 `ConnectionDomainService.createConnection`）
- [ ] 5.2 新增 `connection/command/ConnectionModifyCmdExe`、`ConnectionRemoveCmdExe`
- [ ] 5.3 新增 `connection/command/query/ConnectionGetQryExe`、`ConnectionPageQryExe`（分页查 `selectObjectPage` + `selectObjectCount`，转 `ConnectionCO`）
- [ ] 5.4 新增 `connection/service/ConnectionsServiceImpl`（implements `ConnectionsServiceI`，注入各 Exe，对齐 `ProductsServiceImpl`）

## 6. 后端适配层（laokou-network-adapter）

- [ ] 6.1 新增 `web/ConnectionsController`：端点 `/v1/connections`(POST/PUT/DELETE)、`/v1/connections/page`(POST)、`/v1/connections/{id}`(GET)
- [ ] 6.2 为各端点加注解：save 加 `@Idempotent`；`@PreAuthorize("hasAuthority('write'/'read') and hasAuthority('network:connection:xxx')")`；`@OperateLog`/`@TraceLog`/`@Operation`，对齐 `ProductsController`
- [ ] 6.3 离线编译 `laokou-network` 全 6 模块通过

## 7. 网关与代理放行

- [ ] 7.1 在网关（`laokou-cloud`/gateway 路由配置）放行并转发 `laokou-network` 的 `/network/api/**` 前缀，鉴权策略对齐 `/iot/api/**`
- [ ] 7.2 核对/补充 `ui/config/proxy.ts` 的 `/api-proxy` → `network` 服务转发，确保前端能访问 `/api-proxy/network/api/v1/connections*`

## 8. 数据库迁移与权限种子

- [ ] 8.1 编写 `network_connection` 建表 SQL（继承公共列约定：id/version/del_flag/tenant_id/create_time 等 + name/type/host/port/enabled/config/remark），含按租户 + name 的唯一索引（`CREATE TABLE/INDEX IF NOT EXISTS` 幂等）
- [ ] 8.2 在 `laokou-admin-start` migration 目录新增权限/菜单迁移脚本（命名遵循 `V<date>_NN__network_connection_permissions.sql`），以 `INSERT ... WHERE NOT EXISTS` 幂等插入 `network:connection:page/detail/save/modify/remove` 权限与「网络管理」菜单种子；同步到 standalone-admin 迁移目录
- [ ] 8.3 同步更新 `doc/db/kcloud_platform_iot.sql`（建表）与 `doc/db/kcloud_platform.sql`（权限/菜单种子），保证新环境一致
- [ ] 8.4 在迁移脚本中记录反向回滚 SQL（软删除 `network:connection:*` 权限种子、`DROP TABLE network_connection`，不触碰业务数据）

## 9. 前端 service 与类型（ui）

- [ ] 9.1 新增 `ui/src/services/network/connection.ts`（复刻 `services/iot/product.ts`：modify/save(带 requestId 头)/remove/getById/page，URL 前缀 `/api-proxy/network/api/v1/connections`）
- [ ] 9.2 新增 `ui/src/services/network/typings.d.ts`（`ConnectionCO`/`ConnectionSaveCmd`/`ConnectionModifyCmd`/`ConnectionPageQry` 等类型，字段与后端一致，id 小写）

## 10. 前端权限、路由与国际化（ui）

- [ ] 10.1 在 `ui/src/access.ts` 新增 `canConnectionPage/GetDetail/Save/Modify/Remove`（沿用 `permissions?.includes(...) && scopes?.includes(...)` 模式，权限串 `network:connection:*`）
- [ ] 10.2 在 `ui/config/routes.ts` 新增 `menu.network` 顶级菜单与 `/network/connection` 子路由（component `./Network/Connection`）
- [ ] 10.3 在 `ui/src/locales/zh-CN.ts` 新增 `menu.network.*` 与 `network.connection.*`（含通用字段、各连接类型特有字段的标签/占位/校验、类型枚举文案、启用状态文案、抽屉标题 insert/view/modify）
- [ ] 10.4 在 `ui/src/locales/en-US.ts` 补充与 zh-CN 对应的英文文案

## 11. 前端页面与动态抽屉（ui）

- [ ] 11.1 新增 `ui/src/pages/Network/Connection/index.tsx`，以 `product.tsx` 为模板用 `ProTable` 实现列表：列含 name、type(select 5 枚举 + map 渲染)、host、port、enabled(select 0/1)、createTime + 时间区间搜索；查询参数组装 `getPageQueryParam`（name 模糊、type、enabled、时间区间）
- [ ] 11.2 在 index.tsx 实现 查看/修改/删除 行操作与 新增/批量删除 工具栏按钮，按 `access.canConnection*` 显隐，删除走 `Modal.confirm`，新增时用 `uuid v7` 生成 requestId
- [ ] 11.3 新增 `ui/src/pages/Network/Connection/ConnectionDrawer.tsx`，以 `ProductDrawer.tsx` 为模板：通用字段固定渲染；用 `ProFormDependency` 监听 `type`，按所选类型渲染对应特有字段组（MQTT Server / HTTP Server / MQTT Client / Kafka / RabbitMQ）
- [ ] 11.4 在抽屉提交时把类型特有字段聚合为 `config` JSON 字符串提交；查看/修改时按 `config` 反序列化回显；保存调 `saveConnection`(带 requestId)、修改调 `modifyConnection`，只读模式禁用编辑并隐藏提交按钮

## 12. 测试与验证

- [ ] 12.1 后端：为 `ConnectionDomainService` 补充单元测试（成功、name 缺失、type 缺失、type 越界、config 非法 JSON、修改不存在、删除空 IDS 场景）
- [ ] 12.2 后端：`laokou-network` 全模块离线编译通过（domain/client/infrastructure/app/adapter/start）
- [ ] 12.3 前端：`tsc --noEmit` 类型检查通过，网络管理页面/抽屉/路由/access/locale 无类型错误
- [ ] 12.4 自检前端按钮显隐与后端 `@PreAuthorize` 一致：5 个 `network:connection:*` 权限在 `ConnectionsController`、`access.ts`、页面三处对齐
- [ ] 12.5 数据库迁移脚本以 `WHERE NOT EXISTS`/`IF NOT EXISTS` 保证幂等（无 DB 环境下静态核对，对齐已验证的 device/dict 迁移写法）

## 13. 文档与收尾

- [ ] 13.1 在变更 proposal/spec 中确认网络连接端点、权限点与页面入口完整记录；端点契约 `/network/api/v1/connections*` 固化
- [ ] 13.2 运行 `openspec validate add-network-management --strict` 校验通过
