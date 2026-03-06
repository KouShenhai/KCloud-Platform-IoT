/**
 * umi 的路由配置.
 * @description 只支持 path,component,routes,redirect,wrappers,name,icon 的配置
 * @param path  path 只支持两种占位符配置，第一种是动态参数 :id 的形式，第二种是 * 通配符，通配符只能出现路由字符串的最后。
 * @param component 配置 location 和 path 匹配后用于渲染的 React 组件路径。可以是绝对路径，也可以是相对路径，如果是相对路径，会从 src/pages 开始找起。
 * @param routes 配置子路由，通常在需要为多个路径增加 layout 组件时使用。
 * @param redirect 配置路由跳转
 * @param wrappers 配置路由组件的包装组件，通过包装组件可以为当前的路由组件组合进更多的功能。 比如，可以用于路由级别的权限校验
 * @param name 配置路由的标题，默认读取国际化文件 menu.ts 中 menu.xxxx 的值，如配置 name 为 login，则读取 menu.ts 中 menu.login 的取值作为标题
 * @param icon 配置路由的图标，取值参考 https://ant.design/components/icon-cn， 注意去除风格后缀和大小写，如想要配置图标为 <StepBackwardOutlined /> 则取值应为 stepBackward 或 StepBackward，如想要配置图标为 <UserOutlined /> 则取值应为 user 或者 User
 * @doc https://umijs.org/docs/guides/routes
 */

export default [
	{
		path: '/',
		redirect: '/home',
	},
	{
		name: 'menu.home',
		title: 'menu.home',
		path: '/home',
		component: './Home',
		icon: 'home'
	},
	{
		name: 'menu.login',
		title: 'menu.login',
		path: '/login',
		component: './Login',
		layout: false,
	},
	{
		name: 'menu.sys',
		title: 'menu.sys',
		path: '/sys',
		icon: 'setting',
		routes: [
			{
				name: 'menu.sys.permission',
				title: 'menu.sys.permission',
				path: '/sys/permission',
				routes: [
					{
						name: 'menu.sys.permission.menu',
						title: 'menu.sys.permission.menu',
						path: '/sys/permission/menu',
						component: './Sys/Permission/menu'
					},
					{
						name: 'menu.sys.permission.dept',
						title: 'menu.sys.permission.dept',
						path: '/sys/permission/dept',
						component: './Sys/Permission/dept'
					},
					{
						name: 'menu.sys.permission.role',
						title: 'menu.sys.permission.role',
						path: '/sys/permission/role',
						component: './Sys/Permission/role'
					},
					{
						name: 'menu.sys.permission.user',
						title: 'menu.sys.permission.user',
						path: '/sys/permission/user',
						component: './Sys/Permission/user'
					},
				]
			},
			{
				name: 'menu.sys.log',
				title: 'menu.sys.log',
				path: '/sys/log',
				routes: [
					{
						name: 'menu.sys.log.login',
						title: 'menu.sys.log.login',
						path: '/sys/log/login',
						component: './Sys/Log/login'
					},
					{
						name: 'menu.sys.log.notice',
						title: 'menu.sys.log.notice',
						path: '/sys/log/notice',
						component: './Sys/Log/notice'
					},
					{
						name: 'menu.sys.log.operate',
						title: 'menu.sys.log.operate',
						path: '/sys/log/operate',
						component: './Sys/Log/operate'
					}
				]
			},
			{
				name: 'menu.sys.oss',
				title: 'menu.sys.oss',
				path: '/sys/oss',
				routes: [
					{
						name: 'menu.sys.oss.config',
						title: 'menu.sys.oss.config',
						path: '/sys/oss/config',
						component: './Sys/Oss/config'
					},
					{
						name: 'menu.sys.oss.log',
						title: 'menu.sys.oss.log',
						path: '/sys/oss/log',
						component: './Sys/Oss/log'
					}
				]
			},
			{
				name: 'menu.sys.config',
				title: 'menu.sys.config',
				path: '/sys/config',
				routes: [
					{
						name: 'menu.sys.config.generator',
						title: 'menu.sys.config.generator',
						path: '/sys/config/generator',
						component: './Sys/Config/generator'
					}
				]
			}
		]
	},
	{
		name: 'menu.iot',
		title: 'menu.iot',
		path: '/iot',
		icon: 'robot',
		routes: [
			{
				name: 'menu.iot.device',
				title: 'menu.iot.device',
				path: '/iot/device',
				routes: [
					{
						name: 'menu.iot.device.thingModel',
						title: 'menu.iot.device.thingModel',
						path: '/iot/device/thingModel',
						component: './IoT/Device/thingModel'
					},
					{
						name: 'menu.iot.device.productCategory',
						title: 'menu.iot.device.productCategory',
						path: '/iot/device/productCategory',
						component: './IoT/Device/productCategory'
					},
				]
			}
		]
	}
];
