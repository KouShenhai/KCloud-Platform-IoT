
# 若依 React Pro

> 实现效果截图

- 浅色模式

1. 登录

![](https://images.bookhub.tech/ruoyi/login_light.png)

2. 首页

![](https://images.bookhub.tech/ruoyi/home_light.png)

3. 表单

![](https://images.bookhub.tech/ruoyi/table_light.png)

4. 统计

![](https://images.bookhub.tech/ruoyi/statis_light.png)

- 深色模式

1. 登录

![](https://images.bookhub.tech/ruoyi/login_dark.png)

2. 首页

![](https://images.bookhub.tech/ruoyi/home_dark.png)

3. 表单

![](https://images.bookhub.tech/ruoyi/table_dark.png)

4. 统计

![](https://images.bookhub.tech/ruoyi/statis_dark.png)

## 项目说明

![Static Badge](https://img.shields.io/badge/React-18.2.0-%236EB6FF) ![Static Badge](https://img.shields.io/badge/NextJS-14.1.0-%2349D49D) ![Static Badge](https://img.shields.io/badge/AntD-5.13.1-%23FFA94D) ![Static Badge](https://img.shields.io/badge/AntD%20ProComponets-2.6.48-%23E94F87)

![Static Badge](https://img.shields.io/badge/%E8%8B%A5%E4%BE%9D-%E6%94%AF%E6%8C%81-green)
![Static Badge](https://img.shields.io/badge/ESLint-PASS-green)

本项目为 RuoYi 前后端分离版本的前端适配项目。原作者的 RuoYi 前端项目使用 Vue 开发，本项目使用 React 开发，并引入了 Antd 和 Antd ProComponents，具有更高的美观度。

### 使用框架

- [Next.js](https://nextjs.org/)：主体 React 框架
- [Ant Design](https://ant-design.antgroup.com/index-cn)：页面组件
- [Ant Design ProComponents](https://procomponents.ant.design/)：高级封装页面组件
- [Font Awesome](https://fontawesome.com/)：图标库
- [cookies-next](https://github.com/andreizanik/cookies-next)：cookies 操作
- [jsencrypt](https://github.com/travist/jsencrypt)：加解密
- [ReactQuill](https://github.com/zenoamaro/react-quill)：富文本编辑框组件
- [React Spinner](https://www.davidhu.io/react-spinners/)：加载组件
- [React Cron](https://recron.emptyui.com/)：Cron 表达式生成器组件
- [react-countup](https://github.com/glennreyes/react-countup)：数字自动增长效果
- [react-chartjs-2](https://react-chartjs-2.js.org/)：图表组件
- [react-d3-speedometer](https://github.com/palerdot/react-d3-speedometer)：速度表盘组件

## 项目初始化

- 安装依赖

```bash
npm i
```

## 项目运行

- 配置本地环境变量

1. 根目录下新建 `.env.local` 文件
2. 配置 RuoYi 前后端分离版本中的[后端项目](https://github.com/yangzongzhuan/RuoYi-Vue)的访问地址，如：http://localhost:8080

示例内容如下：

```bash
BACKEND_URL=http://localhost:8080
```

- 启动项目

```bash
npm run dev
```

- 访问 [http://localhost:3000](http://localhost:3000)

## TODO

- 表单构建页面
- 代码生成页面
