## ADDED Requirements

### Requirement: 设备分页查询

系统 SHALL 提供设备分页查询能力，管理端通过 `POST /api/v1/devices/page` 按条件分页返回设备列表。请求方 MUST 同时具备 `read` 作用域与 `iot:device:page` 权限。查询条件 SHALL 支持按设备名称（name，模糊）、设备序列号（sn，模糊）、状态（status）、产品（productId）和创建时间区间过滤，结果按创建时间倒序返回，并返回总数。

#### Scenario: 管理员成功分页查询设备

- **WHEN** 具备 `read` 与 `iot:device:page` 权限的用户提交合法分页参数（pageNum、pageSize）
- **THEN** 系统返回 `code = OK`、当前页设备列表与总记录数，列表仅包含未删除（del_flag = 0）且属于当前租户的数据

#### Scenario: 按名称模糊查询

- **WHEN** 用户在查询条件中传入设备名称关键字
- **THEN** 系统返回名称包含该关键字的设备分页结果

#### Scenario: 缺少分页查询权限被拒绝

- **WHEN** 不具备 `iot:device:page` 权限的用户调用分页接口
- **THEN** 系统拒绝请求并返回鉴权失败，不返回任何设备数据

### Requirement: 设备详情查询

系统 SHALL 提供按 ID 查询设备详情能力，管理端通过 `GET /api/v1/devices/{id}` 返回单个设备的完整字段。请求方 MUST 同时具备 `read` 作用域与 `iot:device:detail` 权限。

#### Scenario: 成功查询设备详情

- **WHEN** 具备 `read` 与 `iot:device:detail` 权限的用户传入存在且属于当前租户的设备 ID
- **THEN** 系统返回该设备的全部字段（sn、name、status、longitude、latitude、imgUrl、address、remark、productId、createTime）

#### Scenario: 查询不存在的设备

- **WHEN** 用户传入不存在或不属于当前租户的设备 ID
- **THEN** 系统返回空数据或未找到提示，不泄露其他租户数据

### Requirement: 设备新增

系统 SHALL 提供设备新增能力，管理端通过 `POST /api/v1/devices` 创建设备。请求方 MUST 同时具备 `write` 作用域与 `iot:device:save` 权限。接口 MUST 通过 `request-id` 头实现幂等。设备序列号 sn 与设备名称 name SHALL 为必填，sn 在同一租户内 SHALL 唯一，productId SHALL 引用存在的产品。

#### Scenario: 成功新增设备

- **WHEN** 具备权限的用户提交包含合法 sn、name、productId 的设备数据
- **THEN** 系统持久化设备并返回 `code = OK`，新设备 del_flag 为 0、tenant_id 为当前租户

#### Scenario: 缺少必填字段被拒绝

- **WHEN** 用户提交的 sn 或 name 为空
- **THEN** 系统返回参数校验失败，不创建设备

#### Scenario: 设备序列号租户内重复被拒绝

- **WHEN** 用户提交的 sn 在当前租户内已存在未删除设备
- **THEN** 系统返回唯一性冲突错误，不创建设备

#### Scenario: 重复提交相同请求保持幂等

- **WHEN** 同一 `request-id` 的新增请求被重复提交
- **THEN** 系统仅创建一条设备记录

### Requirement: 设备修改

系统 SHALL 提供设备修改能力，管理端通过 `PUT /api/v1/devices` 更新设备。请求方 MUST 同时具备 `write` 作用域与 `iot:device:modify` 权限。修改 SHALL 基于乐观锁版本号（version）执行，sn 唯一性校验 SHALL 排除自身。

#### Scenario: 成功修改设备

- **WHEN** 具备权限的用户提交存在设备的 ID 与合法字段
- **THEN** 系统更新该设备并递增 version，返回 `code = OK`

#### Scenario: 并发修改触发乐观锁

- **WHEN** 两个请求基于同一旧 version 并发修改同一设备
- **THEN** 仅有一个请求成功，另一个因版本不匹配失败

### Requirement: 设备删除

系统 SHALL 提供设备删除能力，管理端通过 `DELETE /api/v1/devices` 传入设备 ID 数组进行逻辑删除。请求方 MUST 同时具备 `write` 作用域与 `iot:device:remove` 权限。删除 SHALL 为逻辑删除（del_flag 置 1），并限定在当前租户范围内。

