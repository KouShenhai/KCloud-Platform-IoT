## Context

KCloud-Platform-IoT 采用微服务 + DDD COLA 分层架构。设备管理位于 `laokou-iot` 服务，与产品（product）、产品类别（product-category）、物模型（thing-model）属同一限界上下文。

当前状态：

- **后端**：`laokou-iot` 的 `device` 包已在 client / adapter / app / domain / infrastructure 五层完整落地，`DevicesController` 暴露了 `/v1/devices` 全部 7 个端点（page、detail、save、modify、remove、import、export），权限注解与 product 一致。但 `DeviceSaveCmdExe` 仅有 `// 校验参数` 注释占位，缺少实际的必填、唯一性、产品归属校验。
- **前端 service**：`ui/src/services/iot/device.ts` 已实现全部请求函数，但 `saveDevice` 缺少 product 模式中的 `requestId` 幂等头；`typings.d.ts` 的 `DeviceCO` 字段使用了 `Id`（大写）而非 `id`。
- **前端页面**：`ui/src/pages/IoT/Device/device.tsx` 不存在，`DeviceDrawer.tsx` 是空 stub（`export default () => {};`）。
- **权限胶水**：`access.ts` 已有 `canDeviceGetDetail/Modify/Remove/Save`，缺 `canDevicePage/Import/Export`；路由 `routes.ts` 缺设备列表项；locale 缺 `iot.device.*` 字段文案。
- **数据库**：`iot_device` 表与 `iot:device:detail/remove/modify/save/page` 权限种子已在 `doc/db/kcloud_platform.sql` 存在，但缺 `iot:device:import` / `iot:device:export` 权限，且无 sn 租户内唯一索引。

参照实现：最近合入的"产品管理"（commits `e6f7eb2`、`1e518a0`）是同上下文的完整闭环范例，本变更逐一对齐其结构。

## Goals / Non-Goals

**Goals:**

- 完成设备管理在 Umi Max 管理端的前端闭环（列表页 + 抽屉表单 + 权限显隐 + 国际化）。
- 复用并对齐 `device.ts` service 与 product 的约定（`requestId` 幂等）。
- 加固后端设备校验：sn / name 必填、sn 租户内唯一、productId 引用存在、修改基于乐观锁。
- 通过幂等迁移补齐 `iot:device:import` / `iot:device:export` 权限种子与 sn 唯一索引。
- 补充前后端测试，覆盖成功 / 失败 / 权限不足 / 数据完整性场景。

**Non-Goals:**

- 不改动设备遥测、规则引擎、告警、OTA 等链路。
- 不引入 Kafka / Pulsar / MQTT 消息流。
- 不修改 `edge-gateway-device-management`（边缘网关接入）相关能力。
- 不改造 product / product-category / thing-model 现有功能。

## Decisions

### 复用既有 COLA 设备骨架，仅补校验而非重写

后端五层已存在并与 product 同构。决定**保留现有类与方法签名**，仅在 `DeviceSaveCmdExe` / `DeviceModifyCmdExe` 或领域服务中补充校验逻辑，避免破坏 API 契约。

- 校验放置：必填字段用 client 层 DTO 上的 Bean Validation 注解（与 product 的 `@Validated` 一致）；sn 唯一性与 productId 归属属业务规则，放在领域服务 `DeviceDomainService`，通过 `DeviceMapper` 查询判断。
- 备选：在 Controller 层校验——否决，违反 COLA 分层，业务规则应内聚于 domain。

### 前端页面以 product.tsx 为模板复刻

`device.tsx` 直接复刻 `product.tsx` 的 ProTable 结构，差异点：

- 列：sn、name、status（在线/离线 select）、productId（关联产品下拉，复用 `pageProduct` 加载 options + 维护 `productMap` 渲染名称，模式同 product 的 categoryId）、createTime + createTime 区间搜索。
- `DeviceDrawer.tsx` 复刻 `ProductDrawer.tsx`，字段换为设备字段，新增/修改调用 `saveDevice`/`modifyDevice`，保存走 `requestId` 幂等。
- 备选：抽象通用 CRUD 组件——否决，当前项目每个实体独立页面，保持一致性优先。

### saveDevice 增加 requestId 幂等参数

后端 `DevicesController.saveDevice` 带 `@Idempotent`（依赖 `request-id` 头）。决定给 `device.ts` 的 `saveDevice` 增加 `requestId: string` 参数并写入 `request-id` 头，与 `saveProduct` 完全对齐，页面在打开新增抽屉时用 `uuid v7` 生成。

### typings.d.ts 修正 DeviceCO 字段

将 `DeviceCO.Id` 修正为 `id`（小写），与后端 `DeviceCO`、表格 `rowKey="id"` 及 `getDeviceById({ id })` 调用一致，避免渲染与选中错位。

### 数据库变更走 admin-start 幂等迁移脚本

参照 `V20260604_01__data_dictionary_permissions.sql`，在 `laokou-service/laokou-admin/laokou-admin-start/src/main/resources/db/migration/` 新增设备权限迁移脚本：

- `INSERT ... SELECT ... WHERE NOT EXISTS` 补 `iot:device:import` / `iot:device:export` 两条 `sys_menu` 权限，挂在设备菜单（pid=13）下。
- `CREATE UNIQUE INDEX IF NOT EXISTS` 为 `iot_device(sn, tenant_id) WHERE del_flag = 0` 建唯一索引；脚本头部以注释给出上线前重复 sn 检查 SQL。
- 备选：直接改 `doc/db/kcloud_platform.sql` 初始化文件——同时更新初始化文件保持新环境一致，但既有环境必须靠幂等迁移升级，二者都做。

## Risks / Trade-offs

- [既有环境存在重复 sn 数据导致唯一索引创建失败] → 迁移脚本头部提供重复检查 SQL，先人工清理再建索引；`IF NOT EXISTS` 保证可重复执行。
- [`DeviceCO.Id` 改名可能影响其他已引用处] → 全仓搜索 `DeviceCO` / `\.Id` 引用确认仅 device 页面使用后再改；device 页面尚未实现，影响面可控。
- [新增 sn 唯一性校验可能与导入批量插入冲突] → 导入逐行复用同一校验路径，单行失败返回可定位错误，不整体回滚已成功行（与 product 导入行为一致）。
- [前端权限按钮隐藏不等于后端鉴权] → 后端 `@PreAuthorize` 始终强校验，前端 access 仅控制显隐；spec 已明确二者并存。
- [迁移脚本权限 ID 与既有种子冲突] → 选取未占用的 `sys_menu` 主键区段，插入前 `WHERE NOT EXISTS` 按 permission 判重。

## Migration Plan

1. 后端补校验 + DTO 校验注解，编译 `laokou-iot`。
2. 新增 admin-start 迁移脚本（权限种子 + sn 唯一索引）；同步更新 `doc/db/kcloud_platform.sql` 初始化文件。
3. 前端实现 `device.tsx` / `DeviceDrawer.tsx`，补 `routes.ts`、`access.ts`、locale，修 `device.ts` / `typings.d.ts`。
4. 运行后端单测与前端构建验证。
5. **回滚**：前端按提交回滚页面与路由；后端校验按提交回滚；数据库提供反向脚本删除新增的两条权限种子与唯一索引（不触碰业务数据）。

## Open Questions

- 设备 `status` 字段语义：前端 service 注释为 `0在线 1离线`，但 SQL 默认 `status = 1`。实现前需与既有约定核对在线/离线取值，locale 与 select options 以核对结果为准。
- 导入文件模板格式（Excel 列定义）是否已有既有约定，沿用 product 导入模板风格即可。
