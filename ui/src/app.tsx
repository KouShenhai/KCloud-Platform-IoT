// 运行时配置

// 全局初始化数据配置，用于 Layout 用户信息和权限初始化
// 更多信息见文档：https://umijs.org/docs/api/runtime-config#getinitialstate
import {Dropdown, message, theme} from "antd";
// @ts-ignore
import {history} from 'umi';
import {LogoutOutlined} from "@ant-design/icons";
import {ReactElement, ReactNode, ReactPortal} from "react";
import {logoutV3} from "@/services/auth/logout";
import {clearToken, getAccessToken, getExpiresTime, getRefreshToken} from "@/access";

export async function getInitialState(): Promise<{
	name: string;
	avatar?: string;
}> {
	return {
		name: 'admin',
		avatar: '/1.jpg',
	};
}

export const layout = () => {
	return {
		logo: '/logo.png',
		menu: {
			locale: false,
		},
		layout: 'mix',
		splitMenus: false,
		fixSiderbar: true,
		navTheme: "light",
		contentWidth: "Fluid",
		colorPrimary: "#1677FF",
		fixedHeader: true,
		siderMenuType: "sub",
		avatarProps: {
			src: '/1.jpg',
			size: 'small',
			title: 'admin',
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
										logoutV3({token: getAccessToken()}).then(() => {
											clearToken()
											history.push('/login')
										})
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
			header: {
				colorBgHeader: 'rgb(16 18 26)', // header 的背景颜色
				colorHeaderTitle: '#ffffff', // header 的标题字体颜色
				colorTextMenu: '#dee1f0', // menuItem 的字体颜色
				colorTextMenuSecondary: '#dee1f0', // menu 的二级字体颜色，比如 footer 和 action 的 icon
				colorTextMenuSelected: '#ffffff', // menuItem 的选中字体颜色
				colorTextMenuActive: '#ffffff', // menuItem hover 的选中字体颜色
				colorBgMenuItemHover: 'rgba(90, 75, 75, 0.03)', // menuItem 的 hover 背景颜色
				colorBgMenuItemSelected: 'rgba(0, 0, 0, 0.04)', // menuItem 的选中背景颜色
				colorTextRightActionsItem: '#dee1f0', // 右上角字体颜色
				colorBgRightActionsItemHover: 'rgba(0, 0, 0, 0.03)', // 右上角选中的 hover 颜色
				heightLayoutHeader: 60 // header 高度
			},
			// 侧边菜单的配置 ，这里具体看文档 https://procomponents.ant.design/components/layout
			sider: {
				colorMenuBackground: 'rgb(16 18 26)', // menu 的背景颜色
				colorTextMenuTitle: '#ffffff', // sider 的标题字体颜色
				colorMenuItemDivider: 'transparent', // 	menuItem 分割线的颜色
				colorTextMenu: '#dee1f0', // 	menuItem 的字体颜色
				colorTextMenuSecondary: '#dee1f0', // menu 的二级字体颜色，比如 footer 和 action 的 icon
				colorTextMenuSelected: '#ffffff', // menuItem 的选中字体颜色
				colorTextMenuItemHover: '#ffffff', // menuItem 的 hover 字体颜色
				colorTextMenuActive: '#ffffff', // menuItem hover 的选中字体颜色
				colorBgMenuItemActive: 'rgba(0, 0, 0, 0.15)', // menuItem 的点击时背景颜色
				colorBgMenuItemSelected: '#1677ff', // menuItem 的选中背景颜色
				colorBgMenuItemHover: 'rgba(90, 75, 75, 0.03)', // menuItem 的 hover 背景颜色
				colorBgMenuItemCollapsedElevated: 'transparent', // 收起 menuItem 的弹出菜单背景颜色
				colorBgCollapsedButton: '#ffffff', // 展开收起按钮背景颜色
				colorTextCollapsedButton: '#dee1f0', // 展开收起按钮字体颜色
				colorTextCollapsedButtonHover: '#dee1f0' // 展开收起按钮 hover 时字体颜色
			}
		},
	};
};

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
			const {response, code} = error;
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
			message.error(errorMessage).then();
		},
		errorThrower() {
		},
	},
	// 请求拦截
	requestInterceptors: [
		(config: any) => {
			const headers = config.headers ? config.headers : [];
			// 令牌过期前5分钟刷新
			const time = 5 * 60 * 1000;
			const expiresTime = getExpiresTime();
			const diffTime = expiresTime - new Date().getTime()
			const refreshToken = getRefreshToken();
			if (expiresTime && refreshToken && diffTime >= 0 && diffTime <= time) {
				// 刷新令牌
			}
			const accessToken = getAccessToken() || '';
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
		(response: any) => {
			const {status, data} = response;
			if (status === 200 && data.code === undefined) {
				response.data = {code: 'OK', msg: '请求成功', data: data};
			} else if (status === 200 && data.code !== 'OK') {
				message.error(data.msg).then();
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
