# Apache IoTDB

The platform integrates Apache IoTDB through the `iotdb` data source type and
the JDBC 2.0.8 client. The adapter targets IoTDB's Tree data model.

## Data source settings

- Endpoint: `host[:port]`; port defaults to `6667`.
- Username and password: the IoTDB account used for JDBC RPC access.
- Database name: a valid IoTDB path below `root`, such as `kcloud_platform_iot`
  or `root.kcloud_platform_iot`.

The data source check opens a bounded JDBC connection and runs `SHOW DATABASES`.
The adapter uses a HikariCP pool with bounded connection, validation, and query
timeouts. `close()` releases the pool.

## Schema mapping

`DataSource.create(Table)` reads `SHOW DATABASES` and creates the configured
database only when its exact path is absent. It then reads
`SHOW TIMESERIES <device>.*` and creates only missing measurements. This uses
ordinary `CREATE DATABASE` and `CREATE TIMESERIES` syntax, as supported by
IoTDB 2.0.11. If a concurrent creator wins after the existence check, the
adapter rechecks the catalog and treats the operation as successful when the
requested path is now present.

Each table is represented as a Tree-model device under the configured
database. A relative table name such as `sensor_1` maps to
`root.<database>.sensor_1`; an already-qualified device path is accepted only
when it remains below the configured database. Each column becomes a
measurement. Names use ASCII letters, digits, and underscores, and measurement
names cannot contain path separators.

Supported measurement types are `boolean`, `int`, `long`, `float`, `double`,
`text`, `string`, `enum`, `blob`, `date`, and `timestamp`. Type names are
case-insensitive; `enum` is stored as `STRING`.

The current `DataSource` contract exposes connection validation and schema
creation; it does not define generic telemetry insert or query operations.

## Validation

The storage module's unit tests cover endpoint normalization, type mapping,
database path boundaries, generated DDL, existing-path checks, and concurrent
creation races without requiring a running server. The Docker Compose IoTDB
service is pinned to `apache/iotdb:2.0.8` for manual integration tests.
