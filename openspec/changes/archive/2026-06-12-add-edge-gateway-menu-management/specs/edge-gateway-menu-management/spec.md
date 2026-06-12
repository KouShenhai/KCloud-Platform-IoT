## ADDED Requirements

### Requirement: 菜单数据模型与默认初始化
系统 SHALL 在边缘网关本地维护菜单数据。菜单数据 MUST 包含 `id`、`name`、`path`、`icon`、`parentId`、`sort`、`status`、`createdAt`、`updatedAt` 字段。系统启动时 MUST 加载 `data/menus.yaml`；当文件不存在时 MUST 初始化默认菜单并写入该文件。

#### Scenario: 首次启动初始化默认菜单
- **WHEN** 边缘网关启动且 `data/menus.yaml` 不存在
- **THEN** 系统 SHALL 创建默认菜单数据并写入 YAML 文件

#### Scenario: 非首次启动加载菜单
- **WHEN** 边缘网关启动且 `data/menus.yaml` 已存在
- **THEN** 系统 SHALL 从 YAML 加载菜单到内存，且不得覆盖已有配置

### Requirement: 菜单查询 API
系统 SHALL 提供菜单查询 API。`GET /api/v1/menus` MUST 支持关键字过滤和树形返回，`GET /api/v1/menus/:id` MUST 返回单个菜单详情。查询接口 MUST 要求有效 JWT。

#### Scenario: 查询菜单树
- **WHEN** 已登录用户发送 `GET /api/v1/menus?tree=true`
- **THEN** 系统 SHALL 返回 HTTP 200，响应体包含按 `sort` 排序的树形菜单数据

#### Scenario: 关键字过滤菜单
- **WHEN** 已登录用户发送 `GET /api/v1/menus?keyword=user`
- **THEN** 系统 SHALL 返回名称或路径包含关键字的菜单列表

#### Scenario: 查询不存在菜单
- **WHEN** 已登录用户发送 `GET /api/v1/menus/:id` 且菜单不存在
- **THEN** 系统 SHALL 返回 HTTP 404

#### Scenario: 未认证无法查询菜单
- **WHEN** 请求未携带有效 JWT
- **THEN** 系统 MUST 返回 HTTP 401

### Requirement: 菜单创建与更新
系统 SHALL 允许 admin 用户创建和更新菜单。菜单名称、路径和状态 MUST 合法；未删除菜单的 `path` MUST 唯一；父级菜单存在时 MUST 指向有效菜单。

#### Scenario: 成功创建菜单
- **WHEN** admin 用户提交合法 `name`、`path`、`icon`、`parentId`、`sort`、`status`
- **THEN** 系统 SHALL 返回 HTTP 201，响应体包含新菜单，并持久化到 YAML

#### Scenario: 拒绝重复路径
- **WHEN** admin 用户创建或更新菜单时提交已存在的 `path`
- **THEN** 系统 MUST 返回 HTTP 409，且不得产生重复路径菜单

#### Scenario: 拒绝无效父菜单
- **WHEN** admin 用户提交不存在的 `parentId`
- **THEN** 系统 MUST 返回 HTTP 400，且不得保存该菜单

#### Scenario: 拒绝循环父子关系
- **WHEN** admin 用户将菜单父级设置为自己或自己的后代
- **THEN** 系统 MUST 返回 HTTP 400，且不得保存该修改

#### Scenario: 非 admin 无法写菜单
- **WHEN** operator 用户调用创建或更新菜单接口
- **THEN** 系统 MUST 返回 HTTP 403

### Requirement: 菜单删除与启停
系统 SHALL 允许 admin 用户删除、启用和禁用菜单。存在子菜单的菜单 MUST 拒绝删除。

#### Scenario: 成功删除叶子菜单
- **WHEN** admin 用户删除一个不存在子菜单的菜单
- **THEN** 系统 SHALL 返回 HTTP 204，并从 YAML 持久化文件中移除该菜单

#### Scenario: 拒绝删除父菜单
- **WHEN** admin 用户删除存在子菜单的菜单
- **THEN** 系统 MUST 返回 HTTP 400，并保留菜单数据

#### Scenario: 成功禁用菜单
- **WHEN** admin 用户发送 `PUT /api/v1/menus/:id/status` 且 body 包含 `status=disabled`
- **THEN** 系统 SHALL 更新菜单状态并持久化

### Requirement: 前端菜单管理页面
系统 SHALL 在 edgeGatewayUI 中提供 `/menus` 菜单管理页面。页面 MUST 使用 Umi Max + React 19 + Ant Design Pro，支持菜单树列表、关键字搜索、新建、编辑、删除和启停操作。页面 MUST 仅 admin 角色可访问。

#### Scenario: admin 访问菜单管理页
- **WHEN** admin 角色用户访问 `/menus`
- **THEN** 前端 SHALL 展示菜单管理页面和操作入口

#### Scenario: operator 无法访问菜单管理页
- **WHEN** operator 角色用户访问 `/menus`
- **THEN** 前端 MUST 根据 access 控制拒绝访问

#### Scenario: 新建菜单弹窗
- **WHEN** admin 点击新建菜单按钮
- **THEN** 前端 SHALL 弹出表单，提交成功后刷新菜单列表

#### Scenario: 删除菜单确认
- **WHEN** admin 点击删除菜单
- **THEN** 前端 SHALL 弹出确认对话框，确认后调用删除 API，成功后刷新列表
