## Why

KCloud-Platform-IoT is an IoT cloud platform that heavily relies on time-series databases for telemetry data storage. While the system currently provides a basic structure for data source management (`sys_source` table) and tenant management, it lacks a dedicated feature for tenants to configure and manage their own TDengine data sources. Implementing this will allow tenants to securely connect their own TDengine instances for storing device data, completing the multi-tenant data isolation story.

## What Changes

- **Backend (DDD COLA)**: 
  - Enhance the existing `laokou-admin-source` module to specifically support TDengine connections.
  - Implement connection testing and validation for TDengine before saving the data source.
  - Ensure the `driverClassName` and connection URL formats are validated specifically for TDengine (e.g., `com.taosdata.jdbc.rs.RestfulDriver`).
- **Frontend (Umi Max)**:
  - Add or enhance the "数据源" (Datasource) management page under the Tenant Management menu.
  - Provide a user-friendly form to input TDengine specific configurations (Endpoint, Username, Password, Database).
  - Add connection testing capabilities from the UI.

## Capabilities

### New Capabilities

- `tenant-datasource-management`: Covers the management of data sources by/for tenants, specifically focusing on the addition, validation, and management of TDengine data sources.

### Modified Capabilities

- None.

## Impact

- **Backend**: Modifications in `laokou-admin-adapter` (Controllers), `laokou-admin-app` (Command Execs), and `laokou-admin-domain` (Validation).
- **Frontend**: New or updated pages in `ui/src/pages/Sys/Tenant/Source` and related API services.
- **Database**: No direct schema changes required, as `sys_source` already supports `driver_class_name`, `url`, `username`, and `password`.
