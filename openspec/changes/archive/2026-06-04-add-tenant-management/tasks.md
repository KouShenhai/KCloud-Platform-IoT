## 1. 现状核对与 OpenSpec

- [x] 1.1 核对 `sys_tenant`、租户菜单、国际化菜单和 `sys:tenant:*` 权限初始化数据。
- [x] 1.2 创建租户管理 OpenSpec proposal、design、spec 和任务清单。

## 2. 后端 DDD COLA 加固

- [x] 2.1 修正租户 API 契约字段，确保后端 `TenantCO.code` 与前端类型一致。
- [x] 2.2 在 `TenantDomainService` 中补齐租户参数校验、租户存在性校验和编码唯一性校验。
- [x] 2.3 在 `TenantDomainService` 和 `TenantGateway` 中实现默认租户删除保护。
- [x] 2.4 扩展 `TenantGatewayImpl` 与 `TenantMapper`，支持编码重复检查、租户存在检查和分页按名称/编码/时间范围过滤。
- [x] 2.5 校正租户导入接口前端字段与后端 `@RequestPart("files")` 一致。

## 3. 前端 Umi Max 页面

- [x] 3.1 在 `ui/config/routes.ts` 挂载 `/sys/tenant/index`，组件指向 `./Sys/Tenant`。
- [x] 3.2 在 `ui/src/access.ts` 补齐 `canTenant*` 权限映射，覆盖 page/detail/save/modify/remove/import/export。
- [x] 3.3 新增租户列表页面，使用 `ProTable` 提供名称、编码、创建时间筛选和分页。
- [x] 3.4 新增租户抽屉表单，支持查看、新增、修改，并按权限控制入口。
- [x] 3.5 接入批量删除、导入、导出按钮，修正 multipart 字段名为 `files`。
- [x] 3.6 补齐 `zh-CN.ts` 和 `en-US.ts` 中租户菜单、字段、占位符和提示文案。

## 4. 验证

- [x] 4.1 运行 OpenSpec 状态校验，确认任务制品 apply-ready。
- [x] 4.2 运行后端相关 Maven 编译或测试，至少覆盖 `laokou-admin` 与 `laokou-common-tenant` 受影响模块。
- [x] 4.3 运行前端类型检查、lint 或 build，确认新增页面可编译。
- [x] 4.4 手工核对主要流程：分页查询、新增、修改、删除保护、权限隐藏、导入字段、导出入口。
