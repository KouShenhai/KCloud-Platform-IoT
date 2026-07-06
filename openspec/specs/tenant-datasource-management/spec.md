# Capability: Tenant Data Source Management

## Purpose
TBD (Covers the management of data sources by/for tenants, specifically focusing on the addition, validation, and management of TDengine data sources.)

## Requirements

### Requirement: 租户数据源基础管理
系统 SHALL 提供租户数据源的管理能力，允许租户或管理员在前端对数据源进行分页查询、新增、修改、删除等操作，支持配置 TDengine 等多种数据源。

#### Scenario: 分页查询数据源
- **GIVEN** 用户具备 `iot:source:page` 权限并进入数据源管理页面
- **WHEN** 用户执行分页查询
- **THEN** 系统 SHALL 返回当前租户关联的数据源列表，并在表格中展示名称、驱动名称、URL、用户名等信息

#### Scenario: 新增数据源
- **GIVEN** 用户具备 `iot:source:save` 权限
- **WHEN** 用户提交合法的数据源配置（如 TDengine: `com.taosdata.jdbc.rs.RestfulDriver`, `jdbc:TAOS-RS://host:port/db`, 等）
- **THEN** 系统 SHALL 保存该数据源并返回成功提示

#### Scenario: 修改数据源
- **GIVEN** 用户具备 `iot:source:modify` 权限且选中了已存在的数据源
- **WHEN** 用户更新数据源的 URL 或密码等配置并提交
- **THEN** 系统 SHALL 更新数据库记录并清空/刷新相关缓存

#### Scenario: 删除数据源
- **GIVEN** 用户具备 `iot:source:remove` 权限
- **WHEN** 用户选中一个或多个数据源并点击删除
- **THEN** 系统 SHALL 逻辑删除对应数据源记录并返回成功

### Requirement: 验证数据源连接
系统 SHALL 支持在保存或单独测试时验证数据源的连通性，防止配置错误的数据源被盲目保存。

#### Scenario: 自动验证配置连通性
- **GIVEN** 用户提交新增或修改数据源操作
- **WHEN** 驱动为 `com.taosdata.jdbc.rs.RestfulDriver` 时
- **THEN** 系统 SHALL 尝试使用提供的 URL、用户名和密码建立连接。若连接失败，系统 MUST 拒绝保存并返回"数据库连接失败"的业务异常。
