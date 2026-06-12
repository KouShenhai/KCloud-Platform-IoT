# 边缘网关

## 快速启动

```shell
go mod tidy
```

#### Windows
```shell
cd cmd/server
go run main.go -c D:\laokou\KCloud-Platform-IoT\KEdge-Gateway\configs
go build -o app.exe main.go
app.exe -c D:\laokou\KCloud-Platform-IoT\KEdge-Gateway\configs
```

#### Linux
```shell
cd cmd/server
go run main.go -c /home/a
go build -o app main.go
chmod +x app
./app -c /home/a
```

#### 插件
```shell
# 安装tinygo
tinygo build -o mqtt_parser.wasm -target=wasi main.go
```

## 项目说明

[标准 Go 项目布局](https://github.com/golang-standards/project-layout)

## Edge Gateway User Management

The gateway stores local users in `data/users.yaml`. This file is ignored by
Git because it contains production account data. On first startup the service
creates a default administrator:

- Username: `admin`
- Password: `admin123`

Change the default password immediately after first login.

### Authentication

- `POST /api/v1/login` authenticates against the local user store.
- Successful login returns a JWT whose payload includes `userId`, `username`,
  and `role`.
- Disabled users are rejected with HTTP 403.

### User APIs

All user APIs require a valid `Authorization: Bearer <token>` header. Creating,
updating, and deleting users require the `admin` role.

- `GET /api/v1/users?keyword=&page=&pageSize=` lists users.
- `GET /api/v1/users/:id` gets one user.
- `POST /api/v1/users` creates a user with `username`, `password`, and `role`.
- `PUT /api/v1/users/:id` updates `username`, `role`, or `status`.
- `DELETE /api/v1/users/:id` deletes a user. The final admin cannot be deleted.
- `PUT /api/v1/users/:id/password` resets or changes a password. Admin users can
  reset any password with `newPassword`; non-admin users must provide
  `oldPassword` when changing their own password.

## Edge Gateway Menu Management

The gateway stores local menus in `data/menus.yaml`. This runtime data file is
ignored by Git. On first startup the service creates default menus for Home,
User Management, and Menu Management.

### Menu APIs

All menu APIs require a valid `Authorization: Bearer <token>` header. Querying
menus requires authentication. Creating, updating, deleting, and toggling menu
status require the `admin` role.

- `GET /api/v1/menus?keyword=&tree=true` lists menus. When `tree=true`, the
  response returns nested `children`.
- `GET /api/v1/menus/:id` gets one menu.
- `POST /api/v1/menus` creates a menu with `name`, `path`, `icon`, `parentId`,
  `sort`, and `status`.
- `PUT /api/v1/menus/:id` updates menu fields.
- `PUT /api/v1/menus/:id/status` toggles `status` between `enabled` and
  `disabled`.
- `DELETE /api/v1/menus/:id` deletes a menu. Menus with child menus cannot be
  deleted until their children are moved or removed.
