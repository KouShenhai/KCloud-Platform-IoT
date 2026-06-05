## Why

IoT 平台已有「产品类别」「物模型」「设备」等管理功能，但「产品（Product）管理」前端页面缺失：`ui/src/pages/IoT/Device/product.tsx` 与 `ProductDrawer.tsx` 目前是空桩（`export default () => {}`），路由也未注册，用户无法在控制台维护产品。

后端 `laokou-iot` 服务的产品功能已按 DDD COLA 分层完整实现（控制器、应用服务、命令/查询执行器、领域服务、网关、转换器、DO、Mapper、`iot_product` 表均已存在），仅在分页查询条件上缺少按名称/类别/设备类型过滤的能力。因此本次改动以补齐前端为主、修正后端查询过滤为辅，让产品管理端到端可用。

## What Changes

- 新增产品管理前端页面 `product.tsx`（基于 Ant Design Pro `ProTable` + React 19），支持分页列表、按产品名称/产品类别/设备类型搜索、新增、修改、查看、单条与批量删除。
- 新增 `ProductDrawer.tsx` 表单抽屉（基于 `DrawerForm` + `ProForm*`），用于新增/修改/查看产品，包含产品名称、产品类别（下拉，来自产品类别树）、设备类型（下拉枚举）、产品图片 URL、通讯协议 ID、传输协议 ID、备注等字段。
- 在 `ui/config/routes.ts` 注册 `/iot/device/product` 路由，指向 `./IoT/Device/product`。
- 在 `ui/src/locales/zh-CN.ts` 与 `en-US.ts` 补充 `iot.product.*` 字段级国际化文案（标题、占位、必填校验）。
- 在 `ui/src/access.ts` 补充 `canProductPage` / `canProductImport` / `canProductExport` 权限位，与现有 `canProductSave/Modify/Remove/GetDetail` 对齐。
- 为产品新增 `requestId` 幂等头：在 `ui/src/services/iot/product.ts` 的 `saveProduct` 增加 `request-id` 头参数，与 `productCategory` 一致。
- 后端补全 `ProductMapper.xml` 分页/计数 SQL 的查询过滤：按 `name`（LIKE）、`category_id`、`device_type` 过滤；并在 `ProductPageQry` 的 `name` setter 中套用 `StringExtUtils.like(trim(...))`，对齐项目搜索约定。

## Capabilities

### New Capabilities
- `product-management`: IoT 平台产品（Product）的全生命周期管理能力——前端控制台页面与后端分页查询过滤，覆盖产品的分页查询、按条件检索、新增、修改、查看详情、删除，并关联产品类别与设备类型。

### Modified Capabilities
<!-- 无既有 spec 的需求级行为变更；后端产品 API 行为变化（新增查询过滤）作为新能力 product-management 的一部分描述。 -->

## Impact

- 前端（`ui/`）：
  - 新增/实现：`src/pages/IoT/Device/product.tsx`、`src/pages/IoT/Device/ProductDrawer.tsx`
  - 修改：`config/routes.ts`、`src/locales/zh-CN.ts`、`src/locales/en-US.ts`、`src/access.ts`、`src/services/iot/product.ts`
- 后端（`laokou-service/laokou-iot`）：
  - 修改：`laokou-iot-infrastructure/.../mapper/product/ProductMapper.xml`（新增查询过滤）
  - 修改：`laokou-iot-client/.../product/dto/ProductPageQry.java`（`name` setter 套用 LIKE/trim）
- API：`/iot/api/v1/products`（POST/PUT/DELETE）、`/products/page`、`/products/{id}`、`/products/import`、`/products/export` 已存在，路径与鉴权（`iot:product:*`）不变；分页接口查询条件增强，向后兼容（不传过滤参数时行为不变）。
- 数据库：`iot_product` 表已存在，无需迁移；菜单与权限（`iot:product:*`）需在 RBAC 中具备对应授权数据方可在前端显示按钮。
- 影响范围限于 `laokou-iot` 微服务与 `ui` 控制台，不影响其他微服务。

## Rollback

- 前端：还原 `product.tsx`/`ProductDrawer.tsx` 为桩、移除 `routes.ts` 路由项与新增 locale/access 条目即可回滚，无数据副作用。
- 后端：还原 `ProductMapper.xml` 与 `ProductPageQry.java`；因仅增强查询过滤、无表结构变更，回滚无数据风险。
