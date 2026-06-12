## ADDED Requirements

### Requirement: 用户创建
系统 SHALL 允许管理员通过 API 创建新用户，用户名唯一，密码以 bcrypt 哈希存储，角色为 admin 或 operator。

#### Scenario: 成功创建用户
- **WHEN** 管理员提交合法用户名（3-32位字母数字下划线）、密码（>=8位）和角色（admin/operator）
- **THEN** 系统返回 HTTP 201，响应体包含新用户信息（不含密码哈希），并持久化到 YAML

#### Scenario: 用户名已存在
- **WHEN** 管理员提交的用户名与已有用户重复
- **THEN** 系统返回 HTTP 409 Conflict，错误信息说明用户名已存在

#### Scenario: 参数校验失败
- **WHEN** 管理员提交的用户名为空或密码少于8位或角色非法
- **THEN** 系统返回 HTTP 400 Bad Request，错误信息说明具体字段校验失败原因

#### Scenario: 未授权访问
- **WHEN** 请求未携带有效 JWT 或 JWT role 不为 admin
- **THEN** 系统返回 HTTP 401 或 HTTP 403

### Requirement: 用户列表查询
系统 SHALL 支持分页查询所有用户，并支持按用户名关键字过滤，响应中不含密码哈希字段。

#### Scenario: 获取全部用户列表
- **WHEN** 管理员发送 GET /api/v1/users（无查询参数）
- **THEN** 系统返回 HTTP 200，包含全部用户数组（字段：id、username、role、status、createdAt、updatedAt）

#### Scenario: 关键字过滤
- **WHEN** 管理员发送 GET /api/v1/users?keyword=op
- **THEN** 系统返回用户名包含 "op" 的用户列表

#### Scenario: 分页查询
- **WHEN** 管理员发送 GET /api/v1/users?page=2&pageSize=10
- **THEN** 系统返回第2页、每页10条数据，响应包含 total 字段

### Requirement: 用户详情查询
系统 SHALL 支持通过用户 ID 查询单个用户详情。

#### Scenario: 查询存在的用户
- **WHEN** 管理员发送 GET /api/v1/users/:id，且用户存在
- **THEN** 系统返回 HTTP 200，包含用户详情（不含密码哈希）

#### Scenario: 查询不存在的用户
- **WHEN** 管理员发送 GET /api/v1/users/:id，且用户不存在
- **THEN** 系统返回 HTTP 404 Not Found

### Requirement: 用户信息更新
系统 SHALL 允许管理员更新用户的用户名、角色和状态（active/disabled）。

#### Scenario: 成功更新用户名和角色
- **WHEN** 管理员发送 PUT /api/v1/users/:id，body 包含新用户名和角色
- **THEN** 系统返回 HTTP 200，返回更新后的用户信息，并持久化

#### Scenario: 禁用用户
- **WHEN** 管理员发送 PUT /api/v1/users/:id，body 包含 status=disabled
- **THEN** 系统返回 HTTP 200，该用户后续登录时返回 HTTP 403 账户已禁用

#### Scenario: 更新不存在的用户
- **WHEN** 管理员发送 PUT /api/v1/users/:id，且用户不存在
- **THEN** 系统返回 HTTP 404 Not Found

### Requirement: 用户删除
系统 SHALL 允许管理员删除用户，但不能删除最后一个 admin 用户。

#### Scenario: 成功删除普通用户
- **WHEN** 管理员发送 DELETE /api/v1/users/:id，目标用户存在且非最后 admin
- **THEN** 系统返回 HTTP 204 No Content，用户从存储中移除并持久化

#### Scenario: 拒绝删除最后一个 admin
- **WHEN** 管理员尝试删除系统中唯一的 admin 用户
- **THEN** 系统返回 HTTP 400，错误信息说明无法删除最后一个管理员

#### Scenario: 删除不存在的用户
- **WHEN** 管理员发送 DELETE /api/v1/users/:id，且用户不存在
- **THEN** 系统返回 HTTP 404 Not Found

### Requirement: 重置用户密码
系统 SHALL 允许管理员重置任意用户密码，也允许普通用户修改自己的密码。

#### Scenario: 管理员重置他人密码
- **WHEN** admin 用户发送 PUT /api/v1/users/:id/password，body 包含 newPassword
- **THEN** 系统返回 HTTP 200，新密码以 bcrypt 存储并持久化

#### Scenario: 用户修改自己密码
- **WHEN** 任意用户发送 PUT /api/v1/users/:id/password（id 为自己），body 包含 oldPassword 和 newPassword
- **THEN** 系统验证 oldPassword 正确后更新密码，返回 HTTP 200

#### Scenario: 旧密码错误
- **WHEN** 用户修改自己密码时提供的 oldPassword 不正确
- **THEN** 系统返回 HTTP 400，错误信息说明旧密码错误

### Requirement: 自动初始化默认 admin 用户
系统 SHALL 在首次启动时（YAML 文件不存在）自动创建默认 admin 用户（username=admin，password=admin123）。

#### Scenario: 首次启动自动初始化
- **WHEN** 系统启动时 data/users.yaml 不存在
- **THEN** 系统创建默认 admin 用户，写入 YAML，并在日志输出安全提示建议修改密码

#### Scenario: 非首次启动加载已有数据
- **WHEN** 系统启动时 data/users.yaml 存在
- **THEN** 系统从 YAML 加载所有用户到内存，不覆盖已有数据

### Requirement: 前端用户管理页面
系统 SHALL 提供前端用户管理页面，展示用户列表，支持新建、编辑、删除、重置密码操作，仅 admin 角色可访问。

#### Scenario: admin 用户访问用户管理页
- **WHEN** admin 角色用户导航到 /users
- **THEN** 前端显示用户列表页，包含 ProTable 展示所有用户，工具栏有「新建用户」按钮

#### Scenario: operator 用户访问用户管理页
- **WHEN** operator 角色用户尝试访问 /users
- **THEN** 前端根据 access 控制重定向到 403 无权限页面

#### Scenario: 新建用户弹窗
- **WHEN** admin 点击「新建用户」按钮
- **THEN** 弹出 ModalForm，包含用户名、密码、角色选择字段，提交后刷新列表

#### Scenario: 删除用户确认
- **WHEN** admin 点击某行「删除」按钮
- **THEN** 弹出 Popconfirm 确认对话框，确认后发送删除请求，成功后刷新列表
