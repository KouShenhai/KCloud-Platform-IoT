## Context

The KCloud-Platform-IoT is an IoT Cloud Platform primarily built on Java (Spring Cloud/Alibaba). However, for edge deployments, a lightweight, high-performance edge gateway is necessary to manage devices locally without constant reliance on the cloud. The edge gateway requires its own device management interface and backend APIs. To optimize for performance and resource utilization at the edge, Go and the Hertz framework are chosen.

## Goals / Non-Goals

**Goals:**
- Implement a standalone Go-based backend for edge device management using ByteDance's Hertz framework.
- Implement a standalone UI or new module in the existing Umi Max frontend to interact with the edge gateway APIs.
- Build a custom authentication mechanism in the Go backend to secure edge APIs without central RBAC dependencies.
- Enable CRUD operations for devices at the edge.

**Non-Goals:**
- Synchronizing device data between the edge and the cloud (this design focuses on local edge management).
- Advanced device capabilities like OTA upgrades or Rule Engine execution at the edge (out of scope for this initial change).

## Decisions

- **Backend Framework**: Use ByteDance's Hertz for the Go backend. Rationale: High performance, ultra-low latency, and suitable for microservices or edge scenarios.
- **Authentication**: Implement JWT-based local authentication. Rationale: Edge gateways may operate in offline/disconnected environments, so they need a self-contained auth mechanism. The Hertz backend will issue and validate JWTs.
- **Frontend Stack**: Use React, TypeScript, and Umi Max. Rationale: Aligns with the existing project frontend stack, ensuring consistency and reuse of components (e.g., Ant Design Pro) where possible.

## Risks / Trade-offs

- **Tech Stack Divergence** -> *Mitigation*: The main platform uses Java, but edge uses Go. This introduces polyglot architecture. Mitigation is to ensure clear API contracts (REST) so the edge backend is treated as a black box by the rest of the system if integration is needed later.
- **Offline Authentication Data Loss** -> *Mitigation*: Local admin accounts must be provisioned during edge setup to ensure access when disconnected from the cloud.
