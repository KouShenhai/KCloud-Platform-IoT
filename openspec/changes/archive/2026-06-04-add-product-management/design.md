## Context

KCloud-Platform-IoT 采用 DDD COLA 分层 + 微服务架构。产品（Product）属于 `laokou-iot` 微服务的设备域，与「产品类别（ProductCategory）」「物模型（ThingModel）」「设备（Device）」同属 IoT 设备管理。

当前状态（已通过代码勘察确认）：

- **后端已完整实现**（与 `dict`、`source`、`productCategory` 同构）：
  - adapter：`org.laokou.iot.web.ProductsController`（`/v1/products` 全套 REST 端点 + `iot:product:*` 鉴权 + `@Idempotent`）
  - app：`ProductsServiceImpl` + `ProductSaveCmdExe / ProductModifyCmdExe / ProductRemoveCmdExe / ProductImportCmdExe / ProductExportCmdExe` + `query/ProductPageQryExe / ProductGetQryExe`
  - domain：`ProductDomainService`、`ProductGateway`、`ProductE`
  - infrastructure：`ProductGatewayImpl`、`ProductConvertor`、`ProductDO`（`@TableName("iot_product")`）、`ProductMapper`、`mapper/product/ProductMapper.xml`
  - client：`ProductsServiceI` + `ProductCO` + 各 `*Cmd/*Qry`
  - 数据库：`iot_product` 表已在 `doc/db/kcloud_platform_iot.sql` 定义（含 `name / category_id / device_type / img_url / cp_id / tp_id / remark` 及审计字段）
- **后端唯一缺口**：`ProductMapper.xml` 的 `selectObjectPage` / `selectObjectCount` 仅过滤 `del_flag` 与创建时间，未使用 `ProductPageQry` 的 `name / categoryId / deviceType`，导致前端搜索条件无效。
- **前端为主要缺口**：
  - `ui/src/pages/IoT/Device/product.tsx`、`ProductDrawer.tsx` 是空桩 `export default () => {}`
  - `ui/config/routes.ts` 未注册 `/iot/device/product`（仅有 `thingModel`、`productCategory`）
  - `services/iot/product.ts`、`services/iot/typings.d.ts`（`ProductCO/ProductPageQry/...`）已就绪，但 `saveProduct` 缺少 `request-id` 幂等头
  - `access.ts` 已有 `canProductSave/Modify/Remove/GetDetail`，缺 `canProductPage/Import/Export`
  - locales 已有菜单键 `menu.iot.device.product`，缺字段级 `iot.product.*` 文案

约束：必须复用现有框架基类与约定（`ProTable`/`DrawerForm`、`request('/api-proxy/iot/api/v1/...')`、`StringExtUtils.like(trim())`、乐观锁 `selectVersion`），保持与 `productCategory`/`thingModel` 一致的代码风格。

## Goals / Non-Goals

**Goals:**

- 让产品管理端到端可用：前端列表 + 抽屉 + 路由 + 国际化 + 权限，对接已有后端 API。
- 修正后端分页查询，使 `name`（LIKE）、`categoryId`、`deviceType` 过滤生效，且向后兼容（不传参时行为不变）。
- 严格沿用项目既有模式（以 `productCategory.tsx` / `thingModel.tsx` / `ProductCategoryDrawer.tsx` 为模板），降低维护成本。

**Non-Goals:**

- 不改动 `iot_product` 表结构、不做数据迁移。
- 不实现产品与物模型/设备的级联绑定、产品发布、固件 OTA 等扩展能力。
- 不重写后端 DDD 各层（已存在且符合规范），仅修订 Mapper XML 与 PageQry setter。
- 导入/导出仅保证端点可调用，不在本次完善 Excel 模板细节（后端 import/export 当前为占位实现，遵循既有 `dict` 现状）。

## Decisions

**决策 1：以「补齐前端 + 修正后端查询」为范围，而非重建后端。**
理由：勘察确认后端 8 个产品类的实现与 `dict`/`source` 同构且编译就绪；重建会引入回归风险且违背「学习项目结构后复用」的意图。备选方案（推倒重写后端）被否决——成本高、收益为零。

