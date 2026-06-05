## Why

当前边缘网关的用户认证采用硬编码凭据（admin/admin123），缺乏动态用户管理能力，无法支持多用户场景、细粒度权限控制以及用户生命周期管理（新增、修改、禁用、删除），存在严重的安全隐患和运维盲区。现在需要在边缘侧引入完整的用户管理模块，提升网关系统的安全性与可运维性。

## What Changes

- **新增** 边缘网关用户数据模型（User），包含 ID、用户名、密码哈希、状态、角色、创建/更新时间字段
- **新增** 用户存储层（UserRepository），基于内存 map 实现用户的 CRUD 操作，支持持久化到本地 YAML 配置
- **新增** 用户管理 REST API（Go / Hertz），提供以下接口：
  - `POST /api/v1/users` — 创建用户
  - `GET /api/v1/users` — 用户列表（支持分页、关键字过滤）
  - `GET /api/v1/users/:id` — 查询单个用户
  - `PUT /api/v1/users/:id` — 更新用户（用户名、密码、状态、角色）
  - `DELETE /api/v1/users/:id` — 删除用户
  - `PUT /api/v1/users/:id/password` — 重置密码
- **修改** 登录逻辑，从硬编码凭据改为从用户存储中查询并验证 bcrypt 密码哈希
- **修改** JWT Payload，增加 `role` 字段用于权限判断
- **新增** 前端用户管理页面（Umi Max / React），包含用户列表、新建/编辑弹窗、删除确认
- **新增** 前端路由 `/users`，接入 Umi Max 布局与权限体系

## Capabilities

### New Capabilities
- `edge-user-management`: 边缘网关用户的增删改查及状态管理，包含前后端完整实现
- `edge-user-auth-enhancement`: 将登录认证从硬编码迁移到动态用户存储，密码使用 bcrypt 存储

### Modified Capabilities
- `edge-auth`: 登录接口需从动态用户存储中验证凭据（而非硬编码），JWT Payload 新增 role 字段

## Impact

- **后端**：`KEdge-Gateway/internal/` 下新增 `model/user.go`、`repository/user.go`、`api/sys/user.go`；修改 `api/sys/auth.go`、`api/router.go`
- **前端**：`edgeGatewayUI/src/` 下新增 `pages/UserManagement/`、`services/user.ts`；修改 `.umirc.ts` 路由配置、`src/app.ts` 初始权限状态
- **依赖**：后端需新增 `golang.org/x/crypto`（bcrypt）
- **向后兼容**：登录接口路径不变，仅内部实现变更；需在首次启动时自动初始化默认 admin 用户
