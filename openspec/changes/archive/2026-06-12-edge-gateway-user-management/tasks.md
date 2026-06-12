## 1. 后端 — 依赖与基础准备

- [ ] 1.1 在 `KEdge-Gateway/go.mod` 中添加 `golang.org/x/crypto` 依赖，运行 `go mod tidy`
- [x] 1.2 创建 `KEdge-Gateway/data/` 目录（存放 users.yaml），在 `.gitignore` 中添加 `data/users.yaml`（生产数据不提交）

## 2. 后端 — 用户数据模型

- [x] 2.1 创建 `KEdge-Gateway/internal/model/user.go`，定义 `User` 结构体（ID、Username、PasswordHash、Role、Status、CreatedAt、UpdatedAt）及角色/状态常量（admin/operator、active/disabled）

## 3. 后端 — 用户存储层

- [x] 3.1 创建 `KEdge-Gateway/internal/repository/user.go`，实现 `UserRepository`，包含：内存 `map[string]*models.User` + `sync.RWMutex`、`Create`、`List`（支持 keyword/page/pageSize）、`GetByID`、`GetByUsername`、`Update`、`Delete`、`UpdatePassword` 方法
- [x] 3.2 实现 YAML 持久化：`loadFromFile(path string)`（启动加载）和 `saveToFile(path string)`（原子写：先写临时文件再 rename）
- [x] 3.3 实现 `InitDefaultAdmin(path string)` 函数：检查文件是否存在，不存在则创建 bcrypt hash 的 admin 用户并写入 YAML；存在则从 YAML 加载到内存

## 4. 后端 — 用户管理 API Handler

- [x] 4.1 创建 `KEdge-Gateway/internal/api/sys/user.go`，实现以下 Handler 函数：
  - `CreateUser`（POST /api/v1/users）：参数校验、用户名唯一性检查、bcrypt 密码哈希、调用 repository.Create
  - `ListUsers`（GET /api/v1/users）：解析 keyword/page/pageSize 查询参数，调用 repository.List，响应不含 PasswordHash
  - `GetUser`（GET /api/v1/users/:id）：调用 repository.GetByID，404 处理
  - `UpdateUser`（PUT /api/v1/users/:id）：更新 username/role/status，校验禁用 admin 的边界条件
  - `DeleteUser`（DELETE /api/v1/users/:id）：检查是否为最后 admin，调用 repository.Delete
  - `ResetPassword`（PUT /api/v1/users/:id/password）：支持 admin 重置他人密码（无需旧密码）、用户修改自己密码（需验证旧密码）

## 5. 后端 — 登录逻辑改造

- [x] 5.1 修改 `KEdge-Gateway/internal/api/sys/auth.go`：将硬编码凭据验证替换为调用 `UserRepository.GetByUsername`，使用 `bcrypt.CompareHashAndPassword` 验证密码，检查账户 status（disabled 返回 403）
- [x] 5.2 修改 `KEdge-Gateway/internal/utils/` JWT 生成函数（或新增重载），使 JWT Claims 中包含 `userId`、`username`、`role` 三个字段
- [x] 5.3 更新 `KEdge-Gateway/internal/api/middlewares/` 的 AuthMiddleware：从 JWT Claims 中解析 `role`，注入到 RequestContext 以供 Handler 使用

## 6. 后端 — 路由注册与角色权限中间件

- [x] 6.1 创建 `KEdge-Gateway/internal/api/middlewares/admin.go`，实现 `AdminOnlyMiddleware`：检查 RequestContext 中的 role，非 admin 返回 HTTP 403
- [x] 6.2 修改 `KEdge-Gateway/internal/api/router.go`，注册用户管理路由：GET /api/v1/users 需 JWT 认证，POST/PUT/DELETE /api/v1/users 需 JWT 认证 + AdminOnly 中间件

## 7. 后端 — 启动初始化集成

- [x] 7.1 修改 `KEdge-Gateway/cmd/main.go`（或入口文件），在服务启动时调用 `InitDefaultAdmin("data/users.yaml")`，完成用户数据加载/初始化

## 8. 前端 — API 服务层

- [x] 8.1 创建 `edgeGatewayUI/src/services/user.ts`，封装以下 API 调用函数（使用 Umi Max request）：
  - `listUsers(params: { keyword?, page?, pageSize? })`
  - `createUser(data: { username, password, role })`
  - `updateUser(id, data: { username?, role?, status? })`
  - `deleteUser(id)`
  - `resetPassword(id, data: { oldPassword?, newPassword })`
- [x] 8.2 定义 TypeScript 类型 `User`（id、username、role、status、createdAt、updatedAt）和相关请求/响应类型

## 9. 前端 — 用户管理页面

- [x] 9.1 创建 `edgeGatewayUI/src/pages/UserManagement/index.tsx`，使用 `ProTable` 展示用户列表：
  - 列：用户名、角色（Tag 颜色区分）、状态（Badge）、创建时间、操作列（编辑/重置密码/删除）
  - 工具栏：「新建用户」按钮
  - 支持关键字搜索框（绑定 keyword 参数）
- [x] 9.2 实现「新建用户」`ModalForm`：用户名（必填，3-32位）、密码（必填，≥8位）、角色选择（Select：admin/operator），提交调用 createUser API
- [x] 9.3 实现「编辑用户」`ModalForm`：回填 username、role、status（启用/禁用 Switch），提交调用 updateUser API
- [x] 9.4 实现「重置密码」`ModalForm`：管理员重置时只需填写新密码；用户修改自己时需填写旧密码+新密码
- [x] 9.5 实现「删除用户」`Popconfirm`：确认后调用 deleteUser API，成功后调用 actionRef.current?.reload() 刷新列表

## 10. 前端 — 路由与权限配置

- [x] 10.1 修改 `edgeGatewayUI/.umirc.ts`，添加用户管理路由：`{ name: '用户管理', path: '/users', component: './UserManagement', access: 'canManageUsers' }`
- [x] 10.2 修改 `edgeGatewayUI/src/access.ts`，新增 `canManageUsers` 权限：解析 localStorage 中 JWT token 的 payload，role === 'admin' 时返回 true
- [x] 10.3 修改 `edgeGatewayUI/src/app.ts`，在 `getInitialState` 中从 JWT 解析当前用户信息（id、username、role）并注入全局状态，供 Layout 头像显示和 access 判断使用

## 11. 测试与验证

- [x] 11.1 后端：启动服务验证自动初始化 data/users.yaml，确认默认 admin 用户可登录
- [x] 11.2 后端：使用 curl/HTTPie 测试全部用户 CRUD 接口（含权限检查：operator token 调用 admin 接口应返回 403）
- [x] 11.3 后端：验证禁用用户无法登录（返回 403）
- [x] 11.4 前端：本地启动 edgeGatewayUI，验证用户管理页面列表展示、新建/编辑/删除/重置密码弹窗流程
- [x] 11.5 前端：验证 operator 角色用户无法看到用户管理菜单项（access 控制生效）

## 12. 文档更新

- [x] 12.1 更新 `KEdge-Gateway/README.md`，说明用户管理 API 接口清单及默认凭据说明
- [x] 12.2 更新 `edgeGatewayUI/README.adoc`，说明用户管理页面的访问权限要求
