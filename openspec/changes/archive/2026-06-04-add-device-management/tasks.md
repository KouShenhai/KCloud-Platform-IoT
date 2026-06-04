## 1. 后端校验加固（laokou-iot）

- [x] 1.1 在 `DeviceDomainService` 中按项目既有 `ParamValidator` 约定（对齐 `DictDomainService`）实现 `sn` 非空、`name` 非空、`productId` 非空校验（比 Bean Validation 注解更贴合本项目分层）
- [x] 1.2 在 `DevicesController.saveDevice` / `modifyDevice` 入参确认 `@Validated` 已启用，对齐 product Controller
- [x] 1.3 在 `DeviceGateway`/`DeviceGatewayImpl` 补充 `existsSn`/`existsDevice`/`existsProduct` 查询方法（新增校验用，修改时排除自身 id），并补齐 `DeviceMapper.xml` 的 sn/name/status/productId 过滤
- [x] 1.4 在 `DeviceDomainService.createDevice` / `updateDevice` 中实现 sn 租户内唯一性校验与 productId 引用存在性校验，冲突时抛出 `ParamException`
- [x] 1.5 确认 `DeviceModifyCmdExe` / `DeviceGatewayImpl` 修改走乐观锁 version（对齐 `ProductGatewayImpl.updateProduct` 的 `selectVersion` 逻辑）
- [x] 1.6 移除 `DeviceSaveCmdExe` / `DeviceModifyCmdExe` 中 `// 校验参数` 占位注释，确保校验真实生效；并修复 `DeviceConvertor.toClientObject` 漏设 `id`/`createTime` 的缺陷

## 2. 数据库迁移（laokou-admin-start + doc）

- [x] 2.1 在 `laokou-service/laokou-admin/laokou-admin-start/src/main/resources/db/migration/` 新增设备权限迁移脚本（命名遵循 `V<date>_NN__device_permissions.sql`）
- [x] 2.2 脚本中以 `INSERT ... SELECT ... WHERE NOT EXISTS` 幂等插入 `iot:device:import`、`iot:device:export` 两条 `sys_menu` 权限，挂在设备菜单（pid=13）下，sort 接续现有设备权限；同步复制到 standalone-admin 迁移目录
- [x] 2.3 设备序列号租户内唯一索引随 `iot_device`（独立 iot 库、无 Flyway）维护到 `doc/db/kcloud_platform_iot.sql` 初始化脚本，并在脚本注释提供上线前重复 sn 检查 SQL
- [x] 2.4 同步更新 `doc/db/kcloud_platform.sql` 初始化文件，补齐导入/导出权限种子（id 105/106），保证新环境一致
- [x] 2.5 在迁移脚本中记录反向回滚 SQL（按 permission 软删除权限种子、DROP 唯一索引，不触碰业务数据）

## 3. 前端 service 与类型修正（ui）

- [x] 3.1 修正 `ui/src/services/iot/typings.d.ts` 中 `DeviceCO.Id` 为 `id`（小写），核对其余字段与后端 `DeviceCO` 一致
- [x] 3.2 给 `ui/src/services/iot/device.ts` 的 `saveDevice` 增加 `requestId: string` 参数并写入 `request-id` 头，对齐 `saveProduct`

## 4. 前端权限、路由与国际化（ui）

- [x] 4.1 在 `ui/src/access.ts` 补充 `canDevicePage`、`canDeviceImport`、`canDeviceExport` 三个权限映射，沿用 `permissions?.includes(...) && scopes?.includes(...)` 模式
- [x] 4.2 在 `ui/config/routes.ts` 的 IoT 设备路由下新增 `menu.iot.device.device` 路由项（path `/iot/device/index` 对齐 DB 菜单种子，component `./IoT/Device`）
- [x] 4.3 在 `ui/src/locales/zh-CN.ts` 补充 `iot.device.*` 字段、占位符、校验提示与抽屉标题（insert/view/modify）文案
- [x] 4.4 在 `ui/src/locales/en-US.ts` 补充与 zh-CN 对应的英文文案
- [x] 4.5 核对 `status` 字段在线/离线取值约定（后端 `DeviceCO`/`DeviceDO` 确认 0 在线 1 离线），据此确定 status 的 select options 文案

## 5. 前端设备页面与抽屉（ui）

- [x] 5.1 新建 `ui/src/pages/IoT/Device/index.tsx`，以 `product.tsx` 为模板用 ProTable 实现列表，列含 sn、name、status（select）、productId（关联产品下拉 + map 渲染名称）、address、createTime 与时间区间搜索
- [x] 5.2 在 index.tsx 中实现查看/修改/删除行操作与新增/批量删除工具栏按钮，按 `access.canDevice*` 显隐，删除走 `Modal.confirm`，新增时用 `uuid v7` 生成 requestId
- [x] 5.3 实现 `pageDevice` 查询参数组装（getPageQueryParam），含 name/sn 模糊、status、productId 过滤与时间区间转换
- [x] 5.4 用真实实现替换 `ui/src/pages/IoT/Device/DeviceDrawer.tsx` stub，以 `ProductDrawer.tsx` 为模板用 DrawerForm 实现设备字段表单，保存/修改调用 `saveDevice`(带 requestId)/`modifyDevice`，只读模式禁用编辑
- [x] 5.5 在 index.tsx 接入产品下拉数据加载（复用 `pageProduct`，维护 productMap 用于列渲染）

## 6. 测试与验证

- [x] 6.1 后端：为 `DeviceDomainService` 补充单元测试（`DeviceDomainServiceTest`，10 个用例），覆盖成功、必填缺失、sn 重复、产品不存在、状态非法、修改设备不存在、删除空 IDS 场景，全部通过
- [ ] 6.2 后端：`DevicesController` 接口测试未补充——IoT 模块无 adapter 层测试脚手架，仓库内唯一的 MockMvc 测试依赖运行中的 OAuth2 授权服务 + PostgreSQL + Redis（本环境不可用）；`@PreAuthorize` 权限串与已验证的 product 控制器完全一致，业务规则已由 6.1 领域单测覆盖
- [x] 6.3 后端：`laokou-iot` 全 7 个子模块离线编译通过（domain/client/infrastructure/app/adapter/start），domain 单测通过
- [x] 6.4 前端：`tsc --noEmit` 类型检查通过（exit 0，0 错误），device 页面、抽屉、路由、access、locale 无类型错误
- [ ] 6.5 数据库：迁移脚本未在实际库执行验证——本环境无 PostgreSQL 实例；脚本采用 `WHERE NOT EXISTS` 与 `CREATE UNIQUE INDEX IF NOT EXISTS` 保证幂等，已对齐已验证的 dict 迁移写法，待部署环境执行

## 7. 文档与收尾

- [x] 7.1 设备管理端点、权限点与页面入口已在本变更 proposal/spec 中完整记录；IoT 模块无独立 README 需要更新，端点契约沿用既有 `/api/v1/devices*`
- [x] 7.2 自检前端按钮显隐与后端 `@PreAuthorize` 一致：7 个 `iot:device:*` 权限在 `DevicesController`、`access.ts`、设备页面三处完全对齐，无权限用户既看不到按钮，后端也强校验
- [x] 7.3 确认 git 改动范围与 proposal 的 Impact 一致（laokou-iot 五层 + ui + doc/db + standalone 迁移 + openspec）；`ui/src/types/`、`KEdge-Gateway` 等为会话开始前的既有未跟踪项，非本次产生，无临时文件残留