**决策 2：前端页面以 `productCategory.tsx`（CRUD 抽屉交互）+ `thingModel.tsx`（扁平分页 + 多列搜索）为双模板。**
产品是扁平分页列表（非树），搜索含名称/类别/设备类型，故列表骨架取自 `thingModel.tsx`（`getPageQueryParam`、`rowSelection`、`pageXxx().then(records/total)`），抽屉取自 `ProductCategoryDrawer.tsx`（`DrawerForm` + `onFinish` 中按 `value.id` 区分 save/modify、`requestId` 幂等、只读模式隐藏提交）。备选（自研全新交互）被否决，破坏一致性。

**决策 3：产品类别选择使用 `ProFormSelect`，数据源取自 `pageProductCategory` 或类别树接口。**
产品仅需选择单个类别 ID，采用 `ProFormSelect` 的 `request` 拉取类别选项（`label=name, value=id`）。设备类型为固定枚举（1 直连/2 网关/3 监控），用静态 `options`，与 `thingModel` 的 `dataType/category` 选择写法一致。

**决策 4：后端查询过滤在 XML 中按 `productCategory`/`dict` 约定补全，`name` 走 LIKE。**
在 `ProductMapper.xml` 的 `selectObjectPage`/`selectObjectCount` 增加 `<if>` 条件：`name like #{pageQuery.name}`、`category_id = #{pageQuery.categoryId}`、`device_type = #{pageQuery.deviceType}`；并在 `ProductPageQry` 中重写 `setName` 套用 `StringExtUtils.like(StringExtUtils.trim(name))`（与 `DictPageQry` 一致），使 `%kw%` 由 DTO 层生成、XML 仅做 `like`。同时统一时间过滤参数命名（现有 XML 用 `params.startDate/endDate`，前端 `thingModel` 传 `params.startTime/endTime`——以现有 XML 的 `startDate/endDate` 为准，前端 `getPageQueryParam` 对齐传 `startDate/endDate`）。

**决策 5：幂等与权限对齐既有约定。**
`saveProduct` 增加 `request-id` 头参数（签名改为 `(body, requestId, options?)`），与 `saveProductCategory` 一致；`access.ts` 补 `canProductPage/Import/Export`，权限码沿用 `iot:product:page|import|export`。

## Risks / Trade-offs

- [时间过滤参数命名不一致 `startTime/endTime` vs `startDate/endDate`] → 以后端现有 `ProductMapper.xml` 的 `startDate/endDate` 为准，前端 `getPageQueryParam` 传 `params.startDate/endDate`，避免改动两侧；实施时核对一次确保字段匹配。
- [`saveProduct` 签名变更可能影响其他调用方] → 全仓搜索 `saveProduct` 引用，目前仅 `ProductDrawer`（桩）使用，变更安全。
- [前端 `typings.d.ts` 中 `ProductCO.Id` 字段大小写疑似笔误（应为 `id`）] → 实施时核对真实 typings，统一以后端 `ProductCO.id` 为准，必要时修正类型，避免列表 `rowKey="id"` 失效。
- [菜单/权限数据缺失导致按钮不显示] → 属 RBAC 授权数据问题而非代码缺陷；在 tasks 中提示验证当前账号具备 `iot:product:*` 授权，或在 DDL/菜单种子中补充（如环境需要）。
- [LIKE 查询无索引导致大数据量慢查询] → 当前数据量小，可接受；后续可在 `name` 上加索引，非本次范围。

## Migration Plan

1. 后端：修改 `ProductMapper.xml`、`ProductPageQry.java`；在 `laokou-iot-start` 本地启动验证 `/products/page` 过滤生效。
2. 前端：实现 `product.tsx`/`ProductDrawer.tsx`，注册路由，补 locales/access，调整 `product.ts`；`pnpm` 构建/类型检查通过。
3. 联调：登录控制台进入「产品」菜单，验证增删改查与搜索。
4. 回滚：前端还原桩与配置项、后端还原 XML 与 PageQry；无表结构变更，无数据风险。

## Open Questions

- 产品类别下拉是否需要展示为树形（受类别层级影响）？当前按单选 `ProFormSelect` 实现；如需树形可改 `ProFormTreeSelect` 接 `list-tree` 接口。
- 通讯协议 ID（`cpId`）/传输协议 ID（`tpId`）是否已有可选数据源接口？若暂无，先以数字输入（`ProFormDigit`）占位，后续接入协议管理后改为下拉。
