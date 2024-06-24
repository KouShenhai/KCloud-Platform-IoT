/** @type {import('next').NextConfig} */

const nextConfig = {
  //代理重定向到后台服务
  async rewrites() {
    return {
      fallback: [
        {
          source: "/api/system/user",
          destination: `${process.env.BACKEND_URL}/system/user/`,
        },
        {
          source: "/api/:path*",
          destination: `${process.env.BACKEND_URL}/:path*`,
        },
      ],
    };
  },
  images: {
    dangerouslyAllowSVG: true,
    contentSecurityPolicy: "default-src 'self'; script-src 'none'; sandbox;",
    remotePatterns: [
      {
        protocol: "https",
        hostname: "img.shields.io",
        port: "",
        pathname: "/**",
      },
    ],
  },
};

module.exports = nextConfig;
