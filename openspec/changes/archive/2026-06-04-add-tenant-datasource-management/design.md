## Context

KCloud-Platform-IoT is heavily reliant on TDengine for time-series data storage. To support true multi-tenancy, each tenant needs the ability to configure and manage their own TDengine data source for their device telemetry data. The system currently provides a `sys_source` table and basic CRUD capabilities within the `laokou-admin-source` module. However, specific validation, connection testing, and the frontend management UI for these sources are not fully implemented for tenant-specific use cases.

## Goals / Non-Goals

**Goals:**
- Provide a robust backend mechanism to validate and save TDengine connection details (URL, driver, credentials).
- Implement a frontend management page for tenants/admins to add, modify, delete, and test data sources.
- Support TDengine JDBC Restful connections (`com.taosdata.jdbc.rs.RestfulDriver`).

**Non-Goals:**
- Automatic provisioning of TDengine instances. Tenants must bring their own instance or have it provisioned out of band.
- Supporting arbitrary database types beyond the currently needed relational and time-series DBs.

## Decisions

1. **Backend Validation Flow**: 
   - We will enhance `SourceDomainService` and `SourceGatewayImpl` to validate connection parameters upon saving or modifying a data source. 
   - A dedicated connection test method (`testConnection`) will be introduced to verify credentials before persisting.
   - We will utilize `laokou-common-tdengine` if available, or native JDBC validation, to verify the connection using `java.sql.DriverManager`.

2. **Frontend Architecture**:
   - The UI will be built as a standard Umi Max page using `@ant-design/pro-components` (`ProTable`, `DrawerForm`).
   - The route `/sys/tenant/source` is already initialized in `sys_menu` (ID 23), so we just need to provide the React component under `ui/src/pages/Sys/Tenant/Source`.
   - The form will explicitly support selecting "TDengine" and auto-filling the driver class name `com.taosdata.jdbc.rs.RestfulDriver` to reduce user error.

## Risks / Trade-offs

- **Risk: Connection testing might block or timeout.**
  - **Mitigation**: Implement a strict timeout (e.g., 5 seconds) on the JDBC connection test to prevent thread starvation.
- **Risk: Plaintext password storage in `sys_source`.**
  - **Mitigation**: Currently `sys_source` stores passwords in plaintext. As a trade-off for this iteration, we will follow existing patterns, but strongly recommend implementing reversible encryption in a future security hardening sprint.
