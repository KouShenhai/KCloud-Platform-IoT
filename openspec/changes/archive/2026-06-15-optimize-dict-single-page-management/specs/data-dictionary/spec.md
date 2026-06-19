## MODIFIED Requirements

### Requirement: 数据字典前端工作台
系统 SHALL 在 Umi Max 前端提供 `/sys/base/dict` 数据字典页面，并挂载到 `系统管理 / 基础数据 / 数据字典`。页面 MUST 使用 React 函数组件、TypeScript、Ant Design Pro 组件，复用 `ui/src/services/admin/dict.ts` 与 `ui/src/services/admin/dictItem.ts`，并遵循项目现有 `ProTable`、`DrawerForm`、`useAccess`、`useIntl` 模式。页面 SHALL 在同一个页面内提供字典类型和字典项的主从管理体验：字典类型作为主列表，字典项作为当前选中字典类型的明细列表；字典项查询、新增、删除、导入和导出 MUST 以当前选中字典类型为上下文。

#### Scenario: 打开数据字典页面
- **GIVEN** 当前用户可访问系统管理基础数据菜单
- **WHEN** 用户进入 `/sys/base/dict`
- **THEN** 前端 SHALL 展示字典类型列表和字典项管理区域
- **AND** 前端 SHALL 在未选择字典类型时提示用户先选择字典类型

#### Scenario: 选择字典类型后展示字典项
- **GIVEN** 当前用户拥有 `read` scope、`sys:dict:page` 权限和 `sys:dict-item:page` 权限
- **WHEN** 用户在字典类型列表中选择一个字典类型
- **THEN** 前端 SHALL 记录当前选中字典类型
- **AND** 前端 SHALL 使用该字典类型的 `id` 作为 `typeId` 查询字典项列表
- **AND** 前端 SHALL 在字典项区域展示当前字典类型名称或标识

#### Scenario: 页面按权限展示操作
- **GIVEN** 当前用户仅拥有部分 `sys:dict:*` 或 `sys:dict-item:*` 权限
- **WHEN** 前端渲染字典类型和字典项的工具栏及行操作
- **THEN** 前端 MUST 只展示用户有权执行的新增、查看、修改、删除、导入、导出操作

#### Scenario: 字典类型表单提交
- **GIVEN** 用户打开新增或修改字典类型抽屉
- **WHEN** 用户提交表单
- **THEN** 前端 MUST 校验必填字段，新增时携带幂等请求标识，成功后关闭抽屉并刷新字典类型列表
- **AND** 前端 SHALL 清理可能失效的选中字典类型和字典项选择状态

#### Scenario: 字典项操作依赖选中字典类型
- **GIVEN** 用户尚未选择字典类型
- **WHEN** 前端渲染字典项新增、批量删除、导入或导出操作
- **THEN** 前端 MUST 禁用这些操作或提示用户先选择字典类型
- **AND** 前端 MUST NOT 发起缺少 `typeId` 的字典项查询、导入或导出请求

#### Scenario: 字典项表单提交
- **GIVEN** 用户已选择一个字典类型并打开新增或修改字典项抽屉
- **WHEN** 用户提交字典项表单
- **THEN** 前端 MUST 自动携带当前字典类型 `typeId`，成功后关闭抽屉并刷新当前字典项列表

#### Scenario: 字典项导入导出限定当前字典类型
- **GIVEN** 用户已选择一个字典类型并拥有对应导入或导出权限
- **WHEN** 用户导入或导出字典项
- **THEN** 前端 MUST 在请求中携带当前字典类型 `typeId`
- **AND** 操作完成后前端 SHALL 刷新当前字典类型的字典项列表或保持当前查询上下文

#### Scenario: 响应式管理布局
- **GIVEN** 用户在不同宽度的浏览器中访问 `/sys/base/dict`
- **WHEN** 前端渲染数据字典页面
- **THEN** 页面 SHALL 在桌面宽度下同时展示字典类型和字典项管理区域
- **AND** 页面 SHALL 在较窄宽度下保持表格、工具栏和抽屉操作可用且不遮挡关键内容
