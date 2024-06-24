"use client";

import { PageContainer } from "@ant-design/pro-components";

export default function Swagger() {

  return (
    <PageContainer title={false}>
      <div style={{ height: "100vh" }}>
        <iframe
          src="/api/swagger-ui/index.html"
          width="100%"
          height="100%"
          style={{border: "none"}}
        ></iframe>
      </div>
    </PageContainer>
  );
}
