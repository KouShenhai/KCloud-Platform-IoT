## ADDED Requirements

### Requirement: Device Management APIs
The system SHALL provide APIs to register, view, update, and delete devices at the edge.

#### Scenario: Successful device registration
- **WHEN** user submits device details (name, type) with valid JWT
- **THEN** system registers the device and returns the device ID

#### Scenario: Successful device listing
- **WHEN** user requests device list with valid JWT
- **THEN** system returns all registered devices

### Requirement: Device Management Frontend
The system SHALL provide a frontend UI to interact with device management APIs.

#### Scenario: User views devices
- **WHEN** user navigates to device management page
- **THEN** the UI displays a list of devices fetched from the edge backend
