## 1. 后端：分页查询过滤补全（laokou-iot）

- [ ] 1.1 在 `laokou-iot-client/.../product/dto/ProductPageQry.java` 重写 `setName`，套用 `StringExtUtils.like(StringExtUtils.trim(name))`（参照 `DictPageQry`），其余字段保持原样
- [ ] 1.2 在 `laokou-iot-infrastructure/.../mapper/product/ProductMapper.xml` 的 `selectObjectPage` 与 `selectObjectCount` 增加 `<if>` 过滤：`name like #{pageQuery.name}`、`category_id = #{pageQuery.categoryId}`、`device_type = #{pageQuery.deviceType}`，保留既有 `del_flag` 与 `startDate/endDate` 时间过滤
- [ ] 1.3 核对时间过滤参数命名以 XML 现有的 `params.startDate/endDate` 为基准，确保与前端 `getPageQueryParam` 传参一致
- [ ] 1.4 `mvn -q -pl laokou-service/laokou-iot/laokou-iot-infrastructure -am compile`（或 IoT 模块整体编译）验证后端编译通过

## 2. 前端：服务层与类型对齐（ui/src/services/iot）

- [ ] 2.1 修改 `services/iot/product.ts` 的 `saveProduct`，签名改为 `(body, requestId, options?)` 并在 headers 增加 `'request-id': requestId`（参照 `saveProductCategory`）
- [ ] 2.2 核对 `services/iot/typings.d.ts` 的 `ProductCO`，确认主键字段为小写 `id`（如为 `Id` 笔误则修正），并确认 `ProductPageQry/ProductSaveCmd/ProductModifyCmd/ProductExportCmd` 字段齐全

## 3. 前端：路由、权限与国际化

- [ ] 3.1 在 `ui/config/routes.ts` 的 `/iot/device` 子路由下新增 `{ name/title: 'menu.iot.device.product', path: '/iot/device/product', component: './IoT/Device/product' }`
- [ ] 3.2 在 `ui/src/access.ts` 补充 `canProductPage`（`iot:product:page` + read）、`canProductImport`/`canProductExport`（`iot:product:import|export` + write），与现有 `canProductSave/Modify/Remove/GetDetail` 对齐
- [ ] 3.3 在 `ui/src/locales/zh-CN.ts` 新增 `iot.product.*` 文案：名称、产品类别、设备类型、图片URL、通讯协议ID、传输协议ID、备注、insert/view/modify 标题、placeholder.* 与 required.* 校验文案，以及 `iot.product.deviceType.direct/gateway/monitor` 三个枚举文案
- [ ] 3.4 在 `ui/src/locales/en-US.ts` 新增与 3.3 对应的英文文案

## 4. 前端：产品抽屉组件 ProductDrawer.tsx

- [ ] 4.1 以 `ProductCategoryDrawer.tsx` 为模板创建 `ui/src/pages/IoT/Device/ProductDrawer.tsx`：定义 `TableColumns` 类型与 Props（modalVisit/setModalVisit/title/readOnly/dataSource/onComponent/requestId/setRequestId）
- [ ] 4.2 实现 `DrawerForm` 表单字段：隐藏 `id`、产品名称 `ProFormText`（必填）、产品类别 `ProFormSelect`（`request` 拉取产品类别选项，`label=name/value=id`）、设备类型 `ProFormSelect`（静态枚举 1/2/3）、图片URL `ProFormText`、通讯协议ID/传输协议ID `ProFormDigit`、备注 `ProFormTextArea`；只读时展示 `createTime`
- [ ] 4.3 实现 `onFinish`：`value.id === undefined` 时调用 `saveProduct({co: value}, requestId)`，否则 `modifyProduct({co: value})`；成功后 `message` 提示、关闭抽屉、调用 `onComponent` 刷新、重置 `requestId`/`loading`

## 5. 前端：产品列表页 product.tsx

- [ ] 5.1 以 `thingModel.tsx` 为模板创建 `ui/src/pages/IoT/Device/product.tsx`：引入 `pageProduct/getProductById/removeProduct`、`useAccess/useIntl`、`ProTable`、`uuid v7`、状态钩子（modalVisit/dataSource/title/readOnly/ids/requestId）
- [ ] 5.2 实现 `getPageQueryParam`（pageSize/pageNum/pageIndex + name/categoryId/deviceType + `params.startDate/endDate`）与 `rowSelection`
- [ ] 5.3 定义 `columns`：序号、产品名称（可搜索）、产品类别（select 搜索，选项来自类别接口）、设备类型（select 搜索 + 列内枚举文案渲染）、创建时间（dateTime 列 + dateRange 搜索）、操作列（查看/修改/删除，按 `access.canProduct*` 显隐）
- [ ] 5.4 实现 `ProTable` 的 `request` 调用 `pageProduct().then(records/total)`；`toolBarRender` 实现「新增」（重置 requestId、打开抽屉）与「批量删除」（空选校验 + 确认弹窗 + `removeProduct(ids)`）；挂载 `ProductDrawer`
- [ ] 5.5 删除/保存/修改成功后 `actionRef.current.reload()` 刷新列表，文案统一走 `t(...)`

## 6. 验证与收尾

- [ ] 6.1 前端类型检查与构建：在 `ui/` 运行 `pnpm tsc --noEmit`（或项目既有 lint/build 脚本）确认无类型与编译错误
- [ ] 6.2 本地联调（如环境可用）：启动 `laokou-iot` 与 `ui`，登录后进入「产品」菜单，验证分页、按名称/类别/设备类型搜索、新增、修改、查看、单条与批量删除
- [ ] 6.3 确认当前账号具备 `iot:product:page|save|modify|remove|detail` 授权（RBAC 菜单/权限数据）；若缺失，按需在菜单/权限种子中补充以便按钮显示
- [ ] 6.4 自检代码风格与既有 `productCategory`/`thingModel` 一致（命名、缩进、i18n key、`request` 前缀 `/api-proxy/iot/api/v1`）