#### Scenario: 成功删除设备

- **WHEN** 具备权限的用户传入存在且属于当前租户的设备 ID 数组
- **THEN** 系统将对应设备 del_flag 置 1，后续分页与详情查询不再返回这些设备

#### Scenario: 缺少删除权限被拒绝

- **WHEN** 不具备 `iot:device:remove` 权限的用户调用删除接口
- **THEN** 系统拒绝请求，设备数据保持不变

### Requirement: 设备导入

系统 SHALL 提供设备批量导入能力，管理端通过 `POST /api/v1/devices/import`（multipart/form-data）上传文件批量创建设备。请求方 MUST 同时具备 `write` 作用域与 `iot:device:import` 权限。导入 SHALL 复用新增的必填与唯一性校验，单行失败 SHALL 给出可定位的错误信息。

#### Scenario: 成功导入设备文件

- **WHEN** 具备 `iot:device:import` 权限的用户上传格式正确的设备文件
- **THEN** 系统逐行校验并创建合法设备，返回导入结果

#### Scenario: 导入权限缺失被拒绝

- **WHEN** 不具备 `iot:device:import` 权限的用户调用导入接口
- **THEN** 系统拒绝请求，不创建任何设备

### Requirement: 设备导出

系统 SHALL 提供设备导出能力，管理端通过 `POST /api/v1/devices/export` 按查询条件导出设备数据。请求方 MUST 同时具备 `write` 作用域与 `iot:device:export` 权限。导出范围 SHALL 与分页查询的过滤条件一致，且仅包含当前租户数据。

#### Scenario: 成功导出设备

- **WHEN** 具备 `iot:device:export` 权限的用户提交导出条件
- **THEN** 系统导出符合条件的当前租户设备数据

#### Scenario: 导出权限缺失被拒绝

- **WHEN** 不具备 `iot:device:export` 权限的用户调用导出接口
- **THEN** 系统拒绝请求，不返回任何导出数据

### Requirement: 设备管理端页面与权限胶水

系统 SHALL 在 Umi Max 管理端提供设备管理页面，挂载到 `设备管理 / 设备`（路由 `/iot/device/device`）。页面 SHALL 使用 ProTable 展示设备列表，并提供新增、修改、查看、删除、导入、导出入口。每个操作入口 SHALL 依据对应 access 权限（`canDevicePage`、`canDeviceGetDetail`、`canDeviceSave`、`canDeviceModify`、`canDeviceRemove`、`canDeviceImport`、`canDeviceExport`）显隐，且字段文案 SHALL 提供中英文国际化。

#### Scenario: 有权限用户看到对应操作按钮

- **WHEN** 当前用户具备 `iot:device:save` 与 `write` 作用域
- **THEN** 设备页面显示"新增设备"按钮，点击后打开新增抽屉表单

#### Scenario: 无权限用户隐藏操作按钮

- **WHEN** 当前用户不具备某项设备操作权限
- **THEN** 页面不渲染对应操作按钮，且后端接口在被直接调用时仍拒绝

#### Scenario: 切换语言显示对应文案

- **WHEN** 用户在 zh-CN 与 en-US 之间切换语言
- **THEN** 设备页面字段标签、占位符、抽屉标题显示对应语言文案

### Requirement: 设备权限种子数据完整性

系统 SHALL 通过幂等迁移脚本保证 `sys_menu` 中存在设备管理的全部按钮权限种子，包括 `iot:device:page`、`iot:device:detail`、`iot:device:save`、`iot:device:modify`、`iot:device:remove`、`iot:device:import`、`iot:device:export`。迁移脚本 MUST 可重复执行且不破坏已有数据。

#### Scenario: 迁移补齐缺失的导入导出权限

- **WHEN** 数据库中尚未存在 `iot:device:import` 或 `iot:device:export` 权限种子
- **THEN** 迁移脚本插入这两条权限，挂在设备管理菜单下

#### Scenario: 迁移重复执行保持幂等

- **WHEN** 迁移脚本被重复执行
- **THEN** 已存在的权限种子不会被重复插入，不产生重复数据
