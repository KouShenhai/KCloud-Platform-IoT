## 1. Current Structure and OpenSpec

- [x] 1.1 Review the `KEdge-Gateway` Go/Hertz route, local repository, auth middleware, and startup initialization patterns.
- [x] 1.2 Review the `edgeGatewayUI` Umi Max route, access, request, and user management page patterns.
- [x] 1.3 Create the edge gateway menu management proposal, design, spec, and implementation task list.

## 2. Backend Go Menu Management

- [x] 2.1 Add `internal/model/menu.go` for menu model, status constants, and tree node response model.
- [x] 2.2 Add `internal/repository/menu.go` with in-memory map storage, locking, CRUD, unique path validation, parent validation, cycle validation, and tree sorting.
- [x] 2.3 Implement `data/menus.yaml` YAML persistence using temp file + rename writes.
- [x] 2.4 Implement `InitDefaultMenus("data/menus.yaml")` to load or initialize default menus at startup.
- [x] 2.5 Add `internal/api/sys/menu.go` for list, detail, create, update, delete, and status handlers.
- [x] 2.6 Register `/api/v1/menus*` routes in `internal/api/router.go`, with JWT for reads and admin-only writes.
- [x] 2.7 Initialize the menu repository from `cmd/server/main.go`.

## 3. Frontend Umi Max Menu Page

- [x] 3.1 Add `edgeGatewayUI/src/services/menu.ts` for menu CRUD, status toggling, and types.
- [x] 3.2 Add `edgeGatewayUI/src/pages/MenuManagement/index.tsx` with `PageContainer`, `ProTable`, and `ModalForm`.
- [x] 3.3 Implement keyword search, parent menu selection, sort order, icon, status display, and enable/disable actions.
- [x] 3.4 Add the `/menus` route in `edgeGatewayUI/config/routes.ts` using `canManageMenus`.
- [x] 3.5 Add `canManageMenus` in `edgeGatewayUI/src/access.ts` for admin users.

## 4. Documentation and Verification

- [x] 4.1 Update `KEdge-Gateway/README.md` with menu APIs, default menus, and data file behavior.
- [x] 4.2 Update `edgeGatewayUI/README.adoc` with the menu page and admin access requirement.
- [x] 4.3 Run `go test ./...` or an equivalent `KEdge-Gateway` compile check.
- [x] 4.4 Run an edgeGatewayUI type check or build.
- [x] 4.5 Manually verify the main flows: default menu initialization, tree query, create, edit, protected delete, status toggle, admin access, and operator rejection.
