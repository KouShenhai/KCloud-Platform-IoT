# Tenant Management Spec

## Purpose

本规范定义租户管理能力，包括租户基础信息的增删改查、参数校验与删除保护、前端管理页面、导入导出契约，以及菜单权限与初始化数据要求。

## Requirements

### Requirement: 租户基础信息管理
系统 SHALL 提供租户基础信息管理能力，管理员可以通过管理端和 REST API 对租户进行分页查询、详情查看、新增、修改和删除。租户 API MUST 使用现有路径：`POST /api/v1/tenants/page`、`GET /api/v1/tenants/{id}`、`POST /api/v1/tenants`、`PUT /api/v1/tenants`、`DELETE /api/v1/tenants`。租户数据 MUST 至少包含 `id`、`name`、`code`、`sourceId`、`packageId` 字段。

#### Scenario: 分页查询租户
- **GIVEN** 当前用户拥有 `read` scope 和 `sys:tenant:page` 权限
- **WHEN** 用户提交包含 `pageNum`、`pageSize`、`name`、`code` 或时间范围参数的分页查询
- **THEN** 系统 SHALL 返回统一 `Result<Page<TenantCO>>` 响应，结果仅包含未删除租户

#### Scenario: 查看租户详情
- **GIVEN** 当前用户拥有 `read` scope 和 `sys:tenant:detail` 权限
- **WHEN** 用户访问 `GET /api/v1/tenants/{id}`
- **THEN** 系统 SHALL 返回对应租户详情

#### Scenario: 新增租户
- **GIVEN** 当前用户拥有 `write` scope 和 `sys:tenant:save` 权限
- **WHEN** 用户提交合法的 `name`、`code`、`sourceId`、`packageId`
- **THEN** 系统 SHALL 创建新的租户，并返回成功响应

#### Scenario: 修改租户
- **GIVEN** 当前用户拥有 `write` scope 和 `sys:tenant:modify` 权限，且租户存在
- **WHEN** 用户提交合法的 `id`、`name`、`code`、`sourceId`、`packageId`
- **THEN** 系统 SHALL 更新租户基础信息，并刷新对应租户缓存

### Requirement: 租户编码唯一性与参数校验
系统 MUST 校验租户请求参数。租户名称不能为空且长度不得超过 100 个字符，租户编码不能为空且长度不得超过 30 个字符，数据源 ID 和套餐 ID MUST 为非负数，未删除租户中的 `code` MUST 全局唯一。

#### Scenario: 拒绝重复租户编码
- **GIVEN** 已存在未删除租户使用编码 `tenant1`
- **WHEN** 用户新增租户或将其他租户编码修改为 `tenant1`
- **THEN** 系统 MUST 拒绝请求并返回业务错误，不得产生重复编码租户

#### Scenario: 拒绝非法租户参数
- **GIVEN** 当前用户拥有对应写权限
- **WHEN** 用户提交空名称、空编码、超长名称、超长编码、负数数据源 ID 或负数套餐 ID
- **THEN** 系统 MUST 拒绝请求并返回明确业务错误

#### Scenario: 拒绝修改不存在租户
- **GIVEN** 当前用户拥有 `write` scope 和 `sys:tenant:modify` 权限
- **WHEN** 用户提交不存在或已删除租户的 ID
- **THEN** 系统 MUST 拒绝请求并返回租户不存在错误

### Requirement: 租户删除保护
系统 SHALL 支持删除未删除且存在的租户，但 MUST 保护平台默认租户。默认租户包括 ID 为 `1` 的租户和编码为 `laokouyun` 的租户。

#### Scenario: 删除普通租户
- **GIVEN** 当前用户拥有 `write` scope 和 `sys:tenant:remove` 权限，且删除列表只包含普通租户
- **WHEN** 用户提交 `DELETE /api/v1/tenants`
- **THEN** 系统 SHALL 删除指定租户并返回成功响应

