## Why

当前项目已经生成了租户后端 DDD COLA 骨架、`/api/v1/tenants*` 接口、前端 `tenant.ts` service 和 `sys_tenant` 表，但管理端没有挂载可用页面，后端领域规则也还停留在透传保存层面。租户是多租户平台的基础主数据，需要补齐前后端闭环并加固编码唯一性、参数校验、权限和初始化数据。

## What Changes

- 新增 Umi Max + React 19 租户管理页面，挂载到 `系统管理 / 租户管理 / 租户`，路由使用现有数据库菜单路径 `/sys/tenant/index`。
- 复用并修正 `ui/src/services/admin/tenant.ts` 和 `typings.d.ts`，提供租户分页、详情、新增、修改、删除、导入、导出入口。
- 在 `ui/src/access.ts` 中补齐 `sys:tenant:*` 权限映射，页面按钮按 `read/write` scope 和权限显示。
- 加固 `laokou-admin` 中已有 tenant 的 DDD COLA 分层：client DTO、app command、domain service、infrastructure gateway、common tenant mapper。
- 增加租户参数校验、租户编码唯一性校验、租户存在性校验、默认租户删除保护，以及分页按名称/编码/时间范围查询。
- 校验数据库初始化脚本中的 `sys_tenant`、`sys_menu`、`sys_i18n_menu` 和 `sys:tenant:*` 权限数据，缺失时补齐幂等或初始化数据。
- 补充前后端校验，覆盖成功、失败、权限不足和数据完整性场景。

## Capabilities

### New Capabilities

- `tenant-management`: 租户管理能力，覆盖租户基础信息维护、REST API 契约、前端页面、权限控制、DDD COLA 业务规则和数据库初始化一致性。

### Modified Capabilities

- 无。当前 `openspec/specs/` 中没有租户管理主规格需要修改。

## Impact

- 前端：`ui/config/routes.ts`、`ui/src/access.ts`、`ui/src/pages/Sys/Tenant/*`、`ui/src/services/admin/tenant.ts`、`ui/src/services/admin/typings.d.ts`、`ui/src/locales/*`。
- 后端：`laokou-service/laokou-admin` 中 tenant 的 `adapter`、`client`、`app`、`domain`、`infrastructure` 分层，以及 `laokou-common/laokou-common-tenant` 的 mapper 查询。
- 数据库：PostgreSQL `sys_tenant` 表、菜单 `sys_menu`、国际化菜单 `sys_i18n_menu`、权限资源初始化数据；优先保持现有表结构和 API 路径兼容。
- API：继续使用已有 `/api/v1/tenants`、`/api/v1/tenants/page`、`/api/v1/tenants/{id}`、`/api/v1/tenants/import`、`/api/v1/tenants/export`。
- 微服务影响：仅涉及 `laokou-admin` 管理服务和 `ui` 管理端，不新增 Kafka、Pulsar、MQTT、TDengine 或 IoT 遥测链路。
- 迁移策略：优先补齐幂等初始化数据和索引/查询能力，不破坏已有业务数据；新增唯一约束前必须先检查重复编码。
- 回滚计划：前端可移除路由和页面入口；后端可回滚新增校验和 mapper 查询；数据库回滚不得删除已有租户业务数据，只回退本次新增的菜单/权限/约束。
