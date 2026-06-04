## ADDED Requirements

### Requirement: JWT Authentication
The system SHALL require JWT authentication for all edge management APIs.

#### Scenario: Successful login
- **WHEN** admin provides valid local credentials
- **THEN** system returns a signed JWT

#### Scenario: API access with valid token
- **WHEN** client calls an API with a valid JWT in Authorization header
- **THEN** system grants access to the requested API

#### Scenario: API access with missing token
- **WHEN** client calls an API without a JWT
- **THEN** system rejects the request with HTTP 401 Unauthorized
