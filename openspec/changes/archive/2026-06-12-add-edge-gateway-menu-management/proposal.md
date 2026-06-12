## Why

边缘网关前端目前主要依赖静态路由和硬编码菜单，无法在边缘侧按实际部署裁剪管理入口，也无法让管理员维护菜单名称、路径、图标、排序和启停状态。用户管理已经建立了 Go/Hertz + 本地 YAML 持久化 + Umi Max 管理页的轻量模式，菜单管理应复用该模式补齐边缘侧系统管理闭环。

## What Changes

- 新增边缘网关菜单模型，包含 ID、名称、路径、图标、父级 ID、排序、状态、创建/更新时间字段。
- 新增菜单存储层，使用内存 map + `data/menus.yaml` 本地 YAML 持久化，启动时自动加载或初始化默认菜单。
- 新增 Go/Hertz 菜单管理 REST API：
  - `GET /api/v1/menus`：查询菜单树或平铺列表，支持关键字过滤
  - `GET /api/v1/menus/:id`：查询菜单详情
  - `POST /api/v1/menus`：创建菜单
  - `PUT /api/v1/menus/:id`：修改菜单
  - `DELETE /api/v1/menus/:id`：删除菜单
  - `PUT /api/v1/menus/:id/status`：启用或禁用菜单
- 新增菜单参数校验、路径唯一性校验、父级存在性校验、循环父子关系校验和删除保护。
- 修改边缘网关路由注册，菜单维护接口需要 JWT 认证，写操作需要 admin 角色。
- 新增 edgeGatewayUI 菜单管理服务和页面，使用 Umi Max + React 19 + Ant Design Pro 的 `ProTable` 和 `ModalForm` 管理菜单。
- 新增前端 `/menus` 路由，菜单管理入口仅 admin 可访问。
- 更新 README，说明菜单管理 API、默认菜单数据和本地持久化文件。

## Capabilities

### New Capabilities

- `edge-gateway-menu-management`: 边缘网关菜单的本地持久化、REST API 管理、默认初始化、权限控制和前端管理页面。

### Modified Capabilities

- 无。当前边缘网关已有用户管理、设备管理和认证规格不需要修改其既有 REQUIREMENTS。

## Impact

- 后端：新增 `KEdge-Gateway/internal/model/menu.go`、`internal/repository/menu.go`、`internal/api/sys/menu.go`，修改 `internal/api/router.go` 和 `cmd/server/main.go`。
- 前端：新增 `edgeGatewayUI/src/services/menu.ts`、`edgeGatewayUI/src/pages/MenuManagement/index.tsx`，修改 `edgeGatewayUI/config/routes.ts` 和 `edgeGatewayUI/src/access.ts`。
- 数据文件：新增运行时文件 `KEdge-Gateway/data/menus.yaml`，应作为边缘节点本地数据，不提交生产内容。
- API：新增 `/api/v1/menus*` 路径，不破坏已有 `/api/v1/users*`、`/api/v1/devices*`、`/api/v1/login`。
- 依赖：不新增 Go 或前端依赖，继续使用当前 Hertz、YAML、Umi Max 和 Ant Design Pro 依赖。
- 微服务影响：仅影响边缘网关 Go 服务和边缘网关前端，不触碰主平台 Java DDD COLA 服务。
- 回滚计划：移除新路由注册和前端路由即可停用菜单管理；保留 `data/menus.yaml` 不会影响其他功能。
