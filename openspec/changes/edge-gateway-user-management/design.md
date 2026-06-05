## Context

边缘网关（KEdge-Gateway）是一个轻量级 Go 服务，运行在 IoT 边缘节点，使用 Hertz HTTP 框架对外暴露 REST API，通过 JWT 实现认证。当前用户凭据硬编码在 `auth.go` 中（admin/admin123），无法动态管理用户，存在安全风险。前端 edgeGatewayUI 基于 Umi Max + Ant Design Pro 构建。

本次变更在不引入外部数据库依赖的前提下，于边缘侧实现完整的用户 CRUD 管理能力，用户数据持久化到本地 YAML 文件。

## Goals / Non-Goals

**Goals:**
- 实现用户模型、存储层（内存 + 本地 YAML 持久化）、REST CRUD API（Go/Hertz）
- 将登录从硬编码凭据改为从用户存储查询，密码使用 bcrypt 哈希
- JWT Payload 扩展 `role` 字段，支持 admin / operator 两种角色
- 前端实现用户列表、新建、编辑、删除、重置密码页面（Umi Max / Ant Design Pro Table + Modal）
- 首次启动自动初始化默认 admin 用户

**Non-Goals:**
- 引入 SQLite、PostgreSQL 等数据库
- 实现细粒度 RBAC 权限控制（仅做角色字段预留）
- OAuth2 / SSO 集成
- 多网关用户同步

## Decisions

### D1：用户存储 — 内存 Map + YAML 持久化，而非引入数据库

边缘节点资源受限，引入数据库会增加部署复杂度。选择内存 `sync.Map` 为主存储，写操作后异步写入本地 `data/users.yaml`，启动时从文件加载。

**替代方案对比：**
| 方案 | 优点 | 缺点 |
|---|---|---|
| SQLite | 支持复杂查询 | CGO 依赖，交叉编译困难 |
| BoltDB | 纯 Go KV | API 复杂，序列化开销 |
| YAML 文件 | 简单、人可读、无依赖 | 并发写需加锁，大量用户性能下降 |

**结论**：YAML 文件足够边缘场景（用户数通常 < 100）。

### D2：密码存储 — bcrypt，cost=12

替代 plain text 硬编码。bcrypt 是 Go 标准库 `golang.org/x/crypto/bcrypt` 支持的行业标准，无需额外依赖管理复杂度。

### D3：角色模型 — 枚举字段（admin / operator）

当前场景下两个角色足够：
- `admin`：可管理用户、设备
- `operator`：只读设备数据

未来可扩展为完整 RBAC，当前 JWT 中携带 `role` 字段供前端路由权限判断。

### D4：前端用户管理 — ProTable + ModalForm（Ant Design Pro）

与项目现有 DeviceList 页面风格统一，使用 `@ant-design/pro-components` 的 `ProTable` 和 `ModalForm`，通过 Umi Max request 插件调用后端 API。

```
用户列表页 (UserManagement/index.tsx)
├── ProTable（支持关键字搜索、分页）
│   ├── 新建用户按钮 → ModalForm
│   ├── 编辑按钮 → ModalForm（回填数据）
│   ├── 重置密码按钮 → ModalForm（新密码）
│   └── 删除按钮 → Popconfirm
└── services/user.ts（API 调用封装）
```

### D5：路由与权限 — Umi Max access 控制

在 `.umirc.ts` 中为 `/users` 路由添加 `access: 'canManageUsers'`，在 `access.ts` 中根据 JWT 解析的 role 返回权限，仅 admin 角色可访问用户管理页。

## Risks / Trade-offs

- **[风险] 并发写 YAML 文件** → 所有写操作通过 `sync.RWMutex` 串行化，写完后异步持久化，启动时加载失败则重新初始化
- **[风险] 首次迁移：旧 admin 硬编码失效** → 启动时检查 YAML 是否存在，若不存在则自动创建默认 admin 用户（bcrypt 密码 `admin123`），并在日志中提示修改密码
- **[风险] JWT secret 硬编码** → 沿用现有 utils.GenerateToken 机制，本次不扩展（已有同等风险，记录为后续 backlog）
- **[Trade-off] YAML 持久化非原子写** → 使用先写临时文件再 rename 的方式保证写原子性，防止写到一半崩溃导致数据损坏

## Migration Plan

1. 后端：新增文件，修改 `auth.go` 登录逻辑和 `router.go` 路由注册
2. 启动时自动检测 `data/users.yaml` 是否存在：
   - 不存在 → 初始化默认 admin 用户（bcrypt hash），写入 YAML
   - 存在 → 加载并验证格式
3. 前端：新增页面和服务，更新路由配置
4. 回滚：若需回滚，注释掉新路由注册，恢复 auth.go 硬编码逻辑（1 行改动）
