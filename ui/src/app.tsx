// 运行时配置

// 全局初始化数据配置，用于 Layout 用户信息和权限初始化
// 更多信息见文档：https://umijs.org/docs/api/runtime-config#getinitialstate
import {Dropdown, message, theme} from "antd";
import {history} from "@umijs/max";
import {HomeOutlined, LogoutOutlined, RobotOutlined, SettingOutlined} from "@ant-design/icons";
import {ReactElement, ReactNode, ReactPortal} from "react";
import { logout, refresh } from '@/services/auth/auth';
import { clearToken, getAccessToken, getRefreshToken, setToken } from '@/access';
import React from "react";
import {RunTimeLayoutConfig} from "@@/plugin-layout/types";
import {getProfileUser} from "@/services/admin/user";
import {listUserTreeMenu} from "@/services/admin/menu";
import {ProBreadcrumb} from "@ant-design/pro-layout";
import axios from 'axios';

const getIcon = (icon: string) => {
	switch (icon) {
		case 'SettingOutlined': return <SettingOutlined/>
		case 'RobotOutlined': return <RobotOutlined/>
		default: return <SettingOutlined/>
	}
}

const getRouters = (menus: any[]) => {
	const routers = [{
		name: '首页',
		path: '/home',
		icon: <HomeOutlined/>
	}]
	if (menus.length > 0) {
		menus.forEach((item: any) => {
			item.icon = getIcon(item.icon)
			routers.push(item)
		})
	}
	return routers
}

export async function getInitialState(): Promise<{
	id: bigint;
	username: string;
	avatar: string;
	permissions: string[]
}> {
	const result = await getProfileUser().catch(console.log);
	return {
		id: result?.data?.id,
		username: result?.data?.username,
		avatar: result?.data?.avatar ? result?.data?.avatar : '/1.png',
		permissions: result?.data?.permissions,
	};
}

export const layout: RunTimeLayoutConfig  = ({ initialState }: any) => {
	return {
		// 面包屑配置
		headerContentRender: () => <ProBreadcrumb />,
		logo: '/logo.png',
		menu: {
			locale: false,
			params: initialState?.username,
			request: async () => {
				const result = await listUserTreeMenu({code: 0}).catch(console.log);
				return getRouters(result?.data)
			}
		},
		layout: 'mix',
		splitMenus: false,
		fixSiderbar: true,
		navTheme: "light",
		contentWidth: "Fluid",
		colorPrimary: "#1677ff",
		fixedHeader: true,
		siderMenuType: "sub",
		avatarProps: {
			src: initialState?.avatar,
			size: 'small',
			title: initialState?.username,
			render: (_props: any, dom: string | number | boolean | ReactElement | Iterable<ReactNode> | ReactPortal | null | undefined) => {
				return (
					<Dropdown
						menu={{
							items: [
								{
									key: 'logout',
									icon: <LogoutOutlined/>,
									label: '注销',
									onClick: async () => {
										// @ts-ignore
										logout({token: getAccessToken()}).then(() => {
											clearToken()
										})
										history.push('/login')
									},
								},
							],
						}}
					>
						{dom}
					</Dropdown>
				);
			}
		},
		token: {
			// bgLayout: 'rgb(16 18 26)', // layout 的背景颜色
			// colorTextAppListIcon: '#666', // 跨站点应用的图标颜色
			// colorTextAppListIconHover: 'rgba(0, 0, 0, 0.65)', // 跨站点应用的图标 hover 颜色
			// colorBgAppListIconHover: 'rgba(0, 0, 0, 0.04)', // 跨站点应用的图标 hover 背景颜色
			// 头部菜单的配置 ，这里具体看文档 https://procomponents.ant.design/components/layout
			// header: {
			// 	colorBgHeader: 'rgb(16 18 26)', // header 的背景颜色
			// 	colorHeaderTitle: '#ffffff', // header 的标题字体颜色
			// 	colorTextMenu: '#dee1f0', // menuItem 的字体颜色
			// 	colorTextMenuSecondary: '#dee1f0', // menu 的二级字体颜色，比如 footer 和 action 的 icon
			// 	colorTextMenuSelected: '#ffffff', // menuItem 的选中字体颜色
			// 	colorTextMenuActive: '#ffffff', // menuItem hover 的选中字体颜色
			// 	colorBgMenuItemHover: 'rgba(90, 75, 75, 0.03)', // menuItem 的 hover 背景颜色
			// 	colorBgMenuItemSelected: 'rgba(0, 0, 0, 0.04)', // menuItem 的选中背景颜色
			// 	colorTextRightActionsItem: '#dee1f0', // 右上角字体颜色
			// 	colorBgRightActionsItemHover: 'rgba(0, 0, 0, 0.03)', // 右上角选中的 hover 颜色
			// 	heightLayoutHeader: 60 // header 高度
			// },
			// 侧边菜单的配置 ，这里具体看文档 https://procomponents.ant.design/components/layout
			// sider: {
			// 	colorMenuBackground: 'rgb(16 18 26)', // menu 的背景颜色
			// 	colorTextMenuTitle: '#ffffff', // sider 的标题字体颜色
			// 	colorMenuItemDivider: 'transparent', // 	menuItem 分割线的颜色
			// 	colorTextMenu: '#dee1f0', // 	menuItem 的字体颜色
			// 	colorTextMenuSecondary: '#dee1f0', // menu 的二级字体颜色，比如 footer 和 action 的 icon
			// 	colorTextMenuSelected: '#ffffff', // menuItem 的选中字体颜色
			// 	colorTextMenuItemHover: '#ffffff', // menuItem 的 hover 字体颜色
			// 	colorTextMenuActive: '#ffffff', // menuItem hover 的选中字体颜色
			// 	colorBgMenuItemActive: 'rgba(0, 0, 0, 0.15)', // menuItem 的点击时背景颜色
			// 	colorBgMenuItemSelected: '#1677ff', // menuItem 的选中背景颜色
			// 	colorBgMenuItemHover: '#1677ff', // menuItem 的 hover 背景颜色
			// 	colorBgMenuItemCollapsedElevated: 'transparent', // 收起 menuItem 的弹出菜单背景颜色
			// 	colorBgCollapsedButton: '#ffffff', // 展开收起按钮背景颜色
			// 	colorTextCollapsedButton: '#dee1f0', // 展开收起按钮字体颜色
			// 	colorTextCollapsedButtonHover: '#dee1f0' // 展开收起按钮 hover 时字体颜色
			// }
		},
	};
};

