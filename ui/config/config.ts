import { defineConfig } from '@umijs/max';
import proxy from './proxy';
import routes from './routes';

const { REACT_APP_ENV = 'dev' } = process.env;

// @ts-ignore
export default defineConfig({
  antd: {},
  access: {},
  model: {},
  initialState: {},
  request: {},
  theme: {
    // 如果不想要 configProvide 动态设置主题需要把这个设置为 default
    // 只有设置为 variable， 才能使用 configProvide 动态设置主色调
    'root-entry-name': 'variable',
  },
  /**
   * 开启 hash 模式.
   * @description 让 build 之后的产物包含 hash 后缀，通常用于增量发布和避免浏览器加载缓存.
   * @doc https://umijs.org/docs/api/config#hash
   */
  hash: true,
  /**
   * 代理配置.
   * @description 本地服务器代理到后端服务器上，这样就可以访问服务器的数据.
   * @see 要注意以下 代理只能在本地开发时使用，build之后就无法使用.
   * @doc 代理介绍 https://umijs.org/docs/guides/proxy
   * @doc 代理配置 https://umijs.org/docs/api/config#proxy
   */
  proxy: proxy[REACT_APP_ENV as keyof typeof proxy],
  layout: {
    title: '老寇IoT云平台',
  },
  /**
   * 路由的配置，不在路由中引入的文件不会编译.
   * @description 只支持 path，component，routes，redirect，wrappers，title 的配置
   * @doc https://umijs.org/docs/guides/routes
   * @doc https://umijs.org/docs/routing
   */
  routes,
  mfsu: {
    strategy: 'normal',
  },
  npmClient: 'pnpm',
});
