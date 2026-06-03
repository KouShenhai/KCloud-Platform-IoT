## Why

系统管理中已有数据字典的后端接口、前端 service、数据库表与菜单初始化数据，但 Umi Max 前端还没有挂载 `/sys/base/dict` 页面，权限胶水也不完整，导致数据字典能力无法在管理端闭环使用。

本变更补齐数据字典的前后端管理闭环，让管理员可以维护字典类型和字典项，并将现有 DDD COLA 后端能力按当前项目结构校验、加固和测试。

## What Changes

- 在 Umi Max 管理端新增数据字典页面，挂载到 `系统管理 / 基础数据 / 数据字典`。
- 复用并补齐 `admin` service 中已有的 `dict` 与 `dictItem` API，提供字典类型和字典项的分页、详情、新增、修改、删除、导入、导出操作。
- 在 `access.ts` 中补齐 `sys:dict:*` 与 `sys:dict-item:*` 权限映射，页面按钮按权限展示。
- 审视并加固 `laokou-admin` 中已有 DDD COLA 字典实现，补充请求参数校验、字典类型唯一性、字典项归属校验、删除保护或级联策略。
- 对 PostgreSQL 初始化/迁移脚本中的 `sys_dict`、`sys_dict_item`、`sys_menu`、`sys_i18n_menu`、权限数据进行校验，缺失时补迁移脚本。
- 补充前后端测试与文档，覆盖成功、失败、权限不足和数据完整性场景。

## Capabilities

### New Capabilities

- `data-dictionary`: 数据字典管理能力，覆盖字典类型、字典项、权限、前端页面、后端 API 契约和数据库完整性。

### Modified Capabilities

- 无。当前 `openspec/specs/` 中没有已有主规格需要修改。

## Impact

- 前端：`ui/config/routes.ts`、`ui/src/access.ts`、`ui/src/pages/Sys/Base/*`、`ui/src/locales/*`，以及现有 `ui/src/services/admin/dict*.ts` 的调用方式。
- 后端：`laokou-service/laokou-admin` 下 `adapter`、`app`、`domain`、`infrastructure`、`client` 分层中的 `dict` 与 `dictItem` 包。
- 数据库：PostgreSQL 的 `sys_dict`、`sys_dict_item` 表，以及菜单、权限、国际化菜单初始化或迁移数据。
- API：继续使用已有 `/api/v1/dicts`、`/api/v1/dicts/page`、`/api/v1/dicts/{id}`、`/api/v1/dict-items`、`/api/v1/dict-items/page`、`/api/v1/dict-items/{id}` 等路径，保持向后兼容。
- 微服务影响：仅涉及 `laokou-admin` 管理服务和 `ui` 管理端；不引入 Kafka、Pulsar、MQTT 或 IoT 遥测链路变更。
- 迁移策略：优先补充幂等迁移/初始化数据，不破坏已有表结构；新增唯一索引或约束前需先清理重复数据。
- 回滚计划：前端可移除路由和页面入口回滚；后端新增校验可按提交回滚；数据库迁移需提供反向脚本或明确保留兼容性，避免删除业务数据。
