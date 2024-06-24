import type { Metadata } from "next";
import { AntdRegistry } from "@ant-design/nextjs-registry";
import "./normalize.css";
import "@fortawesome/fontawesome-svg-core/styles.css"; // import Font Awesome CSS
import { config } from "@fortawesome/fontawesome-svg-core";
config.autoAddCss = false; // Tell Font Awesome to skip adding the CSS automatically since it's being imported above


export const metadata: Metadata = {
  title: "MorTnon RuoYi",
  description: "MorTnon 版本高级 RuoYi 前台",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>
          <AntdRegistry>{children}</AntdRegistry>
      </body>
    </html>
  );
}
