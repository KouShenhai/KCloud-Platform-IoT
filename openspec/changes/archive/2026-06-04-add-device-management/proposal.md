## Why

IoT 平台已经具备产品、产品类别、物模型的前后端管理能力，设备是这些能力的最终落点，但当前设备管理只完成了后端 DDD COLA 分层和前端 service 层，Umi Max 管理端缺少 `/iot/device/device` 页面、抽屉表单、权限胶水与字段国际化，导致设备能力无法在管理端闭环使用。同时设备的导入、导出权限种子数据缺失，后端实现也需要按现有约定做一次校验加固。

本变更补齐设备管理的前后端闭环，让运维人员可以在管理端对设备做分页查询、详情、新增、修改、删除、导入和导出，并将已有的 DDD COLA 后端实现按当前项目结构校验、加固和测试。

## What Changes

- 在 Umi Max 管理端新增设备列表页 `ui/src/pages/IoT/Device/device.tsx`，挂载到 `设备管理 / 设备`（路由 `/iot/device/device`），使用 ProTable 实现分页、查询、删除、导入、导出入口。
- 实现设备新增/修改/查看抽屉表单（替换当前的空 stub `DeviceDrawer.tsx`），使用 DrawerForm + ProForm 字段，关联产品下拉选择。
- 复用并按需补齐 `ui/src/services/iot/device.ts`，保持与 product 一致的请求约定；为 `saveDevice` 补充 `requestId` 幂等参数。
- 在 `ui/config/routes.ts` 增加设备列表路由；在 `ui/src/access.ts` 补齐 `canDevicePage`、`canDeviceImport`、`canDeviceExport` 权限映射，页面按钮按权限展示。
- 在 `zh-CN.ts`、`en-US.ts` 补齐 `iot.device.*` 字段、占位符、校验与抽屉标题国际化文案。
- 审视并加固 `laokou-iot` 中已有的设备 DDD COLA 实现，补充请求参数校验（sn、name 必填，sn 租户内唯一）、产品归属校验、删除保护策略，确认与 product 一致的注解（`@Idempotent`、`@OperateLog`、`@TraceLog`、`@PreAuthorize`）齐全。
- 补充 PostgreSQL 幂等迁移脚本，新增 `iot:device:import`、`iot:device:export` 菜单权限种子，并按需补齐 `iot_device` 设备序列号租户内唯一索引。
- 补充前后端测试，覆盖成功、失败、权限不足与数据完整性场景。

## Capabilities

### New Capabilities

- `device-management`: 设备管理能力，覆盖设备的分页、详情、新增、修改、删除、导入、导出，前端页面与抽屉、权限胶水、后端 API 契约（`/api/v1/devices*`）、参数校验与数据库完整性。

### Modified Capabilities

- 无。当前 `openspec/specs/` 中没有与租户设备 CRUD 管理重叠的已有主规格需要修改（`edge-gateway-device-management` 针对边缘网关设备接入，与本变更的管理端 CRUD 不冲突）。

## Impact

- 前端：`ui/config/routes.ts`、`ui/src/access.ts`、`ui/src/pages/IoT/Device/device.tsx`（新增）、`ui/src/pages/IoT/Device/DeviceDrawer.tsx`（由 stub 实现）、`ui/src/services/iot/device.ts`、`ui/src/services/iot/typings.d.ts`、`ui/src/locales/zh-CN.ts`、`ui/src/locales/en-US.ts`。
- 后端：`laokou-service/laokou-iot` 下 `adapter`、`app`、`domain`、`infrastructure`、`client` 五层中的 `device` 包及 `DevicesController`。
- 数据库：PostgreSQL `iot_device` 表，以及 `sys_menu` 设备导入/导出权限种子；新增唯一索引前需先检查并清理重复 sn 数据。
- API：继续使用已有 `/api/v1/devices`、`/api/v1/devices/page`、`/api/v1/devices/{id}`、`/api/v1/devices/import`、`/api/v1/devices/export` 路径，保持向后兼容。
- 微服务影响：仅涉及 `laokou-iot` 服务和 `ui` 管理端；不引入 Kafka、Pulsar、MQTT 或遥测链路变更。
- 向后兼容：所有 API 路径与 DTO 字段保持不变，仅新增校验与权限种子，对现有调用方无破坏。
- 迁移策略：优先补充幂等迁移/初始化数据，不破坏已有表结构；新增唯一索引前先执行重复 sn 检查 SQL。
- 回滚计划：前端可移除路由和页面入口回滚；后端新增校验可按提交回滚；数据库迁移提供反向删除权限种子与索引的说明，避免删除业务数据。