let isRefreshToken = false;

export const request: {
	responseInterceptors: ((response: any) => any)[];
	requestInterceptors: (((config: any) => any) | ((error: any) => any))[];
	timeout: number;
	errorConfig: { errorThrower(): void; errorHandler(error: any): void }
} = {
	timeout: 60000,
	// other axios options you want
	errorConfig: {
		errorHandler(error: any) {
			const {request, response, code} = error;
			let errorMessage;
			if (response && response.data && response.data.error_description !== undefined) {
				errorMessage = response.data.error_description
			}
			if (response && response.status === 500) {
				errorMessage = '服务器内部错误，无法完成请求'
			}
			if (code === 'ERR_BAD_RESPONSE') {
				errorMessage = '网络请求错误，请稍后再试'
			}
			if (response && response.status === 400 && response.data.error === "invalid_grant") {
				errorMessage = "令牌续期失败，请重新登录"
			}
			if (response && response.status === 404) {
				errorMessage = "无法找到 " + request.responseURL + " 请求的资源"
			}
			message.error(errorMessage).then();
		},
		errorThrower() {
		},
	},
	// 请求拦截
	requestInterceptors: [
		async (config: any) => {
			const headers = config.headers ? config.headers : [];
			const accessToken = getAccessToken()
			if (accessToken) {
				headers['Authorization'] = `Bearer ${accessToken}`
			}
			return config;
		},
		(error: any) => {
			return error;
		},
	],
	// 响应拦截
	responseInterceptors: [
		async (response: any) => {
			const {status, data} = response;
			if (response.request?.responseType === 'blob' || response.request?.responseType === 'arraybuffer') {
				if(response.data.type === 'application/json') {
					const res = await new Response(response.data).json()
					message.error(res.msg).then();
				}
			}
			if (status === 200 && data.code === undefined) {
				response.data = {code: 'OK', msg: '请求成功', data: data};
			} else if (status === 200 && data.code !== 'OK') {
				if (data.code === "Unauthorized") {
					const refreshToken = getRefreshToken();
					if (refreshToken && !isRefreshToken) {
						isRefreshToken = true;
						// 清空令牌
						clearToken()
						// 刷新令牌
						refresh({refresh_token: refreshToken, grant_type: 'refresh_token'}).then((res) => {
							if (res.code === 'OK') {
								// 存储令牌
								setToken(res.data?.access_token, res.data?.refresh_token)
								// 续签提醒
								message.warning("令牌续期成功，请刷新页面或重新请求");
								// 重新请求
								return axios.request(response.config)
							}
						}).catch(() => {
							history.push('/login')
						}).finally(() => isRefreshToken = false);
					} else if (!refreshToken && !isRefreshToken) {
						history.push('/login')
					}
				} else {
					message.error(data.msg).then();
				}
			}
			return response;
		},
	],
};

export const antd: (memo: { theme: { algorithm?: any }; appConfig: { message: { maxCount: number } } }) => {
	theme: { algorithm?: any };
	appConfig: { message: { maxCount: number } }
} = (memo: {
	theme: { algorithm?: any; }; appConfig: {
		message: {
			// 配置 message 最大显示数，超过限制时，最早的消息会被自动关闭
			maxCount: number;
		};
	};
}) => {
	memo.theme ??= {};
	memo.theme.algorithm = theme.darkAlgorithm; // 配置 antd5 的预设 dark 算法

	memo.appConfig = {
		message: {
			// 配置 message 最大显示数，超过限制时，最早的消息会被自动关闭
			maxCount: 3,
		}
	}

	return memo;
};
