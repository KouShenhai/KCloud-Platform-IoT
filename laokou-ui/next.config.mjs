// @ts-check

/**
 * @type {import('next').NextConfig}
 */

const nextConfig = {
    env: {
        APP_ENV: process.env.APP_ENV
      },
    async rewrites() {
        return [
            {
                source: "/laokou/:path*",
                destination: "http://127.0.0.1:9999/laokou/:path*",
                // destination: "https://laokou.org.cn/laokou/:path*",
            },
        ];
    },
}

export default nextConfig