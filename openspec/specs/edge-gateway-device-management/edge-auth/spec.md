## ADDED Requirements

### Requirement: JWT Authentication
The system SHALL require JWT authentication for all edge management APIs. JWT payload SHALL include `userId`, `username`, and `role` fields. The `role` field SHALL be used for permission control. Login SHALL authenticate against the dynamic user store rather than hardcoded credentials. Disabled users SHALL be rejected at login with HTTP 403.

#### Scenario: Successful login with dynamic credentials
- **WHEN** a user provides valid username and password matching a record in the user store, and the account is active
- **THEN** the system returns a signed JWT containing userId, username, and role fields

#### Scenario: Login with disabled account
- **WHEN** a user provides valid credentials but the account status is disabled
- **THEN** the system returns HTTP 403 with error message indicating account is disabled

#### Scenario: API access with valid token
- **WHEN** client calls an API with a valid JWT in Authorization header
- **THEN** system grants access to the requested API

#### Scenario: API access with missing token
- **WHEN** client calls an API without a JWT
- **THEN** system rejects the request with HTTP 401 Unauthorized

#### Scenario: Admin-only API access by operator role
- **WHEN** a user with operator role calls a user management API (POST/PUT/DELETE /api/v1/users)
- **THEN** system rejects the request with HTTP 403 Forbidden
