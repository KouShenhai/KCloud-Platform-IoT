"use client";

import { PageContainer } from "@ant-design/pro-components";

export default function Druid() {

  return (
    <PageContainer title={false}>
      <div style={{ height: "100vh" }}>
        <iframe
          src="/api/druid/login.html"
          width="100%"
          height="100%"
          style={{border: "none"}}
        ></iframe>
      </div>
    </PageContainer>
  );
}
