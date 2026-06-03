## 1. 现状核对与数据库准备

- [x] 1.1 核对 `doc/db` 和运行初始化脚本中的 `sys_dict`、`sys_dict_item` 表结构、默认数据、菜单、国际化文案和权限资源。
- [x] 1.2 确认 `/sys/base/dict` 菜单、`menu.sys.base.dict` 文案、`sys:dict:*`、`sys:dict-item:*` 权限是否在目标环境可幂等初始化。
- [x] 1.3 检查现有数据是否存在重复字典类型 `type` 或同一 `typeId` 下重复字典项 `value`。
- [x] 1.4 如有缺失，补充幂等数据库迁移或初始化脚本，并提供重复数据检查和回滚说明。

## 2. 后端 DDD COLA 加固

- [ ] 2.1 在 `laokou-admin-client` 的字典 DTO/CO 或对应 command 校验中补齐必填、长度、状态值和 ID 校验。
- [ ] 2.2 在 `dict` 领域服务和网关中补齐租户内字典类型唯一性校验。
- [ ] 2.3 在 `dictItem` 领域服务和网关中补齐 `typeId` 归属校验和同类型字典项 `value` 唯一性校验。
- [ ] 2.4 实现字典类型删除保护：存在未删除字典项时拒绝删除并返回明确业务错误。
- [ ] 2.5 核对导入接口 `@RequestPart("files")`、导出接口 blob 响应和现有 OpenAPI 生成结果，修正不一致的契约。
- [ ] 2.6 为字典类型和字典项补充 controller/app/domain/infrastructure 层关键测试。

## 3. 前端 Umi Max 页面

- [x] 3.1 在 `ui/config/routes.ts` 挂载 `/sys/base/dict`，组件指向 `./Sys/Base/dict`。
- [x] 3.2 在 `ui/src/access.ts` 补齐 `canDict*` 和 `canDictItem*` 权限映射，覆盖 page/detail/save/modify/remove/import/export。
- [x] 3.3 新增 `ui/src/pages/Sys/Base/dict.tsx`，使用主从 `ProTable` 管理字典类型和字典项。
- [x] 3.4 新增或复用抽屉表单组件，分别处理字典类型和字典项的查看、新增、修改。
- [x] 3.5 实现字典类型搜索、选中刷新字典项、批量删除确认、状态展示和空选中提示。
- [x] 3.6 实现字典项搜索、新增时自动携带当前 `typeId`、排序字段、状态展示和删除确认。
- [x] 3.7 接入导入导出按钮，校正 admin service 路径前缀和 FormData 字段名 `files`。
- [x] 3.8 补齐 `zh-CN.ts` 和 `en-US.ts` 中数据字典页面、字段、占位符、提示文案。

## 4. 验证与文档

- [ ] 4.1 运行后端相关 Maven 测试，至少覆盖 `laokou-admin` 字典与字典项用例。
- [x] 4.2 运行前端类型检查、lint 或 build，确认新增页面可编译。
- [ ] 4.3 手工验证权限矩阵：无权限隐藏按钮，缺少 scope 时后端拒绝。
- [ ] 4.4 手工验证主要流程：分页查询、新增、修改、删除保护、导入、导出、失败提示。
- [ ] 4.5 更新项目二开文档或数据库说明，记录数据字典入口、权限和迁移注意事项。
