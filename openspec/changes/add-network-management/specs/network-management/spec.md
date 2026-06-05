## ADDED Requirements

### Requirement: 网络连接分页查询与条件检索

系统 SHALL 提供网络连接分页查询接口 `POST /network/api/v1/connections/page`，支持按连接名称（模糊匹配）、连接类型、启用状态、创建时间范围进行过滤，并返回分页结果（记录列表与总数）。该接口 MUST 要求 `read` 范围权限及 `network:connection:page` 授权。

#### Scenario: 无条件分页查询

- **WHEN** 已授权用户不带任何过滤条件调用 `/connections/page`
- **THEN** 系统返回当前租户下未删除（`del_flag = 0`）的连接列表，按 `id` 倒序分页，并返回总记录数

#### Scenario: 按连接名称模糊检索

- **WHEN** 用户传入 `name` 过滤参数
- **THEN** 系统对连接名称执行 LIKE 模糊匹配，仅返回名称包含该关键字的连接

#### Scenario: 按连接类型检索

- **WHEN** 用户传入 `type`（1 MQTT Server / 2 HTTP Server / 3 MQTT Client / 4 Kafka / 5 RabbitMQ）
- **THEN** 系统按类型精确过滤，仅返回该类型的连接

#### Scenario: 按启用状态检索

- **WHEN** 用户传入 `enabled`（0 启用 / 1 停用）
- **THEN** 系统按启用状态精确过滤，仅返回匹配的连接

#### Scenario: 按创建时间范围检索

- **WHEN** 用户传入开始日期与结束日期
- **THEN** 系统仅返回创建时间落在该区间内的连接

#### Scenario: 缺少权限

- **WHEN** 未持有 `network:connection:page` 授权的用户调用该接口
- **THEN** 系统拒绝请求并返回未授权错误

### Requirement: 网络连接新增

系统 SHALL 提供新增网络连接接口 `POST /network/api/v1/connections`，接收连接名称、连接类型、主机地址、端口、启用状态、类型特有配置（`config`，JSON 文本）、备注等字段。该接口 MUST 要求 `write` 范围权限及 `network:connection:save` 授权，并 MUST 支持基于 `request-id` 头的幂等控制。系统 MUST 校验连接名称非空、连接类型非空且取值 ∈ [1,5]、`config` 为合法 JSON。

#### Scenario: 成功新增连接

- **WHEN** 已授权用户在前端抽屉中选择连接类型、填写名称与该类型必填项并提交，且携带唯一 `request-id`
- **THEN** 系统创建连接记录、将类型特有字段聚合为 `config` JSON 持久化，返回成功结果（`code = OK`），前端提示保存成功并刷新列表

#### Scenario: 重复提交幂等

- **WHEN** 用户以相同 `request-id` 重复提交同一新增请求
- **THEN** 系统不重复创建记录

#### Scenario: 缺少必填字段

- **WHEN** 用户未填写连接名称或未选择连接类型即提交
- **THEN** 前端表单校验阻止提交并提示对应必填文案；若绕过前端，后端 SHALL 拒绝并返回参数错误

#### Scenario: 非法连接类型或非法配置

- **WHEN** 提交的 `type` 不在 [1,5] 区间，或 `config` 不是合法 JSON
- **THEN** 后端 SHALL 拒绝请求并返回参数错误，不创建记录

### Requirement: 网络连接修改

系统 SHALL 提供修改网络连接接口 `PUT /network/api/v1/connections`，按连接 ID 更新可编辑字段。该接口 MUST 要求 `write` 范围权限及 `network:connection:modify` 授权，并 MUST 通过版本号实现乐观锁，防止并发覆盖。连接类型 `type` 在修改时不可变更。

#### Scenario: 成功修改连接

- **WHEN** 已授权用户打开修改抽屉、变更字段并提交
- **THEN** 系统按 ID 更新连接记录并递增版本号，返回成功结果，前端提示修改成功并刷新列表

#### Scenario: 查看只读详情

- **WHEN** 用户点击「查看」
- **THEN** 系统按 `GET /connections/{id}` 返回连接详情（含 `config` 反序列化后的类型特有字段），前端以只读模式展示且隐藏提交按钮

#### Scenario: 并发修改冲突

- **WHEN** 两个用户基于同一版本号并发提交修改
- **THEN** 系统仅接受首个提交，后者因版本号不匹配而失败，不发生覆盖

### Requirement: 网络连接删除

系统 SHALL 提供删除网络连接接口 `DELETE /network/api/v1/connections`，接收连接 ID 数组，支持单条与批量逻辑删除。该接口 MUST 要求 `write` 范围权限及 `network:connection:remove` 授权。

#### Scenario: 删除单条连接

- **WHEN** 用户在某行点击「删除」并在确认弹窗中确认
- **THEN** 系统逻辑删除该连接（置 `del_flag = 1`），前端提示删除成功并刷新列表

#### Scenario: 批量删除

- **WHEN** 用户勾选多条记录后点击工具栏「删除」并确认
- **THEN** 系统批量逻辑删除选中连接

#### Scenario: 批量删除未选择记录

- **WHEN** 用户未勾选任何记录即点击批量删除
- **THEN** 前端提示「至少选择一条」且不发起请求

### Requirement: 网络连接管理控制台页面

系统 SHALL 在控制台 `/network/connection` 路由下提供网络连接管理页面，基于 Ant Design Pro `ProTable` 与 `DrawerForm` 实现，支持中英文国际化，并依据用户权限位（`canConnectionPage/GetDetail/Save/Modify/Remove`）显隐操作入口。新增/修改抽屉 MUST 根据所选连接类型动态渲染该类型的特有配置字段。

#### Scenario: 进入网络管理菜单

- **WHEN** 已授权用户从导航进入「网络管理」菜单
- **THEN** 系统加载连接列表页，展示连接名称、连接类型、主机、端口、启用状态、创建时间等列及搜索区与操作按钮

#### Scenario: 连接类型枚举选择与可读渲染

- **WHEN** 用户在搜索区或抽屉中选择连接类型
- **THEN** 系统提供「MQTT Server / HTTP Server / MQTT Client / Kafka / RabbitMQ」枚举项，并在列表中以可读文案展示类型

#### Scenario: 按连接类型动态渲染配置表单

- **WHEN** 用户在新增/修改抽屉中选择某一连接类型
- **THEN** 表单 SHALL 渲染该类型对应的特有配置字段组（MQTT Server / HTTP Server / MQTT Client 复用既有配置项语义，Kafka / RabbitMQ 渲染各自的 Vertx 连接参数），切换类型时切换字段组

#### Scenario: 配置字段与 config 的序列化

- **WHEN** 用户提交表单
- **THEN** 前端 SHALL 将类型特有字段聚合为 `config` JSON 字符串随请求提交；查看/修改时按 `config` 反序列化回显到对应字段

#### Scenario: 按权限显隐操作

- **WHEN** 用户不具备某操作授权（如 `network:connection:remove`）
- **THEN** 前端不渲染对应的操作按钮