#### Scenario: 拒绝删除默认租户
- **GIVEN** 当前用户拥有 `write` scope 和 `sys:tenant:remove` 权限
- **WHEN** 用户提交的删除列表包含 ID 为 `1` 或编码为 `laokouyun` 的默认租户
- **THEN** 系统 MUST 拒绝整个删除请求，并保留所有租户数据不变

#### Scenario: 拒绝删除不存在租户
- **GIVEN** 当前用户拥有 `write` scope 和 `sys:tenant:remove` 权限
- **WHEN** 用户提交空 ID 列表、非法 ID 或不存在租户 ID
- **THEN** 系统 MUST 拒绝请求并返回明确业务错误

### Requirement: 租户前端管理页面
系统 SHALL 在 Umi Max 前端提供租户管理页面，并挂载到 `系统管理 / 租户管理 / 租户`，路由为 `/sys/tenant/index`。页面 MUST 使用 React 函数组件、TypeScript、Ant Design Pro 组件，复用 `ui/src/services/admin/tenant.ts`，并遵循项目现有 `ProTable`、`DrawerForm`、`useAccess`、`useIntl` 模式。

#### Scenario: 打开租户管理页面
- **GIVEN** 当前用户可访问系统管理租户菜单
- **WHEN** 用户进入 `/sys/tenant/index`
- **THEN** 前端 SHALL 展示租户列表，并提供名称、编码和创建时间筛选

#### Scenario: 页面按权限展示操作
- **GIVEN** 当前用户仅拥有部分 `sys:tenant:*` 权限
- **WHEN** 前端渲染租户页面的工具栏及行操作
- **THEN** 前端 MUST 只展示用户有权执行的新增、查看、修改、删除、导入、导出操作

#### Scenario: 租户表单提交
- **GIVEN** 用户打开新增或修改租户抽屉
- **WHEN** 用户提交表单
- **THEN** 前端 MUST 校验必填字段，新增时携带幂等请求标识，成功后关闭抽屉并刷新列表

### Requirement: 租户导入导出契约
系统 SHALL 保留租户导入导出 API 契约：`POST /api/v1/tenants/import` 和 `POST /api/v1/tenants/export`。前端上传文件字段名 MUST 与后端 `@RequestPart("files")` 保持一致。

#### Scenario: 导入租户字段匹配
- **GIVEN** 当前用户拥有 `write` scope 和 `sys:tenant:import` 权限
- **WHEN** 用户在前端选择文件并提交导入
- **THEN** 前端 MUST 使用 multipart 字段 `files` 上传文件，后端 SHALL 接收到 `MultipartFile[] files`

#### Scenario: 导出租户
- **GIVEN** 当前用户拥有 `write` scope 和 `sys:tenant:export` 权限
- **WHEN** 用户触发导出租户
- **THEN** 前端 SHALL 调用 `POST /api/v1/tenants/export`，后端 SHALL 保持现有导出接口契约

### Requirement: 租户菜单权限与初始化数据
系统 SHALL 保持数据库初始化中的租户菜单、国际化菜单和权限资源与前端路由一致。初始化数据 MUST 包含 `/sys/tenant/index` 菜单、`menu.sys.tenant`、`menu.sys.tenant.tenant` 文案，以及 `sys:tenant:page`、`sys:tenant:detail`、`sys:tenant:save`、`sys:tenant:modify`、`sys:tenant:remove`、`sys:tenant:import`、`sys:tenant:export` 权限。

#### Scenario: 初始化租户菜单和权限
- **GIVEN** 新环境执行数据库初始化或迁移脚本
- **WHEN** 迁移完成
- **THEN** 系统 SHALL 包含租户菜单、国际化文案和租户按钮权限，且前端路由能够匹配 `/sys/tenant/index`

#### Scenario: 权限不足无法维护租户
- **GIVEN** 当前用户缺少对应 `sys:tenant:*` 权限或缺少所需 scope
- **WHEN** 用户访问租户查询、详情、新增、修改、删除、导入或导出能力
- **THEN** 系统 MUST 拒绝后端请求，前端 MUST 隐藏无权限操作入口
