## Why

The edge gateway requires a dedicated device management module to handle the provisioning, monitoring, and control of devices connected at the edge. A standalone backend and frontend are needed to provide management capabilities and APIs directly at the edge, ensuring low latency and continuous operation even when disconnected from the central cloud.

## What Changes

- Build a new frontend module using Umi Max for edge gateway device management.
- Develop a new Go-based backend service.
- Setup Go project scaffolding using ByteDance's Hertz HTTP framework.
- Implement custom authentication logic in the Go backend to secure the edge APIs.
- Define APIs for edge device lifecycle management (add, update, delete, list).

## Capabilities

### New Capabilities
- `edge-device-management`: Core APIs and frontend UI for managing devices on the edge gateway.
- `edge-auth`: Custom authentication logic for the edge gateway backend using Hertz.

### Modified Capabilities
None

## Impact

- **New Services**: Introduces a new Go microservice (backend) and a new frontend interface (or module in the existing Umi Max portal).
- **Tech Stack**: Introduces Go and the Hertz framework for the edge gateway backend.
- **Authentication**: Implements a standalone custom authentication mechanism at the edge gateway to handle localized security without relying entirely on the central cloud RBAC.
