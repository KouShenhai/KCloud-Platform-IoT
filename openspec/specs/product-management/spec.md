## Purpose

产品（Product）管理能力提供 IoT 平台产品的全生命周期维护——前端控制台页面与后端分页查询过滤，覆盖产品的分页查询、按条件检索、新增、修改、查看详情、删除，并关联产品类别与设备类型。

## Requirements

### Requirement: 产品分页查询与条件检索

系统 SHALL 提供产品分页查询接口 `POST /iot/api/v1/products/page`，支持按产品名称（模糊匹配）、产品类别、设备类型、创建时间范围进行过滤，并返回分页结果（记录列表与总数）。该接口 MUST 要求 `read` 范围权限及 `iot:product:page` 授权。

#### Scenario: 无条件分页查询

- **WHEN** 已授权用户不带任何过滤条件调用 `/products/page`
- **THEN** 系统返回当前租户下未删除（`del_flag = 0`）的产品列表，按 `id` 倒序分页，并返回总记录数

#### Scenario: 按产品名称模糊检索

- **WHEN** 用户传入 `name` 过滤参数
- **THEN** 系统对产品名称执行 LIKE 模糊匹配，仅返回名称包含该关键字的产品

#### Scenario: 按产品类别与设备类型检索

- **WHEN** 用户传入 `categoryId` 或 `deviceType`
- **THEN** 系统按对应字段精确过滤，仅返回匹配的产品

#### Scenario: 按创建时间范围检索

- **WHEN** 用户传入开始日期与结束日期
- **THEN** 系统仅返回创建时间落在该区间内的产品

#### Scenario: 缺少权限

- **WHEN** 未持有 `iot:product:page` 授权的用户调用该接口
- **THEN** 系统拒绝请求并返回未授权错误

### Requirement: 产品新增

系统 SHALL 提供新增产品接口 `POST /iot/api/v1/products`，接收产品名称、产品类别、设备类型、产品图片 URL、通讯协议 ID、传输协议 ID、备注等字段。该接口 MUST 要求 `write` 范围权限及 `iot:product:save` 授权，并 MUST 支持基于 `request-id` 头的幂等控制。

#### Scenario: 成功新增产品

- **WHEN** 已授权用户在前端抽屉中填写产品名称与必填项并提交，且携带唯一 `request-id`
- **THEN** 系统创建产品记录、返回成功结果（`code = OK`），前端提示保存成功并刷新列表

#### Scenario: 重复提交幂等

- **WHEN** 用户以相同 `request-id` 重复提交同一新增请求
- **THEN** 系统不重复创建记录

#### Scenario: 缺少必填字段

- **WHEN** 用户未填写产品名称等必填字段提交
- **THEN** 前端表单校验阻止提交并提示对应必填文案

### Requirement: 产品修改

系统 SHALL 提供修改产品接口 `PUT /iot/api/v1/products`，按产品 ID 更新可编辑字段。该接口 MUST 要求 `write` 范围权限及 `iot:product:modify` 授权，并 MUST 通过版本号实现乐观锁，防止并发覆盖。

#### Scenario: 成功修改产品

- **WHEN** 已授权用户打开修改抽屉、变更字段并提交
- **THEN** 系统按 ID 更新产品记录并递增版本号，返回成功结果，前端提示修改成功并刷新列表

#### Scenario: 查看只读详情

- **WHEN** 用户点击「查看」
- **THEN** 系统按 `GET /products/{id}` 返回产品详情，前端以只读模式展示且隐藏提交按钮

### Requirement: 产品删除

系统 SHALL 提供删除产品接口 `DELETE /iot/api/v1/products`，接收产品 ID 数组，支持单条与批量逻辑删除。该接口 MUST 要求 `write` 范围权限及 `iot:product:remove` 授权。

#### Scenario: 删除单条产品

- **WHEN** 用户在某行点击「删除」并在确认弹窗中确认
- **THEN** 系统逻辑删除该产品（置 `del_flag = 1`），前端提示删除成功并刷新列表

#### Scenario: 批量删除

- **WHEN** 用户勾选多条记录后点击工具栏「删除」并确认
- **THEN** 系统批量逻辑删除选中产品

#### Scenario: 批量删除未选择记录

- **WHEN** 用户未勾选任何记录即点击批量删除
- **THEN** 前端提示「至少选择一条」且不发起请求

### Requirement: 产品管理控制台页面

系统 SHALL 在 IoT 控制台 `/iot/device/product` 路由下提供产品管理页面，基于 Ant Design Pro `ProTable` 与 `DrawerForm` 实现，支持中英文国际化，并依据用户权限位（`canProductSave/Modify/Remove/GetDetail`）显隐操作入口。

#### Scenario: 进入产品菜单

- **WHEN** 已授权用户从导航进入「产品」菜单
- **THEN** 系统加载产品列表页，展示产品名称、产品类别、设备类型、创建时间等列及搜索区与操作按钮

#### Scenario: 产品类别下拉选择

- **WHEN** 用户在新增/修改抽屉中选择产品类别
- **THEN** 系统提供来自产品类别接口的可选项供选择

#### Scenario: 设备类型枚举选择

- **WHEN** 用户选择设备类型
- **THEN** 系统提供「直连设备 / 网关设备 / 监控设备」枚举项，并在列表中以可读文案展示

#### Scenario: 按权限显隐操作

- **WHEN** 用户不具备某操作授权（如 `iot:product:remove`）
- **THEN** 前端不渲染对应的操作按钮
