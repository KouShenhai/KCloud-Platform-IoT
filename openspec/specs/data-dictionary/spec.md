## Purpose

数据字典提供系统级枚举和基础配置值的维护能力，支撑管理员在管理端维护字典类型与字典项，并确保权限、租户隔离、导入导出和数据库初始化保持一致。

## Requirements

### Requirement: 字典类型管理
系统 SHALL 提供字典类型管理能力，管理员可以通过管理端和 REST API 对字典类型进行分页查询、详情查看、新增、修改、删除、导入和导出。字典类型 API MUST 继续使用已有路径：`POST /api/v1/dicts/page`、`GET /api/v1/dicts/{id}`、`POST /api/v1/dicts`、`PUT /api/v1/dicts`、`DELETE /api/v1/dicts`、`POST /api/v1/dicts/import`、`POST /api/v1/dicts/export`。字典类型数据 MUST 至少包含 `id`、`name`、`type`、`remark`、`status` 字段，且 `type` 在租户范围内唯一。

#### Scenario: 分页查询字典类型
- **GIVEN** 当前用户拥有 `read` scope 和 `sys:dict:page` 权限
- **WHEN** 用户提交包含 `pageNum`、`pageSize`、`name`、`type`、`status` 或时间范围参数的分页查询
- **THEN** 系统 SHALL 返回统一 `Result<Page<DictCO>>` 响应，结果仅包含当前租户可访问且未删除的字典类型

#### Scenario: 新增字典类型
- **GIVEN** 当前用户拥有 `write` scope 和 `sys:dict:save` 权限
- **WHEN** 用户提交合法的 `name`、`type`、`remark`、`status` 字段
- **THEN** 系统 SHALL 创建新的字典类型，并返回成功响应

#### Scenario: 拒绝重复字典类型
- **GIVEN** 当前租户已存在相同 `type` 的未删除字典类型
- **WHEN** 用户新增或修改字典类型为该 `type`
- **THEN** 系统 MUST 拒绝请求并返回业务错误，不得产生重复数据

#### Scenario: 权限不足无法维护字典类型
- **GIVEN** 当前用户缺少对应 `sys:dict:*` 权限或缺少所需 scope
- **WHEN** 用户访问字典类型查询、详情、新增、修改、删除、导入或导出接口
- **THEN** 系统 MUST 拒绝请求，前端 MUST 隐藏无权限操作入口

### Requirement: 字典项管理
系统 SHALL 提供字典项管理能力，管理员可以在选定字典类型后维护该类型下的字典项。字典项 API MUST 继续使用已有路径：`POST /api/v1/dict-items/page`、`GET /api/v1/dict-items/{id}`、`POST /api/v1/dict-items`、`PUT /api/v1/dict-items`、`DELETE /api/v1/dict-items`、`POST /api/v1/dict-items/import`、`POST /api/v1/dict-items/export`。字典项数据 MUST 至少包含 `id`、`label`、`value`、`sort`、`remark`、`status`、`typeId` 字段，且字典项 MUST 归属于存在且未删除的字典类型。

#### Scenario: 查询选中字典类型的字典项
- **GIVEN** 当前用户拥有 `read` scope 和 `sys:dict-item:page` 权限，并已选择一个字典类型
- **WHEN** 用户提交字典项分页查询，查询参数包含 `typeId`
- **THEN** 系统 SHALL 返回该 `typeId` 下未删除的字典项，并按 `sort` 和创建时间提供稳定排序

#### Scenario: 新增字典项
- **GIVEN** 当前用户拥有 `write` scope 和 `sys:dict-item:save` 权限，且 `typeId` 指向有效字典类型
- **WHEN** 用户提交合法的 `label`、`value`、`sort`、`remark`、`status`、`typeId`
- **THEN** 系统 SHALL 创建新的字典项，并在前端刷新当前字典类型的字典项列表

#### Scenario: 拒绝无效字典项归属
- **GIVEN** 请求中的 `typeId` 不存在、已删除或不属于当前租户
- **WHEN** 用户新增、修改或查询该 `typeId` 下的字典项
- **THEN** 系统 MUST 拒绝请求或返回空结果，不得跨租户泄露数据

#### Scenario: 拒绝同一字典类型下重复字典值
- **GIVEN** 同一 `typeId` 下已存在相同 `value` 的未删除字典项
- **WHEN** 用户新增或修改字典项为该 `value`
- **THEN** 系统 MUST 拒绝请求并返回业务错误，不得产生歧义字典值

### Requirement: 数据字典前端工作台
系统 SHALL 在 Umi Max 前端提供 `/sys/base/dict` 数据字典页面，并挂载到 `系统管理 / 基础数据 / 数据字典`。页面 MUST 使用 React 函数组件、TypeScript、Ant Design Pro 组件，复用 `ui/src/services/admin/dict.ts` 与 `ui/src/services/admin/dictItem.ts`，并遵循项目现有 `ProTable`、`DrawerForm`、`useAccess`、`useIntl` 模式。

#### Scenario: 打开数据字典页面
- **GIVEN** 当前用户可访问系统管理基础数据菜单
- **WHEN** 用户进入 `/sys/base/dict`
- **THEN** 前端 SHALL 展示字典类型列表，并在选择字典类型后展示对应字典项列表

#### Scenario: 页面按权限展示操作
- **GIVEN** 当前用户仅拥有部分 `sys:dict:*` 或 `sys:dict-item:*` 权限
- **WHEN** 前端渲染字典类型和字典项的工具栏及行操作
- **THEN** 前端 MUST 只展示用户有权执行的新增、查看、修改、删除、导入、导出操作

#### Scenario: 字典类型表单提交
- **GIVEN** 用户打开新增或修改字典类型抽屉
- **WHEN** 用户提交表单
- **THEN** 前端 MUST 校验必填字段，新增时携带幂等请求标识，成功后关闭抽屉并刷新列表

#### Scenario: 字典项表单提交
- **GIVEN** 用户已选择一个字典类型并打开新增或修改字典项抽屉
- **WHEN** 用户提交字典项表单
- **THEN** 前端 MUST 自动携带当前字典类型 `typeId`，成功后关闭抽屉并刷新当前字典项列表

### Requirement: 数据完整性与迁移
系统 SHALL 保持现有 API 路径、表名和基础数据兼容。数据库变更 MUST 使用幂等迁移或初始化脚本维护 `sys_dict`、`sys_dict_item`、`sys_menu`、`sys_i18n_menu` 及权限数据，且生产环境不得直接手工修改 schema。

#### Scenario: 初始化菜单和权限数据
- **GIVEN** 新环境执行数据库初始化或迁移脚本
- **WHEN** 迁移完成
- **THEN** 系统 SHALL 包含 `/sys/base/dict` 菜单、`menu.sys.base.dict` 国际化文案，以及 `sys:dict:*`、`sys:dict-item:*` 相关按钮权限

#### Scenario: 删除字典类型时保护字典项
- **GIVEN** 某字典类型下存在未删除的字典项
- **WHEN** 用户删除该字典类型
- **THEN** 系统 MUST 采用明确的数据保护策略：拒绝删除并提示先处理字典项，或在设计确认后执行同租户范围内的受控级联删除

#### Scenario: 回滚数据库变更
- **GIVEN** 数据库迁移引入新约束、索引或初始化数据
- **WHEN** 需要回滚本变更
- **THEN** 回滚脚本 MUST 不删除已有业务字典数据，并保留对旧 API 和旧表结构的兼容性
