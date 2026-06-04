## 1. 后端: 数据源验证与测试接口

- [x] 1.1 在 `SourceDomainService` 或相关工具类中，使用 JDBC 针对 `com.taosdata.jdbc.rs.RestfulDriver` 驱动实现数据库连接的验证逻辑。
- [x] 1.2 在 `SourceGatewayImpl`（或对应层）的保存和修改逻辑中，接入连接验证，若连接失败则抛出业务异常。
- [x] 1.3 扩展 `SourcesController`、定义相关 Command 和应用层 Exec，增加 `/v1/sources/test` 接口，支持前端测试数据源连通性。

## 2. 前端: 租户数据源管理页面

- [x] 2.1 在 `ui/src/services/admin/source.ts` 中补充数据源的 API 契约（page, save, modify, remove, testConnection）。
- [x] 2.2 在 `ui/src/pages/Sys/Tenant/Source` 创建数据源列表页面（使用 `ProTable` 展示名称、驱动、连接等字段）。
- [x] 2.3 在同目录下创建新增/编辑抽屉表单，专门适配 TDengine 参数（默认填充驱动名称 `com.taosdata.jdbc.rs.RestfulDriver`）。
- [x] 2.4 在表单中集成“测试连接”按钮，调用后端的 test 接口验证连通性。

## 3. 测试与验证

- [x] 3.1 运行后端测试，验证提供错误的 TDengine 连接信息时能否正确拦截。
- [x] 3.2 在前端 Umi Max 环境中完整验证数据源的列表、新增、连通性测试、修改和删除流程。
